package com.example.constant.enums;

import com.base.web.error.IErrorCode;

/**
 * @author suyh
 * @since 2025-05-16
 */
public enum ErrorCodeEnums implements IErrorCode {
    UNKNOWN_ERROR(3001001, "Unknown Error：{0}"),

    ;

    private final int code;
    private final String msg;

    ErrorCodeEnums(int code, String msg) {
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
