

整体的使用，参考示例项目：`simple-example-app`







管理员初始帐号/密码：==admin/admin==



1. springboot 的版本：2.7.15

2. 如果需要对接口的返回值进行自动封装统一结构，需要实现接口 `WrapperResponseScanPackages` 并注册为bean 对象

3. 自定义错误码枚举 ErrorCodeEnums(类名自定义) 并实现接口 `IErrorCode`

   同时需要创建文件 `classpath:/META-INF/services/com.base.web.error.IErrorCode` 并将该枚举类的完全限定类名写在里面
   这里的作用是检查所定义的错误码是否有重复的code 值，如果未配置则无法检查错误码重复的问题
   ==约定 code 在 2,000,000 以内的数字留给sdk，业务相关的从 2,000,000 开始使用==

4. 国际化配置

   sdk 中的国际化配置文件目录为 message 如果业务的目录为其他，则需要将两个目录都配置上，如下：

   ```yaml
   spring:
     messages:
       # 要使用MessageSource 我们应该要提供 一个对应 的配置文件 。
       # 当前SDK 中，使用的目录是："messages"
       basename: "messages,i18n/messages"
       fallback-to-system-locale: false
   ```

   如果配置文件中找不到对应的key，则直接使用代码中枚举对应的msg。

5. 全局异常拦截处理已经添加，业务异常直接使用  `com.base.web.exception.ExceptionUtil` ，示例如下：

   ```java
   throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR)
   ```

6. 数据库

   1. 支持多数据源以及flywaydb

      由于兼容性问题，需要控制flyway-core 的版本号

      ```xml
      <flyway.version>7.15.0</flyway.version>
      <dependency>
          <groupId>org.flywaydb</groupId>
          <artifactId>flyway-core</artifactId>
          <version>${flyway.version}</version>
      </dependency>
      ```

   2. sdk 所需要的用户相关的SQL 存放在目录：`classpath:/sqls/mysql/sys/base`

      同时sdk 中的flyway 版本文件名采用格式：`V00_00_00_XXX__xxxxx.sql` 业务相关的版本文件名需要与其区分，==不要冲突了==

   3. 如果启用flyway 则需要将 sdk 相关的SQL 目录配置上

   4. sdk 所使用的数据源为默认数据源，所以用户相关的数据源需要处理为默认数据源，sql 目录配置也应该配置在默认数据源上面

   5. 多数据源配置示例

      首先，必须先禁用掉 Flyway 的自动配置

      ```yaml
      spring:
        flyway:
          # 系统实现的 FlywayAutoConfiguration 需要禁用掉
          enabled: false
      ```

      然后添加相关数据源的配置

      ```yaml
      base:
        datasource:
          hikari:
            master:
              jdbc-url: jdbc:mysql://192.168.8.143:3306/example_master?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai
              username: example_master
              password: example_master
              flyway:
                enabled: true
                locations:
                  - "sqls/mysql/sys/base"
                  - "sqls/mysql/master"
            slave:
              jdbc-url: jdbc:mysql://192.168.8.143:3306/example_slave?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai
              username: example_slave
              password: example_slave
              flyway:
                enabled: true
                locations:
                  - "sqls/mysql/slave"
      ```

7. 引入了spring-security 但是没有使用它的用户认证体系，需要将其相关的自动配置排除掉。

   添加如下配置项：

   ```yaml
   spring:
     autoconfigure:
       exclude:
         # 禁用spring-security 的用户相关的功能
         # 其主要作用是为 Spring Boot 应用提供默认的用户认证机制
         - org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
   ```

8. 日志，使用 logback ，直接在配置文件中配置日志目录以及文件名即可。

   默认日志文件：`/tmp/unknown.log`

   如下配置，日志文件将会生成：`/tmp/app/sys-info.log`

   ```yaml
   logging:
     file:
       path: "/tmp/app"
       name: "sys-info"
   ```

9. swagger 配置

   ```yaml
   knife4j:
     enable: true
   
   # swagger 页面访问： http://localhost:${server.servlet.context-path}/doc.html
   springdoc:
     group-configs:
       # 分组信息也可以在这里配置，在代码的配置类里面配置也是可以的。
       - group: 'ruoyi-system-user'
         paths-to-match:
           - "/system/user/**"
         packages-to-scan:
           - "com.web.ruoyi.controller"
   ```

10. 审计日志（页面操作日志记录，表：operation_record）

    自定义审核枚举，实现接口 IAudit，参考 `SysWebAuditEnums`

    用法：在需要日志的接口上面添加注解  `@AuditOperation`

    具体参考：`SysUserController.edit`

    ```java
        @AuditOperation("@audit.auditRecord(" +
                "T(com.web.sys.constants.enums.SysWebAuditEnums).SYSTEM_USER_EDIT, " +
                "#spelReturnValue, #request, #loginUser, " +
                "#user)")
        @PutMapping()
        public AjaxResult edit(
                @SuppressWarnings("unused") HttpServletRequest request,
                @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
                @Validated @RequestBody SysUser user)
        {
            return toAjax(editUser(loginUser, user));
        }
    ```

11. 忽略认证

    1. 默认情况下，有几种接口是不需要认证的，参考：`AbstractAuthenticationInterceptor#ignoreAuthPathPatterns`
    2. 对于controller 方法，使用注解 `@Permit(required = false)` 即可。
    3. 对于其他路径，需要实现接口 `IgnoreAuthPathPatternProvider` 并注册为bean 对象。

12. 用户token 密钥配置

    如果未配置，有一个默认的字符串

    ```yaml
    sys:
      web:
        user:
          token-secret-key: "r7X9cLp5Z2v8KbJ3n6F7d4H1m9s0Q8w"
    ```

13. excel 导入导出

    - 导入（`RuoyiExcelUtil`）
    - 导出（`ExcelExport`）

14. 菜单历史记录

    首个版本见：`sys-menu-v0.0.0.txt`

    ```sql
    -- 菜单表数据，用于每次版本升级对菜单调整的对比。
    SELECT 
      menu_id, menu_name, parent_id, order_num, path, component, query, 
      route_name, is_frame, is_cache, menu_type, visible, status, perms, 
      icon, remark, menu_key
    FROM sys_menu
    ORDER BY 
      menu_id, menu_name, parent_id, order_num, path, component, query, 
      route_name, is_frame, is_cache, menu_type, visible, status, perms, 
      icon, remark, menu_key;
    ```

    

    



