package com.web.sys.authentication;

import com.base.web.constants.BaseWebConstants;
import com.base.web.exception.ExceptionUtil;
import com.web.sys.authentication.annotation.CurrLoginUser;
import com.web.sys.authentication.user.LoginUser;
import com.web.sys.constants.enums.SysWebErrorCodeEnums;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义参数解析器的实现，该实现针对在Controller 的handler 接口方法中的参数做匹配。
 * 匹配上的参数，则会为该参数绑定上一个值，然后在handler 方法中就可以直接得到该值使用了。
 */
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        CurrLoginUser ann = parameter.getParameterAnnotation(CurrLoginUser.class);
        if (ann == null) {
            return false;
        }

        Class<?> parameterType = parameter.getParameterType();
        return LoginUser.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer container,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // 获取到拦截器放到属性中的user 对象
        assert request != null;

        Object currUser = request.getAttribute(BaseWebConstants.LOGIN_USER_ATTRIBUTE_KEY);
        if (currUser == null) {    // 用户未登录
            CurrLoginUser ann = parameter.getParameterAnnotation(CurrLoginUser.class);
            assert ann != null;
            if (ann.required()) {    // 用户必须登录
                throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_NOT_LOGIN);
            }
        }

        return currUser;
    }
}


