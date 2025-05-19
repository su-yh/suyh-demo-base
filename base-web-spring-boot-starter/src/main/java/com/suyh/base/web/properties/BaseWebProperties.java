package com.suyh.base.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author suyh
 * @since 2025-05-16
 */
@ConfigurationProperties(prefix = BaseWebProperties.PREFIX)
@Data
public class BaseWebProperties {
    public static final String PREFIX = "base.web";

    private boolean enabled = true;
}
