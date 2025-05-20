package com.web.sys.properties;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.validation.Valid;

/**
 * @author suyh
 * @since 2025-05-19
 */
@Data
public class SysUserProperties {
    /**
     * token 的有效时间
     */
    private Integer tokenSeconds = 30 * 60;

    @NestedConfigurationProperty
    @Valid
    private CaptchaProperties captcha = new CaptchaProperties();
}
