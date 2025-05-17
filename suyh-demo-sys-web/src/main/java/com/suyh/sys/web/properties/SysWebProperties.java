package com.suyh.sys.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author suyh
 * @since 2025-05-17
 */
@ConfigurationProperties(prefix = SysWebProperties.PREFIX)
@Data
public class SysWebProperties {
    public static final String PREFIX = "sys.web";

    private boolean enabled = true;


}
