package com.suyh.base.web;

import com.suyh.base.web.properties.BaseMvcProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author suyh
 * @since 2025-05-16
 */
@ConditionalOnProperty(prefix = BaseMvcProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(BaseMvcProperties.class)
@AutoConfiguration
public class BaseMvcAutoConfiguration {
}
