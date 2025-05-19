package com.suyh.sys.web;

import com.suyh.sys.web.constants.SysWebConstants;
import com.suyh.sys.web.filter.TraceFilter;
import com.suyh.sys.web.properties.SysWebProperties;
import com.suyh.sys.web.response.SysWebWrapperResponseScanPackages;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author suyh
 * @since 2025-05-17
 */
@ConditionalOnProperty(prefix = SysWebProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SysWebProperties.class)
@ComponentScan(basePackages = {SysWebConstants.BASE_PACKAGE_CONTROLLER, SysWebConstants.BASE_PACKAGE_SERVICE, SysWebConstants.BASE_PACKAGE_COMPONENT})
@MapperScan(basePackages = SysWebConstants.BASE_PACKAGE_MAPPER)
@AutoConfiguration
public class SysWebAutoConfiguration {
    @Bean
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

    @Bean
    public SysWebWrapperResponseScanPackages sysWebWrapperResponseScanPackages() {
        return new SysWebWrapperResponseScanPackages();
    }
}
