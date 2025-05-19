package com.suyh.sys.web.properties;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 验证码
 *
 * @author suyh
 * @since 2024-09-02
 */
@Data
public class CaptchaProperties {
    /**
     * 启用/禁用 2FA
     */
    private boolean twoFactorAuthEnabled = true;
    /**
     * google 验证码有效期时间窗口数量，每个窗口大小为30 秒
     * 当值为1 时有效时间为30 秒，值为2 时有效时间为60 秒
     * 最小值为1
     */
    @Min(1)
    private int googleAuthWindowSize = 1;
}
