package com.suyh.base.web.constants.enums;

import com.suyh.base.web.error.IErrorCode;

/**
 * 错误码枚举
 *
 * @author suyh
 * @since 2025-05-16
 */
public enum BaseWebErrorCodeEnums implements IErrorCode {
    NO_IMPLEMENT(1014001, "功能代码还未实现"),
    USER_NOT_LOGIN(1015005, "未登录"),
    SERVICE_ERROR(1015000, "服务错误"),
    ACCESS_DENIED(1014403, "禁止访问"),
    TOKEN_ERROR_OR_EXPIRE(1015006, "无效token"),

    ;

    private final int code;
    private final String msg;

    BaseWebErrorCodeEnums(int code, String msg) {
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
