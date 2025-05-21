
1. 如果需要对接口的返回值进行自动封装统一结构，需要实现接口 WrapperResponseScanPackages 并注册为bean 对象
2. 自定义错误码枚举 ErrorCodeEnums(类名自定义) 并实现接口 IErrorCode
    同时需要创建文件 classpath:/META-INF/services/com.base.web.error.IErrorCode 并将该枚举类的完全限定类名写在里面
    这里的作用是检查所定义的错误码是否有重复的code 值，如果未配置则无法检查错误码重复的问题
    约定 code 在 2000_000 以内的数字留给sdk，业务相关的从 2000_000 开始使用
3. 全局异常拦截处理已经添加，业务异常直接使用  com.base.web.exception.ExceptionUtil 即可
    例：throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR)
4. 数据库脚本
    4.1 支持多数据源以及flywaydb
        由于兼容性问题，需要控制flyway-core 的版本号
                <flyway.version>7.15.0</flyway.version>
                <dependency>
                    <groupId>org.flywaydb</groupId>
                    <artifactId>flyway-core</artifactId>
                    <version>${flyway.version}</version>
                </dependency>
    4.2 sdk 所需要的用户相关的SQL 存放在目录：classpath:/sqls/mysql/sys/base
        同时sdk 中的flyway 版本文件名采用格式：V00_00_00_XXX__xxxxx.sql 业务相关的版本文件名需要与其区分，不要冲突了
    4.3 如果启用flyway 则需要将 sdk 相关的SQL 目录配置上
    4.4 sdk 所使用的数据源为默认数据源，所以用户相关的数据源需要处理为默认数据源，sql 目录配置也应该配置在默认数据源上面
    4.5 多数据源配置示例
        首先，必须先禁用掉 Flyway 的自动配置
        spring:
          flyway:
            # 系统实现的 FlywayAutoConfiguration 需要禁用掉
            enabled: false

        多数据源的配置，以及flyway 的启用
        base:
          datasource:
            hikari:
              master:
                jdbc-url: jdbc:mysql://isuyh.com:3306/suyh?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai
                username: suyh
                password: suyh
                flyway:
                  enabled: true
                  locations:
                    - "sqls/mysql/sys/base"
                    - "sqls/mysql/master"
              slave:
                jdbc-url: jdbc:mysql://isuyh.com:3306/suyh?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai
                username: suyh
                password: suyh
                flyway:
                  enabled: false
5. 引入了spring-security 但是没有使用它的用户认证体系，需要将其相关的自动配置排除掉，添加如下配置项即可：
    spring:
      autoconfigure:
        exclude:
          # 禁用spring-security 的用户相关的功能
          # 其主要作用是为 Spring Boot 应用提供默认的用户认证机制
          - org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
6. 国际化配置
    sdk 中的国际化配置文件目录为 message 如果业务的目录为其他，则需要将两个目录都配置上，如下：
    spring:
      messages:
        # 两个国际化目录：message  i18n/messages
        basename: "messages,i18n/messages"
        fallback-to-system-locale: false
7. 日志，使用 logback ，直接在配置文件中配置日志目录以及文件名即可。
    默认日志文件：/tmp/unknown.log
    如下配置，日志文件将会生成：/tmp/app/sys-info.log
    logging:
      file:
        path: "/tmp/app"
        name: "sys-info"
8. swagger 配置
    # swagger 页面访问： http://localhost:${server.servlet.context-path}/doc.html
    参考如下配置：
    knife4j:
      enable: true
    springdoc:
      group-configs:
        # 分组信息也可以在这里配置，在代码的配置类里面配置也是可以的。
        - group: 'ruoyi-system-user'
          paths-to-match:
            - "/system/user/**"
          packages-to-scan:
            - "com.ruoyi.web.controller"
9. 审计日志（页面操作日志记录，表：operation_record）
    自定义审核枚举，实现接口 IAudit，参考 SysWebAuditEnums
    用法：在需要日志的接口上面添加注解  @AuditOperation
    具体参考：SysUserController.edit
    @AuditOperation("@audit.auditRecord(" +
            "T(com.sys.web.constants.enums.SysWebAuditEnums).SYSTEM_USER_EDIT, " +
            "#spelReturnValue, #request, #loginUser, " +
            "#user)")
    @PutMapping()
    public AjaxResult edit(
            @SuppressWarnings("unused") HttpServletRequest request,
            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
            @Validated @RequestBody SysUser user) {
        return toAjax(editUser(loginUser, user));
    }
10. 跳过认证
    10.0 默认情况下，有几种接口是不需要认证的，参考：AbstractAuthenticationInterceptor#ignoreAuthPathPatterns
    10.1 对于controller 方法，使用注解 @Permit(required = false) 即可。
    10.2 对于其他方式的，还没实现



