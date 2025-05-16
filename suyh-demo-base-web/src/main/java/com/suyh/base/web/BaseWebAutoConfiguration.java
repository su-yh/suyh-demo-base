package com.suyh.base.web;

import com.suyh.base.web.advice.StringTrimmerControllerAdvice;
import com.suyh.base.web.configurer.BaseWebMvcConfigurer;
import com.suyh.base.web.error.BaseErrorAttributes;
import com.suyh.base.web.properties.BaseWebProperties;
import com.suyh.base.web.runner.ErrorCodeValidationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

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
    public ErrorCodeValidationRunner errorCodeValidationRunner() {
        return new ErrorCodeValidationRunner();
    }
}
