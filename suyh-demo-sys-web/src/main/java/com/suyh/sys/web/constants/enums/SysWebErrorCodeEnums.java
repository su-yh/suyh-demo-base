package com.suyh.sys.web.constants.enums;

import com.suyh.base.web.error.IErrorCode;

/**
 * @author suyh
 * @since 2025-05-17
 */
public enum SysWebErrorCodeEnums implements IErrorCode {
    RUOYI_SYSTEM_USER_CREATE_USERNAME_EXISTS(1013101, "新增用户({0})失败，登录账号已存在"),
    RUOYI_SYSTEM_USER_CREATE_PHONE_NUMBER_EXISTS(1013102, "新增用户({0})失败，手机号码已存在"),
    RUOYI_SYSTEM_USER_CREATE_EMAIL_EXISTS(1013103, "新增用户({0})失败，邮箱账号已存在"),
    CANNOT_DELETE_DICT_TYPE(1013004, "字典类型：{0} 已分配,不能删除"),
    REFUSE_MODIFY_ADMIN_ROLE(1013002, "不允许操作超级管理员角色"),
    CANNOT_DELETE_ROLE(1013003, "角色：{0} 已分配,不能删除"),
    REFUSE_MODIFY_ADMIN_USER(1013001, "不允许操作超级管理员用户"),
    ;

    private final int code;
    private final String msg;

    SysWebErrorCodeEnums(int code, String msg) {
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
