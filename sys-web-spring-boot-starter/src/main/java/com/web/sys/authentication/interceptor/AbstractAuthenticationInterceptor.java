package com.web.sys.authentication.interceptor;

import com.base.web.constants.BaseWebConstants;
import com.base.web.exception.ExceptionUtil;
import com.web.ruoyi.constants.UserConstants;
import com.web.sys.authentication.annotation.Permit;
import com.web.sys.authentication.user.LoginUser;
import com.web.sys.constants.enums.SysWebErrorCodeEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 处理用户认证拦截器
 */
@Slf4j
public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {
    protected final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 对于非业务API 接口忽略认证的API 配置
     * 如果是业务相关的API 接口忽略认证使用注解{@link Permit}
     */
    protected final Set<String> ignoreAuthPathPatterns = new HashSet<>(Arrays.asList(
            // spring mvc 基础错误重定向API 接口
            "/error",
            "/**/*.js", "/**/*.css",
            // knife4j
            "/doc.html", "/v3/api-docs/**"));

    public void addIgnoreAuthPathPatterns(Collection<String> pathPatterns) {
        if (pathPatterns == null) {
            return;
        }

        for (String pathPattern : pathPatterns) {
            if (StringUtils.hasText(pathPattern)) {
                ignoreAuthPathPatterns.add(pathPattern.trim());
            }
        }
    }

    // 这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        HandlerMethod handlerMethod = (handler instanceof HandlerMethod) ? (HandlerMethod) handler : null;
        if (handlerMethod == null) {
            return true;
        }

        authentication(request, handlerMethod);

        return true;
    }

    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

        request.removeAttribute(BaseWebConstants.LOGIN_USER_ATTRIBUTE_KEY);
    }

    // 认证
    protected void authentication(@NonNull HttpServletRequest request, @NonNull HandlerMethod handlerMethod) {
        // 匹配无需认证路径
        String servletPath = request.getServletPath();
        for (String pattern : ignoreAuthPathPatterns) {
            if (antPathMatcher.match(pattern, servletPath)) {
                return;
            }
        }

        // 匹配无需认证注解
        Permit permit = handlerMethod.getMethodAnnotation(Permit.class);
        if (permit != null && !permit.required()) {
            return;
        }

        String userToken = getUserToken(request);
        LoginUser loginUser = parseUserToken(userToken);

        // 正常登录
        if (loginUser != null) {
            String status = loginUser.getUser().getStatus();
            if (status == null || !status.trim().equals(UserConstants.NORMAL)) {
                throw ExceptionUtil.business(SysWebErrorCodeEnums.SYSTEM_USER_USER_DISABLED);
            }

            request.setAttribute(BaseWebConstants.LOGIN_USER_ATTRIBUTE_KEY, loginUser);
            return;
        }

        // 需要登录却未登录
        throw ExceptionUtil.business(SysWebErrorCodeEnums.TOKEN_ERROR_OR_EXPIRE);
    }

    @Nullable
    protected String getUserToken(@NonNull HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    @Nullable
    protected abstract LoginUser parseUserToken(String userToken);
}
