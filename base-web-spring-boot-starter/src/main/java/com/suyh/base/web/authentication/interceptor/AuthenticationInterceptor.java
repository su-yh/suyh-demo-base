package com.suyh.base.web.authentication.interceptor;

import com.suyh.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.suyh.base.web.exception.ExceptionUtil;
import com.suyh.base.web.user.LoginUser;
import com.suyh.base.web.util.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor extends AbstractAuthenticationInterceptor {
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
        LoginUser loginUser = new LoginUser();
        loginUser.setId(id);
        loginUser.setUsername(username);
        loginUser.setNickname(nickname);
        return loginUser;
    }
}
