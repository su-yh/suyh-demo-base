package com.base.web.runner;

import com.base.web.error.IErrorCode;
import com.base.web.util.ErrorCodeLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误码枚举的校验
 *
 * @author suyh
 * @since 2025-05-16
 */
@Slf4j
public class ErrorCodeDuplicationValidationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // <code, className>
        Map<Integer, String> errorCodeMap = new HashMap<>();

        ErrorCodeLoader loader = ErrorCodeLoader.load(IErrorCode.class, null);
        for (String className : loader) {
            Class<?> clazz = Class.forName(className);
            if (!clazz.isEnum()) {
                log.error("{} is not an Enum class", className);
                throw new RuntimeException(className + " is not an Enum class");
            }

            if (!IErrorCode.class.isAssignableFrom(clazz)) {
                log.error("{} is not implement {}", className, IErrorCode.class.getName());
                throw new RuntimeException(className + " is not implement " + IErrorCode.class.getName());
            }

            log.info("ErrorCode: {}", className);

            // 获取枚举的所有实例
            Object[] enumConstants = clazz.getEnumConstants();
            for (Object constant : enumConstants) {
                IErrorCode errorCode = (IErrorCode) constant;
                String historyClassName = errorCodeMap.get(errorCode.getCode());
                if (historyClassName != null) {
                    log.error("duplication code: {}, {} and {}", errorCode.getCode(), className, historyClassName);
                    throw new RuntimeException("");
                }

                errorCodeMap.put(errorCode.getCode(), className);
            }
        }

        log.info("error code duplication value scan pass.");
    }
}
