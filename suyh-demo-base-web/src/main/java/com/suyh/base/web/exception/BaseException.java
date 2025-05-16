package com.suyh.base.web.exception;

import com.suyh.base.web.error.IErrorCode;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class BaseException extends AbstractBusinessException {

    public BaseException(@NonNull ExceptionCategory category, IErrorCode ec, Object... params) {
        super(category, ec, params);
    }
}
