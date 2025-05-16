package com.suyh.base.web.error;

/**
 * @author suyh
 * @since 2025-05-16
 */
public interface IErrorCode {
    String ERROR_CODE_PREFIX = "error.code";

    int getCode();
    String getMsg();
}
