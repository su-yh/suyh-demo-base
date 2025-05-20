package com.suyh.base.web;

import com.suyh.base.web.advice.StringTrimmerControllerAdvice;
import com.suyh.base.web.audit.AuditAop;
import com.suyh.base.web.configurer.BaseWebMvcConfigurer;
import com.suyh.base.web.error.BaseErrorAttributes;
import com.suyh.base.web.properties.BaseWebProperties;
import com.suyh.base.web.response.wrapper.WrapperResponseBodyAdvice;
import com.suyh.base.web.response.wrapper.WrapperResponseScanPackages;
import com.suyh.base.web.runner.ErrorCodeDuplicationValidationRunner;
import com.suyh.base.web.security.SecurityConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

/**
 * @author suyh
 * @since 2025-05-16
 */
@ConditionalOnProperty(prefix = BaseWebProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(BaseWebProperties.class)
@AutoConfiguration(before = ErrorMvcAutoConfiguration.class)
public class BaseWebAutoConfiguration {
    @Bean
    public StringTrimmerControllerAdvice stringTrimmerControllerAdvice() {
        return new StringTrimmerControllerAdvice();
    }

    @Bean
    public BaseWebMvcConfigurer baseWebMvcConfigurer() {
        return new BaseWebMvcConfigurer();
    }

    @Bean
    public BaseErrorAttributes baseErrorAttributes(MessageSource messageSource) {
        return new BaseErrorAttributes(messageSource);
    }

    @Bean
    public ErrorCodeDuplicationValidationRunner errorCodeValidationRunner() {
        return new ErrorCodeDuplicationValidationRunner();
    }

    @Bean
    public WrapperResponseBodyAdvice wrapperResponseBodyAdvice(
            ObjectProvider<WrapperResponseScanPackages> scanPackagesObjectProvider) {

        WrapperResponseBodyAdvice advice = new WrapperResponseBodyAdvice();

        for (WrapperResponseScanPackages wrapperResponseScanPackages : scanPackagesObjectProvider) {
            Collection<String> scanPackages = wrapperResponseScanPackages.getScanPackages();
            advice.addBasePackages(scanPackages);
        }

        return advice;
    }

    @Bean
    public SecurityConfiguration securityConfiguration() {
        return new SecurityConfiguration();
    }

    @Bean
    public AuditAop auditAop(MessageSource messageSource, BeanFactory beanFactory) {
        return new AuditAop(messageSource, beanFactory);
    }


}
