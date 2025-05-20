package com.web;

import com.web.ruoyi.service.SysPermissionService;
import com.web.sys.authentication.interceptor.AuthenticationInterceptor;
import com.web.sys.configurer.SysWebMvcConfigurer;
import com.web.sys.constants.SysWebConstants;
import com.web.sys.filter.TraceFilter;
import com.web.sys.properties.SysWebProperties;
import com.web.sys.response.SysWebWrapperResponseScanPackages;
import com.web.sys.service.UserService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author suyh
 * @since 2025-05-17
 */
@ConditionalOnProperty(prefix = SysWebProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SysWebProperties.class)
@ComponentScan(basePackages = {
        SysWebConstants.BASE_PACKAGE_CONTROLLER, SysWebConstants.BASE_PACKAGE_SERVICE, SysWebConstants.BASE_PACKAGE_COMPONENT,
        SysWebConstants.RUOYI_PACKAGE_CONTROLLER, SysWebConstants.RUOYI_PACKAGE_SERVICE, SysWebConstants.RUOYI_PACKAGE_COMPONENT
})
@MapperScan(basePackages = {
        SysWebConstants.BASE_PACKAGE_MAPPER,
        SysWebConstants.RUOYI_PACKAGE_MAPPER,
})
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

    @Bean
    public GoogleAuthenticator googleAuthenticator(SysWebProperties sysWebProperties) {
        int googleAuthWindowSize = sysWebProperties.getUser().getCaptcha().getGoogleAuthWindowSize();
        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder builder
                = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();
        builder.setWindowSize(googleAuthWindowSize * 2);
        GoogleAuthenticatorConfig config = builder.build();
        return new GoogleAuthenticator(config);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor(
            UserService userService, SysPermissionService permissionService) {
        return new AuthenticationInterceptor(userService, permissionService);
    }

    @Bean
    public SysWebMvcConfigurer sysWebMvcConfigurer() {
        return new SysWebMvcConfigurer();
    }
}
