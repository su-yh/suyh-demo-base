package com.suyh.sys.web.constants.enums;

import com.suyh.base.web.error.IErrorCode;

/**
 * @author suyh
 * @since 2025-05-17
 */
public enum SysWebErrorCodeEnums implements IErrorCode {
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
