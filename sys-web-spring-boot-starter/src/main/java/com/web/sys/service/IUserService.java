package com.web.sys.service;

import com.web.ruoyi.mybatis.entity.SysUser;

public interface IUserService {
    SysUser obtainUserById(Long id);

    String getBase64EncodedSecretKey();

    int createUser(SysUser entity);
}
