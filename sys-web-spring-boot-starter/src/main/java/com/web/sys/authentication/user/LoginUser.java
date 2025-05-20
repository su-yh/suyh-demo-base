package com.web.sys.authentication.user;

import com.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.base.web.exception.ExceptionUtil;
import com.base.web.user.AbstractLoginUser;
import com.web.ruoyi.mybatis.entity.SysUser;
import com.web.ruoyi.service.SysPermissionService;
import com.web.sys.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.Set;

@Slf4j
public class LoginUser extends AbstractLoginUser {
    public static final String NICK_NAME_KEY = "nickname";

    public LoginUser(UserService userService, SysPermissionService permissionService, Long id, String username, String nickname) {
        super(id, username, nickname);

        if (userService == null || id == null || username == null || nickname == null) {
            log.error("userService or id or username is null!, id: {}, username: {}, nickname: {}",
                    id, username, nickname);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }

        this.userService = userService;
        this.permissionService = permissionService;

    }

    private final UserService userService;
    private final SysPermissionService permissionService;



    /**
     * 权限列表
     */
    private volatile Set<String> permissions;

    private volatile SysUser user;

    public SysUser getUser() {
        if (user == null) {
            synchronized (this) {
                if (user == null) {
                    user = userService.obtainUserById(id);
                }
            }
        }

        return user;
    }

    @NonNull
    public Set<String> getPermissions() {
        if (permissions == null) {
            SysUser user = getUser();
            synchronized (this) {
                if (permissions == null) {
                    permissions = permissionService.getMenuPermission(user);
                }
            }
        }
        return permissions;
    }
}
