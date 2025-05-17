package com.suyh.base.web;

import com.suyh.base.web.advice.StringTrimmerControllerAdvice;
import com.suyh.base.web.configurer.BaseWebMvcConfigurer;
import com.suyh.base.web.error.BaseErrorAttributes;
import com.suyh.base.web.properties.BaseWebProperties;
import com.suyh.base.web.response.WrapperResponseBodyAdvice;
import com.suyh.base.web.response.WrapperResponseScanPackages;
import com.suyh.base.web.runner.ErrorCodeDuplicationValidationRunner;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@AutoConfiguration
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
}
