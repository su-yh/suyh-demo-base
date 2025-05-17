package com.suyh.sys.web.audit;

import com.suyh.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.suyh.base.web.error.IErrorCode;
import com.suyh.base.web.exception.AbstractBusinessException;
import com.suyh.base.web.response.WrapperResponseBodyAdvice;
import com.suyh.base.web.response.annotation.WrapperResponseAdvice;
import com.suyh.base.web.rsp.R;
import com.suyh.sys.web.util.SpelParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @author suyh
 * @since 2024-10-10
 */
@Aspect
@Component
@Slf4j
public class AuditAop {
    // i18n 国际化
    private final MessageSource messageSource;
    private final BeanResolver beanResolver;

    public AuditAop(MessageSource messageSource, BeanFactory beanFactory) {
        this.messageSource = messageSource;
        this.beanResolver = new BeanFactoryResolver(beanFactory);
    }

    @Around(value = "@annotation(com.suyh.sys.web.audit.AuditOperation)", argNames = "pjp")
    public Object actionLog(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();

        // 参数
        Object[] args = pjp.getArgs();

        // 参数名称
        String[] parameterNames = signature.getParameterNames();

        // 目标方法
        Method targetMethod = signature.getMethod();

        Exception exp = null;
        Object originalReturnValue = null;
        Object spelReturnValue = null;
        try {
            originalReturnValue = pjp.proceed();
            spelReturnValue = originalReturnValue;
        } catch (AbstractBusinessException e) {
            String messageSourceCode = IErrorCode.ERROR_CODE_PREFIX + "." + e.getEc().getCode();
            spelReturnValue = messageSource.getMessage(messageSourceCode, e.getParams(),
                    "messages.properties lost id: " + messageSourceCode, Locale.SIMPLIFIED_CHINESE);
            exp = e;
        } catch (Exception e) {
            spelReturnValue = R.ofFail(BaseWebErrorCodeEnums.SERVICE_ERROR.getCode(), e.getMessage());
            exp = e;
        }

        try {
            do {
                // 方法上的日志注解
                AuditOperation auditOperation = targetMethod.getAnnotation(AuditOperation.class);
                if (auditOperation == null) {
                    break;
                }

                @Language("SpEL") String spelExpression = auditOperation.value();
                if (!StringUtils.hasText(spelExpression)) {
                    break;
                }

                Class<?> targetClass = pjp.getTarget().getClass();
                Class<?> returnClass = spelReturnValue == null ? null : spelReturnValue.getClass();
                WrapperResponseAdvice wrapperResponseAdvice = targetMethod.getAnnotation(WrapperResponseAdvice.class);
                if (WrapperResponseBodyAdvice.supportsWrapper(targetClass, returnClass, wrapperResponseAdvice)) {
                    spelReturnValue = R.ofSuccess(spelReturnValue);
                }

                StandardEvaluationContext context = new StandardEvaluationContext();
                context.setBeanResolver(beanResolver);
                for (int i = 0; i < args.length; i++) {
                    context.setVariable(parameterNames[i], args[i]);
                }

                context.setVariable("spelReturnValue", spelReturnValue);

                SpelParserUtils.parse(context, spelExpression, null);
            } while (false);
        } catch (Exception e) {
            log.error("audit log call failed.", e);
        }

        if (exp != null) {
            throw exp;
        }

        return originalReturnValue;
    }
}
