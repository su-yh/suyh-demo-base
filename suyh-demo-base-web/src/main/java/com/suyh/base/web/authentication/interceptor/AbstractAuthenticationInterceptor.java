package com.suyh.base.web.authentication.interceptor;

import com.suyh.base.web.authentication.annotation.Permit;
import com.suyh.base.web.constants.BaseWebConstants;
import com.suyh.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.suyh.base.web.exception.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 处理用户认证拦截器
 */
@Slf4j
public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {
    protected final AntPathMatcher antPathMatcher = new AntPathMatcher();

    // TODO: suyh - 需要添加一个扩展，支持业务添加忽略认证的接口配置
    /**
     * 对于非业务API 接口忽略认证的API 配置
     * 如果是业务相关的API 接口忽略认证使用注解{@link Permit}
     */
    protected final List<String> ignoreAuthPathPatterns = Arrays.asList(
            // spring mvc 基础错误重定向API 接口
            "/error",
            "/**/*.js", "/**/*.css",
            // knife4j
            "/doc.html", "/v3/api-docs/**");

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
        String userToken = getUserToken(request);
        Object loginUser = parseUserToken(userToken);

        // 正常登录
        if (loginUser != null) {
            request.setAttribute(BaseWebConstants.LOGIN_USER_ATTRIBUTE_KEY, loginUser);
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(loginUser, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return;
        }

        // 未登录的情况

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

        throw ExceptionUtil.business(BaseWebErrorCodeEnums.TOKEN_ERROR_OR_EXPIRE);
    }

    @Nullable
    protected String getUserToken(@NonNull HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    @Nullable
    protected abstract Object parseUserToken(String userToken);

//    @Nullable
//    protected LOGIN_USER parseLoginUser(String userToken) {
//        if (!StringUtils.hasText(userToken)) {
//            return null;
//        }
//
//        Claims claims = TokenUtils.parseToken(userToken);
//        if (claims == null) {
//            return null;
//        }
//
//        String username = claims.getSubject();
//        String strId = claims.getId();
//        String nickname = claims.get(TokenUtils.NICK_NAME_KEY, String.class);
//        if (username == null || strId == null || nickname == null) {
//            log.error("claims value null, username: {}, id: {}, nickname: {}", username, strId, nickname);
//            throw ExceptionUtil.business(ErrorCodeConstants.SERVICE_ERROR);
//        }
//
//        if (userService == null) {
//            log.error("{} bean is null", SysUserService.class.getSimpleName());
//            throw ExceptionUtil.business(ErrorCodeConstants.SERVICE_ERROR);
//        }
//
//        long id = Long.parseLong(strId);
//        return new LoginUser(userService, permissionService, id, username, nickname);
//    }
}
