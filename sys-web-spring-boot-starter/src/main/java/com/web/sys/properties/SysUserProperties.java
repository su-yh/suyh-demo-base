package com.web.sys.properties;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    /**
     * token 加解密的密钥
     */
    @NotBlank
    private String tokenSecretKey = "5bZ2x8D9p4K7QfJ3mN6Lg0C1hR5sT7uV9W";

    @NestedConfigurationProperty
    @Valid
    private CaptchaProperties captcha = new CaptchaProperties();
}
