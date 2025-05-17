package com.suyh.sys.web.interceptor;

import com.suyh.base.web.authentication.interceptor.AbstractAuthenticationInterceptor;
import com.suyh.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.suyh.base.web.exception.ExceptionUtil;
import com.suyh.sys.web.component.SysPermissionService;
import com.suyh.sys.web.service.SysUserService;
import com.suyh.sys.web.user.LoginUser;
import com.suyh.sys.web.util.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    private final SysUserService userService;
    private final SysPermissionService permissionService;

    @Override
    protected Object parseUserToken(String userToken) {
        if (!StringUtils.hasText(userToken)) {
            return null;
        }

        Claims claims = TokenUtils.parseToken(userToken);
        if (claims == null) {
            return null;
        }

        String username = claims.getSubject();
        String strId = claims.getId();
        String nickname = claims.get(TokenUtils.NICK_NAME_KEY, String.class);
        if (username == null || strId == null || nickname == null) {
            log.error("claims value null, username: {}, id: {}, nickname: {}", username, strId, nickname);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }

        if (userService == null) {
            log.error("{} bean is null", SysUserService.class.getSimpleName());
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }

        long id = Long.parseLong(strId);
        return new LoginUser(userService, permissionService, id, username, nickname);
    }
}
