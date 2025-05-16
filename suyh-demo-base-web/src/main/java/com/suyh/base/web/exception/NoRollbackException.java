package com.suyh.base.web.exception;

import com.suyh.base.web.error.IErrorCode;
import org.springframework.lang.NonNull;

/**
 * @author suyh
 * @since 2024-10-21
 */
public class NoRollbackException extends AbstractBusinessException {
    public NoRollbackException(@NonNull ExceptionCategory category, IErrorCode ec, Object... params) {
        super(category, ec, params);
    }
}
