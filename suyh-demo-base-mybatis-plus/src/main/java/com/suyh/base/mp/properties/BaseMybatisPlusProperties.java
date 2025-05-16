package com.suyh.base.mp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author suyh
 * @since 2025-05-16
 */
@ConfigurationProperties(prefix = BaseMybatisPlusProperties.PREFIX)
@Data
public class BaseMybatisPlusProperties {
    public static final String PREFIX = "base.mp";

    @NestedConfigurationProperty
    private final SqlHandlerProperties sqlHandler = new SqlHandlerProperties();
}
