package com.suyh.sys.web.user;

import com.suyh.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.suyh.base.web.exception.ExceptionUtil;
import com.suyh.base.web.user.AbstractLoginUser;
import com.suyh.sys.web.component.SysPermissionService;
import com.suyh.sys.web.mybatis.entity.SysUserEntity;
import com.suyh.sys.web.service.SysUserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class LoginUser extends AbstractLoginUser {
    public LoginUser(SysUserService userService, SysPermissionService permissionService, Long id, String username, String nickname) {
        if (userService == null || id == null || username == null || nickname == null) {
            log.error("userService or id or username is null!, id: {}, username: {}, nickname: {}",
                    id, username, nickname);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }

        this.userService = userService;
        this.permissionService = permissionService;
        this.id = id;
        this.username = username;
        this.nickname = nickname;
    }

    private final SysUserService userService;
    private final SysPermissionService permissionService;

    @Getter
    private final Long id;
    @Getter
    private final String username;
    @Getter
    private final String nickname;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    private volatile SysUserEntity user;

    public SysUserEntity getUser() {
        if (user == null) {
            synchronized (this) {
                if (user == null) {
                    user = userService.obtainUserById(id);
                }
            }
        }

        return user;
    }

    public Set<String> getPermissions() {
        if (permissions == null) {
            SysUserEntity user = getUser();
            synchronized (this) {
                if (permissions == null) {
                    permissions = permissionService.getMenuPermission(user);
                }
            }
        }
        return permissions;
    }
}
