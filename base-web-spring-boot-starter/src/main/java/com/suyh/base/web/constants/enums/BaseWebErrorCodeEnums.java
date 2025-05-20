package com.suyh.base.web.constants.enums;

import com.suyh.base.web.error.IErrorCode;

/**
 * 错误码枚举
 *
 * @author suyh
 * @since 2025-05-16
 */
public enum BaseWebErrorCodeEnums implements IErrorCode {
    ACCESS_DENIED(1000403, "禁止访问"),
    NO_IMPLEMENT(1000404, "功能代码还未实现"),
    SERVICE_ERROR(1000500, "服务错误"),

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
