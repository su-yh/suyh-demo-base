package com.suyh.ruoyi.web.service;

import com.suyh.ruoyi.web.mybatis.entity.SysRole;
import com.suyh.ruoyi.web.mybatis.entity.SysUser;
import com.suyh.ruoyi.web.util.RuoyiConstants;
import com.suyh.sys.web.authentication.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * RuoYi首创 自定义权限实现，ss取自SpringSecurity首字母
 *
 * @author ruoyi
 */
@Component("ss")
public class SysPermissionService {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    @NonNull
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add(RuoyiConstants.ALL_PERMISSION);
        } else {
            List<SysRole> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (SysRole role : roles) {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param expectPerms 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPerm(LoginUser loginUser, String... expectPerms) {
        if (loginUser == null) {
            return false;
        }

        Set<String> permissions = loginUser.getPermissions();
        if (permissions.isEmpty()) {
            return false;
        }

        if (expectPerms == null) {
            return false;
        }

        for (String expectPerm : expectPerms) {
            if (hasPermissions(permissions, expectPerm)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 拥有的权限列表
     * @param expectPerm 期望的权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String expectPerm) {
        if (permissions.contains(RuoyiConstants.ALL_PERMISSION)) {
            return true;
        }

        if (expectPerm == null) {
            return false;
        }

        return permissions.contains(expectPerm.trim());
    }
}
