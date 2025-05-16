package com.suyh.base.mp.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.suyh.base.mp.handler.SqlHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置
 * 
 * @author ruoyi
 */
@MapperScan(basePackages = {"com.suyh0201.business.mapper", "com.suyh0201.sys.mapper"})
@Configuration
public class MybatisPlusConfiguration {
    @Bean
    public SqlHandler sqlHandler() {
        return new SqlHandler();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor();
    }
}
