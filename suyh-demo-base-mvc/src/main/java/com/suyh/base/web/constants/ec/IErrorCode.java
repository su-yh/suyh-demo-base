package com.suyh.base.web.constants.ec;

/**
 * @author suyh
 * @since 2025-05-16
 */
public interface IErrorCode {
    String ERROR_CODE_PREFIX = "error.code";

    int getCode();
    String getMsg();
}
