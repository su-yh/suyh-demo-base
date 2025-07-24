package com.web.sys.authentication.user;

import com.base.web.user.AbstractLoginUser;
import com.web.ruoyi.mybatis.entity.SysUser;
import com.web.ruoyi.service.SysPermissionService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.Set;

@Slf4j
public class LoginUser extends AbstractLoginUser {
    public LoginUser(@NonNull SysUser user, SysPermissionService permissionService) {
        this.user = user;
        this.permissionService = permissionService;
    }

    private final SysPermissionService permissionService;

    /**
     * 权限列表
     */
    private volatile Set<String> permissions;

    @Getter
    private final SysUser user;

    public Set<String> getPermissions() {
        if (permissions == null) {
            synchronized (this) {
                if (permissions == null) {
                    permissions = permissionService.getMenuPermission(user);
                }
            }
        }
        return permissions;
    }

    @Override
    public Long getId() {
        return user.getId();
    }

    @Override
    public String getNickname() {
        return user.getNickname();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
