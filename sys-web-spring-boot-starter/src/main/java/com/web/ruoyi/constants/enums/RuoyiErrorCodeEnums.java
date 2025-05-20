package com.web.ruoyi.constants.enums;

import com.base.web.error.IErrorCode;

/**
 * @author suyh
 * @since 2025-05-19
 */
public enum RuoyiErrorCodeEnums implements IErrorCode {
    RUOYI_SYSTEM_MENU_CREATE_FAILED_KEY_ERROR(1013308, "新增菜单({0})失败，菜单key已存在"),
    RUOYI_SYSTEM_MENU_CREATE_FAILED_NAME_EXISTS(1013301, "新增菜单({0})失败，菜单名称已存在"),
    RUOYI_SYSTEM_MENU_CREATE_FAILED_PATH_ERROR(1013302, "新增菜单({0})失败，地址必须以http(s)://开头"),
    RUOYI_SYSTEM_MENU_EDIT_FAILED_NAME_EXISTS(1013303, "修改菜单({0})失败，菜单名称已存在"),
    RUOYI_SYSTEM_MENU_EDIT_FAILED_PATH_ERROR(1013304, "修改菜单({0})失败，地址必须以http(s)://开头"),
    RUOYI_SYSTEM_MENU_EDIT_FAILED_PARENT_ERROR(1013305, "修改菜单({0})失败，上级菜单不能选择自己"),
    RUOYI_SYSTEM_MENU_DELETE_FAILED_CHILD_ERROR(1013306, "存在子菜单,不允许删除"),
    RUOYI_SYSTEM_MENU_DELETE_FAILED_ASSIGNED_ERROR(1013307, "菜单已分配,不允许删除"),
    RUOYI_SYSTEM_ROLE_CREATE_FAILED_NAME_EXISTS(1013201, "新增角色({0})失败，角色名称已存在"),
    RUOYI_SYSTEM_ROLE_CREATE_FAILED_KEY_EXISTS(1013202, "新增角色({0})失败，角色权限已存在"),
    RUOYI_SYSTEM_ROLE_EDIT_FAILED_NAME_EXISTS(1013203, "修改角色({0})失败，角色名称已存在"),
    RUOYI_SYSTEM_ROLE_EDIT_FAILED_KEY_EXISTS(1013204, "修改角色({0})失败，角色权限已存在"),
    RUOYI_SYSTEM_ROLE_EDIT_FAILED(1013205, "修改角色({0})失败，请联系管理员"),
    RUOYI_SYSTEM_USER_CREATE_USERNAME_EXISTS(1013101, "新增用户({0})失败，登录账号已存在"),
    RUOYI_SYSTEM_USER_CREATE_PHONE_NUMBER_EXISTS(1013102, "新增用户({0})失败，手机号码已存在"),
    RUOYI_SYSTEM_USER_CREATE_EMAIL_EXISTS(1013103, "新增用户({0})失败，邮箱账号已存在"),
    RUOYI_SYSTEM_USER_EDIT_USERNAME_EXISTS(1013104, "修改用户({0})失败，登录账号已存在"),
    RUOYI_SYSTEM_USER_EDIT_PHONE_NUMBER_EXISTS(1013105, "修改用户({0})失败，手机号码已存在"),
    RUOYI_SYSTEM_USER_EDIT_EMAIL_EXISTS(1013106, "修改用户({0})失败，邮箱账号已存在"),
    RUOYI_SYSTEM_USER_DELETE_FAILED(1013107, "当前用户不能删除"),
    CANNOT_DELETE_DICT_TYPE(1013004, "字典类型：{0} 已分配,不能删除"),
    REFUSE_MODIFY_ADMIN_ROLE(1013002, "不允许操作超级管理员角色"),
    CANNOT_DELETE_ROLE(1013003, "角色：{0} 已分配,不能删除"),
    REFUSE_MODIFY_ADMIN_USER(1013001, "不允许操作超级管理员用户"),

    ;

    private final int code;
    private final String msg;

    RuoyiErrorCodeEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
