package com.suyh.sys.web.controller;

import com.suyh.base.web.authentication.annotation.Permit;
import com.suyh.sys.web.mybatis.entity.SysUserEntity;
import com.suyh.sys.web.service.SysUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author suyh
 * @since 2025-05-16
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    // TODO: suyh - 测试用的
    @Permit(required = false)
    @GetMapping("/queryById")
    public SysUserEntity queryById(@RequestParam("id") Long id) {
        return sysUserService.obtainUserById(id);
    }

//    @Autowired
//    private ISysUserService userService;
//
//    @Autowired
//    private ISysRoleService roleService;

//    /**
//     * 获取用户列表
//     */
////    @PreAuthorize("@ss.hasPermi('system:user:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(
//            SysUser user)
//    {
//        startPage();
//        List<SysUser> list = userService.selectUserList(user);
//        if (list != null) {
//            for (SysUser sysUser : list) {
//                List<SysRole> sysRoles = roleService.selectRolePermissionListByUserId(sysUser.getUserId());
//                sysUser.setRoles(sysRoles);
//            }
//        }
//        return getDataTable(list);
//    }
//
//    /**
//     * 根据用户编号获取详细信息
//     */
////    @PreAuthorize("@ss.hasPermi('system:user:query')")
//    @GetMapping(value = { "/", "/{userId}" })
//    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
//    {
////        userService.checkUserDataScope(userId);
//        AjaxResult ajax = AjaxResult.success();
//        List<SysRole> roles = roleService.selectRoleAll();
//        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
////        ajax.put("posts", postService.selectPostAll());
//        if (RuoyiStringUtils.isNotNull(userId))
//        {
//            SysUser sysUser = userService.selectUserById(userId);
//            ajax.put(AjaxResult.DATA_TAG, sysUser);
////            ajax.put("postIds", postService.selectPostListByUserId(userId));
//            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
//        }
//        return ajax;
//    }
//
//    /**
//     * 新增用户
//     */
//    @AuditOperation("@audit.auditRecord(" +
//            "T(com.ebusiness.constant.enums.AuditEnums).SYSTEM_USER_CREATE, " +
//            "#spelReturnValue, #request, #loginUser, " +
//            "#user)")
//    @PostMapping()
//    @WrapperResponseAdvice(enabled = false)
//    public AjaxResult add(
//            @SuppressWarnings("unused") HttpServletRequest request,
//            @SuppressWarnings("unused")  @Parameter(hidden = true) @CurrUser LoginUser loginUser,
//            @Validated @RequestBody SysUser user) {
//        if (!sysUserService.checkUserNameUnique(user)) {
//            throw ExceptionUtil.business(SysWebErrorCodeEnums.RUOYI_SYSTEM_USER_CREATE_USERNAME_EXISTS, user.getUsername());
//        }
//        if (RuoyiStringUtils.isNotEmpty(user.getPhonenumber()) && !sysUserService.checkPhoneUnique(user)) {
//            throw ExceptionUtil.business(SysWebErrorCodeEnums.RUOYI_SYSTEM_USER_CREATE_PHONE_NUMBER_EXISTS, user.getUsername());
//        }
//        if (RuoyiStringUtils.isNotEmpty(user.getEmail()) && !sysUserService.checkEmailUnique(user)) {
//            throw ExceptionUtil.business(SysWebErrorCodeEnums.RUOYI_SYSTEM_USER_CREATE_EMAIL_EXISTS, user.getUsername());
//        }
//
//        sysUserService.insertUser(user);
//        return AjaxResult.success();
//    }
//
//    /**
//     * 修改用户
//     */
////    @PreAuthorize("@ss.hasPermi('system:user:edit')")
////    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
//    @AuditOperation("@audit.auditRecord(" +
//            "T(com.ebusiness.constant.enums.AuditEnums).SYSTEM_USER_EDIT, " +
//            "#spelReturnValue, #request, #loginUser, " +
//            "#user)")
//    @PutMapping()
//    public AjaxResult edit(
//            @SuppressWarnings("unused") HttpServletRequest request,
//            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
//            @Validated @RequestBody SysUser user)
//    {
////        userService.checkUserAllowed(user);
////        userService.checkUserDataScope(user.getUserId());
////        deptService.checkDeptDataScope(user.getDeptId());
////        roleService.checkRoleDataScope(user.getRoleIds());
//        return toAjax(editUser(loginUser, user));
//    }
//
//    @Data
//    public static class UserUpdatePassword {
//        @NotBlank
//        private String oldPassword;
//        @NotBlank
//        private String newPassword;
//    }
//
//    @Operation(summary = "修改当前用户密码")
//    @PostMapping("/profile/updatePwd")
//    public AjaxResult updatePwdSelf(
//            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
//            @RequestBody @Validated UserUpdatePassword body) {
//        customUserService.updatePwdByOldValue(loginUser.getId(), body.getOldPassword(), body.getNewPassword());
//        return success();
//    }
//
//    @Operation(summary = "查询当前登录用户的信息")
//    @GetMapping("/profile")
//    public AjaxResult querySelf(
//            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser) {
//        SysUser user = loginUser.getUser();
//        return success(user);
//    }
//
//    @Operation(summary = "修改登录用户自己的相关信息")
//    @PutMapping("/profile")
//    public AjaxResult editSelf(
//            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
//            @Validated @RequestBody SysUser user) {
//        user.setId(loginUser.getId());
//        return toAjax(editUser(loginUser, user));
//    }
//
//    private int editUser(LoginUser loginUser, SysUser user) {
//        if (!userService.checkUserNameUnique(user)) {
//            throw ExceptionUtil.business(ErrorCodeConstants.RUOYI_SYSTEM_USER_EDIT_USERNAME_EXISTS, user.getUsername());
//        }
//        if (RuoyiStringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
//            throw ExceptionUtil.business(ErrorCodeConstants.RUOYI_SYSTEM_USER_EDIT_PHONE_NUMBER_EXISTS, user.getUsername());
//        }
//        if (RuoyiStringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
//            throw ExceptionUtil.business(ErrorCodeConstants.RUOYI_SYSTEM_USER_EDIT_EMAIL_EXISTS, user.getUsername());
//        }
//
//        // 这三个不能在这里修改。
//        user.setPassword(null);
//        user.setSalt(null);
//        user.setTwoFactorAuthKey(null);
//
//        user.setUpdateBy(loginUser.getNickname());
//        return userService.updateUser(user);
//    }
//
//    /**
//     * 删除用户
//     */
////    @PreAuthorize("@ss.hasPermi('system:user:remove')")
////    @Log(title = "用户管理", businessType = BusinessType.DELETE)
//    @AuditOperation("@audit.auditRecord(" +
//            "T(com.ebusiness.constant.enums.AuditEnums).SYSTEM_USER_DELETE, " +
//            "#spelReturnValue, #request, #loginUser, " +
//            "#userIds)")
//    @DeleteMapping("/{userIds}")
//    public AjaxResult remove(
//            @SuppressWarnings("unused") HttpServletRequest request,
//            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
//            @PathVariable Long[] userIds)
//    {
//        if (ArrayUtils.contains(userIds, loginUser.getId())) {
//            throw ExceptionUtil.business(ErrorCodeConstants.RUOYI_SYSTEM_USER_DELETE_FAILED);
//        }
//        return toAjax(userService.deleteUserByIds(userIds));
//    }
//
//    /**
//     * 重置密码
//     */
////    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
////    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
//    @PutMapping("/resetPwd")
//    public AjaxResult resetPwd(@RequestBody SysUser user)
//    {
////        userService.checkUserAllowed(user);
////        userService.checkUserDataScope(user.getUserId());
//        customUserService.updateUserPwd(user.getId(), user.getPassword());
//        return success();
//    }
//
//    /**
//     * 状态修改
//     */
////    @PreAuthorize("@ss.hasPermi('system:user:edit')")
////    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
//    @PutMapping("/changeStatus")
//    public AjaxResult changeStatus(
//            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser,
//            @RequestBody SysUser user)
//    {
////        userService.checkUserAllowed(user);
////        userService.checkUserDataScope(user.getUserId());
//        user.setUpdateBy(loginUser.getNickname());
//        return toAjax(userService.updateUserStatus(user));
//    }
//
//    /**
//     * 根据用户编号获取授权角色
//     */
////    @PreAuthorize("@ss.hasPermi('system:user:query')")
//    @GetMapping("/authRole/{userId}")
//    public AjaxResult authRole(@PathVariable("userId") Long userId)
//    {
//        AjaxResult ajax = AjaxResult.success();
//        SysUser user = userService.selectUserById(userId);
//        List<SysRole> roles = roleService.selectRolesByUserId(userId);
//        ajax.put("user", user);
//        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
//        return ajax;
//    }
//
//    /**
//     * 用户授权角色
//     */
////    @PreAuthorize("@ss.hasPermi('system:user:edit')")
////    @Log(title = "用户管理", businessType = BusinessType.GRANT)
//    @PutMapping("/authRole")
//    public AjaxResult insertAuthRole(Long userId, Long[] roleIds)
//    {
////        userService.checkUserDataScope(userId);
////        roleService.checkRoleDataScope(roleIds);
//        userService.insertUserAuth(userId, roleIds);
//        return success();
//    }
}
