package com.web.sys.service;

import com.web.ruoyi.mybatis.entity.SysUser;
import org.springframework.lang.NonNull;

public interface IUserService {
    SysUser obtainUserById(Long id);

    String getBase64EncodedSecretKey();

    int createUser(SysUser entity);

    void updatePwdByOldValue(
            @NonNull Long userId, @NonNull String oldPassword, @NonNull String newPassword);

    void updateUserPwd(@NonNull Long userId, @NonNull String password);

    String login(@NonNull String username, @NonNull String password, @NonNull Integer code);

    String resetTwoFactorAuthKey(@NonNull Long id);
}
