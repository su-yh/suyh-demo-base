package com.suyh.sys.web.constants.enums;

import com.suyh.base.web.error.IErrorCode;

/**
 * @author suyh
 * @since 2025-05-17
 */
public enum SysWebErrorCodeEnums implements IErrorCode {
    PARAMETER_ERROR(1015001, "参数错误"),
    USER_NOT_LOGIN(1015005, "未登录"),
    TOKEN_ERROR_OR_EXPIRE(1015006, "无效token"),
    USER_EXISTS(1015007, "用户({0}) 已存在"),
    USER_BAD_CREDENTIALS(1015008, "用户名或者密码错误"),
    USER_LOGIN_CAPTCHA_ERROR(1015009, "验证码错误"),
    USER_NOT_EXISTS(1015010, "用户({0}) 不存在"),
    USER_OLD_PASSWORD_NOT_MATCH(1015023, "修改密码失败，旧密码不匹配"),
    FILE_NOT_EXISTS(1015024, "文件 ({0}) 不存在"),

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
