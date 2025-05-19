package com.suyh.ruoyi.web.controller;

import com.suyh.base.web.authentication.annotation.CurrLoginUser;
import com.suyh.base.web.user.LoginUser;
import com.suyh.ruoyi.web.component.SysUserComponent;
import com.suyh.ruoyi.web.domain.AjaxResult;
import com.suyh.ruoyi.web.mybatis.entity.SysMenu;
import com.suyh.ruoyi.web.mybatis.entity.SysUser;
import com.suyh.ruoyi.web.service.ISysMenuService;
import com.suyh.ruoyi.web.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/online")
public class SysLoginController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo(@Parameter(hidden = true) @CurrLoginUser LoginUser loginUser)
    {
        SysUser user = SysUserComponent.getUser(loginUser);
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters(@Parameter(hidden = true) @CurrLoginUser LoginUser loginUser)
    {
        Long userId = loginUser.getId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
