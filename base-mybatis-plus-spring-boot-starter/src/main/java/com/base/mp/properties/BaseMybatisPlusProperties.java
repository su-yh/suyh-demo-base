package com.base.mp.properties;

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

    private boolean enabled = true;

    @NestedConfigurationProperty
    private final SqlHandlerProperties sql = new SqlHandlerProperties();
}
