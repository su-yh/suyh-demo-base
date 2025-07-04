package com.example;

import com.base.web.error.IErrorCode;
import com.example.constant.enums.ErrorCodeEnums;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author suyh
 * @since 2025-07-04
 */
@ActiveProfiles("suyh")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = SimpleExampleApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public class MessageSourceTest {
    @Resource
    private MessageSource messageSource;

    @Test
    public void testUnmatchedErrorCode() {
        IErrorCode ec = ErrorCodeEnums.UNKNOWN_ERROR;
        String messageSourceCode = IErrorCode.ERROR_CODE_PREFIX + "." + ec.getCode();
        Object[] params = new Object[] {"匹配参数"};
        Locale locale = Locale.CHINA;
        String message = messageSource.getMessage(messageSourceCode, params, ec.getMsg(), locale);
        log.info("message: {}", message);
    }
}
