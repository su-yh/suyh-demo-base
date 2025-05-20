package com.suyh.sys.web.authentication.interceptor;

import com.suyh.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.suyh.base.web.exception.ExceptionUtil;
import com.suyh.base.web.util.TokenUtils;
import com.suyh.ruoyi.web.service.SysPermissionService;
import com.suyh.sys.web.authentication.user.LoginUser;
import com.suyh.sys.web.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    private final UserService userService;
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
        String nickname = claims.get(LoginUser.NICK_NAME_KEY, String.class);
        if (username == null || strId == null || nickname == null) {
            log.error("claims value null, username: {}, id: {}, nickname: {}", username, strId, nickname);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }

        long id = Long.parseLong(strId);
        return new LoginUser(userService, permissionService, id, username, nickname);
    }
}
