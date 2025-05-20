package com.web.ruoyi.controller;

import com.base.web.audit.AuditOperation;
import com.base.web.exception.ExceptionUtil;
import com.web.ruoyi.constants.enums.RuoyiErrorCodeEnums;
import com.web.ruoyi.domain.AjaxResult;
import com.web.ruoyi.domain.page.TableDataInfo;
import com.web.ruoyi.mybatis.entity.SysRole;
import com.web.ruoyi.mybatis.entity.SysUser;
import com.web.ruoyi.mybatis.entity.SysUserRole;
import com.web.ruoyi.service.ISysRoleService;
import com.web.ruoyi.service.ISysUserService;
import com.web.sys.authentication.annotation.CurrLoginUser;
import com.web.sys.authentication.user.LoginUser;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    @Autowired
    private ISysRoleService roleService;

//    @Autowired
//    private TokenService tokenService;
//
//    @Autowired
//    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

//    @Autowired
//    private ISysDeptService deptService;

//    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

//    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('system:role:export')")
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SysRole role)
//    {
//        List<SysRole> list = roleService.selectRoleList(role);
//        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
//        util.exportExcel(response, list, "角色数据");
//    }

    /**
     * 根据角色编号获取详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId)
    {
//        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
//    @PreAuthorize("@ss.hasPermi('system:role:add')")
//    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @AuditOperation("@audit.auditRecord(" +
            "T(com.web.sys.constants.enums.SysWebAuditEnums).SYSTEM_ROLE_CREATE, " +
            "#spelReturnValue, #request, #loginUser, " +
            "#role)")
    @PostMapping
    public AjaxResult add(
            @SuppressWarnings("unused") HttpServletRequest request,
            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
            @Validated @RequestBody SysRole role)
    {
        if (!roleService.checkRoleNameUnique(role)) {
            throw ExceptionUtil.business(RuoyiErrorCodeEnums.RUOYI_SYSTEM_ROLE_CREATE_FAILED_NAME_EXISTS, role.getRoleName());
        }
        if (!roleService.checkRoleKeyUnique(role)) {
            throw ExceptionUtil.business(RuoyiErrorCodeEnums.RUOYI_SYSTEM_ROLE_CREATE_FAILED_KEY_EXISTS, role.getRoleName());
        }
        role.setCreateBy(loginUser.getNickname());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
//    @PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @AuditOperation("@audit.auditRecord(" +
            "T(com.web.sys.constants.enums.SysWebAuditEnums).SYSTEM_ROLE_EDIT, " +
            "#spelReturnValue, #request, #loginUser, " +
            "#role)")
    @PutMapping
    public AjaxResult edit(
            @SuppressWarnings("unused") HttpServletRequest request,
            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
            @Validated @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
//        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role)) {
            throw ExceptionUtil.business(RuoyiErrorCodeEnums.RUOYI_SYSTEM_ROLE_EDIT_FAILED_NAME_EXISTS, role.getRoleName());
        }
        else if (!roleService.checkRoleKeyUnique(role)) {
            throw ExceptionUtil.business(RuoyiErrorCodeEnums.RUOYI_SYSTEM_ROLE_EDIT_FAILED_KEY_EXISTS, role.getRoleName());
        }
        role.setUpdateBy(loginUser.getNickname());
        
        if (roleService.updateRole(role) > 0)
        {
//            // 更新缓存用户权限
//            LoginUser loginUser = getLoginUser();
//            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin())
//            {
//                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
//                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
//                tokenService.setLoginUser(loginUser);
//            }
            return success();
        }

        throw ExceptionUtil.business(RuoyiErrorCodeEnums.RUOYI_SYSTEM_ROLE_EDIT_FAILED, role.getRoleName());
    }

    /**
     * 修改保存数据权限
     */
//    @PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
//        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
//    @PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(
            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
            @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
//        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(loginUser.getNickname());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
//    @PreAuthorize("@ss.hasPermi('system:role:remove')")
//    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @AuditOperation("@audit.auditRecord(" +
            "T(com.web.sys.constants.enums.SysWebAuditEnums).SYSTEM_ROLE_DELETE, " +
            "#spelReturnValue, #request, #loginUser, " +
            "#roleIds)")
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(
            @SuppressWarnings("unused") HttpServletRequest request,
            @SuppressWarnings("unused") @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
            @PathVariable Long[] roleIds)
    {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
//    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return success(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     */
//    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
//    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
//    @PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole)
    {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
//    @PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds)
    {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
//    @PreAuthorize("@ss.hasPermi('system:role:edit')")
//    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds)
    {
//        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 获取对应角色部门树列表
     */
//    @PreAuthorize("@ss.hasPermi('system:role:query')")
//    @GetMapping(value = "/deptTree/{roleId}")
//    public AjaxResult deptTree(@PathVariable("roleId") Long roleId)
//    {
//        AjaxResult ajax = AjaxResult.success();
//        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
//        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
//        return ajax;
//    }
}
