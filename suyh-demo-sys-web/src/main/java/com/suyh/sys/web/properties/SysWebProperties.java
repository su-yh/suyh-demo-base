package com.suyh.sys.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @author suyh
 * @since 2025-05-17
 */
@ConfigurationProperties(prefix = SysWebProperties.PREFIX)
@Validated
@Data
public class SysWebProperties {
    public static final String PREFIX = "sys.web";

    private boolean enabled = true;

    @NestedConfigurationProperty
    @Valid
    private final SysUserProperties user = new SysUserProperties();
}
