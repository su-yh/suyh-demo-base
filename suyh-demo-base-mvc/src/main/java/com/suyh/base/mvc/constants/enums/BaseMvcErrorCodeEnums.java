package com.suyh.base.mvc.constants.enums;

import com.suyh.base.mvc.constants.ec.IErrorCode;

/**
 * @author suyh
 * @since 2025-05-16
 */
public enum BaseMvcErrorCodeEnums implements IErrorCode {
    USER_NOT_LOGIN(1015005, "未登录"),
    SERVICE_ERROR(1015000, "服务错误"),
    ACCESS_DENIED(1014403, "禁止访问"),
    TOKEN_ERROR_OR_EXPIRE(1015006, "无效token"),

    ;

    private final int code;
    private final String msg;

    BaseMvcErrorCodeEnums(int code, String msg) {
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
