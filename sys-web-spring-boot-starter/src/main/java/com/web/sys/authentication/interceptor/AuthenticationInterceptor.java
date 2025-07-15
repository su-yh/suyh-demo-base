package com.web.sys.authentication.interceptor;

import com.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.base.web.exception.ExceptionUtil;
import com.base.web.util.TokenUtils;
import com.web.ruoyi.service.SysPermissionService;
import com.web.sys.authentication.user.LoginUser;
import com.web.sys.service.IUserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    private final IUserService userService;
    private final SysPermissionService permissionService;

    @Override
    protected Object parseUserToken(String userToken) {
        if (!StringUtils.hasText(userToken)) {
            return null;
        }

        Claims claims = TokenUtils.parseToken(userService.getBase64EncodedSecretKey(), userToken);
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
