package com.suyh.base.web.runner;

import com.suyh.base.web.error.IErrorCode;
import com.suyh.base.web.util.ErrorCodeLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * @author suyh
 * @since 2025-05-16
 */
@Slf4j
public class ErrorCodeValidationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO: suyh - 检查所有的枚举是否有重复的地方
        ErrorCodeLoader loader = ErrorCodeLoader.load(IErrorCode.class, null);
        for (String className : loader) {
            System.out.println("name: " + className);

            Class<?> clazz = Class.forName(className);

            // 检查是否为枚举
            if (clazz.isEnum()) {
                System.out.println(className + " 是一个枚举类");

                // 获取枚举的所有实例
                Object[] enumConstants = clazz.getEnumConstants();
                for (Object constant : enumConstants) {
                    Enum<?> enumConstant = (Enum<?>) constant;
                    System.out.println("枚举常量: " + enumConstant.name());

                    // 如果枚举实现了特定接口，可以进一步转换
                    if (constant instanceof IErrorCode) {
                        IErrorCode errorCode = (IErrorCode) constant;
                        System.out.println("错误码: " + errorCode.getCode());
                    }
                }
            } else {
                System.out.println(className + " 不是枚举类");
            }
        }
    }
}
