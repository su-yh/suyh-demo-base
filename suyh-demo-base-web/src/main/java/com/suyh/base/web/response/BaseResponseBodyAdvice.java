package com.suyh.base.web.response;

import com.suyh.base.web.response.annotation.WrapperResponseAdvice;
import com.suyh.base.web.rsp.R;
import com.suyh.base.web.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * 由业务方，自行调用创建bean 对象，业务方自己会知道它的controller 的包路径。
 * 这个是处理统一的返回值的，将所有的返回值都封装到一个公共的模板({@link R})中，
 * 这样在Controller 的接口中可以直接返回实际的返回值对象，在不需要有返回值的情况也可以直接添加void 作为返回值。
 */
@ControllerAdvice
@RequiredArgsConstructor
public class BaseResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final List<String> basePackages;

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> containingClass = returnType.getContainingClass();
        WrapperResponseAdvice wrapperResponseAdvice = returnType.getMethodAnnotation(WrapperResponseAdvice.class);

        // 只对指定包路径下的返回值类型进行自动封装
        boolean flag = false;
        if (basePackages != null) {
            for (String basePackage : basePackages) {
                if (containingClass.getPackage().getName().startsWith(basePackage)) {
                    flag = true;
                    break;
                }
            }
        }

        if (!flag) {
            return false;
        }

        return BaseResponseBodyAdvice.supportsWrapper(returnType.getParameterType(), wrapperResponseAdvice);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {

        if (String.class.isAssignableFrom(returnType.getParameterType())) {
            R<Object> result = R.ofSuccess(body);
            return JsonUtils.serializable(result);
        }

        return R.ofSuccess(body);
    }

    /**
     * 是否支持将返回值重新封装
     *
     * @param returnClass           返回值的类对象
     * @param wrapperResponseAdvice 方法注解
     * @return true: 支持，false: 禁止
     */
    private static boolean supportsWrapper(
            Class<?> returnClass, WrapperResponseAdvice wrapperResponseAdvice) {

        if (returnClass != null) {
            if (ResponseEntity.class.isAssignableFrom(returnClass)) {
                return false;
            }

            if (R.class.isAssignableFrom(returnClass)) {
                return false;
            }
        }

        if (wrapperResponseAdvice != null && !wrapperResponseAdvice.enabled()) {
            return false;
        }

        return true;
    }

}
