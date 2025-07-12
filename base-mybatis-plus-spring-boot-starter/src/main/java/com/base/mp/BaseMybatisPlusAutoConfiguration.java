package com.base.mp;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusInnerInterceptorAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.base.mp.handler.SqlHandler;
import com.base.mp.properties.BaseMybatisPlusProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 验证码配置
 * 
 * @author ruoyi
 */
@ConditionalOnProperty(prefix = BaseMybatisPlusProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(BaseMybatisPlusProperties.class)
@AutoConfiguration(before = MybatisPlusInnerInterceptorAutoConfiguration.class)
public class BaseMybatisPlusAutoConfiguration {

    @ConditionalOnProperty(prefix = BaseMybatisPlusProperties.PREFIX, name = "sql.enabled", havingValue = "true", matchIfMissing = true)
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
