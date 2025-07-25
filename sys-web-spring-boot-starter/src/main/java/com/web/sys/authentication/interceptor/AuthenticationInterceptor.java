package com.web.sys.authentication.interceptor;

import com.base.web.exception.ExceptionUtil;
import com.base.web.util.TokenUtils;
import com.web.ruoyi.mybatis.entity.SysUser;
import com.web.ruoyi.service.SysPermissionService;
import com.web.sys.authentication.user.LoginUser;
import com.web.sys.constants.enums.SysWebErrorCodeEnums;
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
    protected LoginUser parseUserToken(String userToken) {
        if (!StringUtils.hasText(userToken)) {
            return null;
        }

        Claims claims = TokenUtils.parseToken(userService.getBase64EncodedSecretKey(), userToken);
        if (claims == null) {
            return null;
        }

        String username = claims.getSubject();
        String tokenIdStr = claims.getId();
        Long userId = claims.get(TokenUtils.USER_ID_KEY, Long.class);
        if (username == null || userId == null || tokenIdStr == null) {
            log.error("invalid token, claims value null, username: {}, userId: {}, tokenId: {}", username, userId, tokenIdStr);
            throw ExceptionUtil.business(SysWebErrorCodeEnums.TOKEN_ERROR_OR_EXPIRE);
        }

        SysUser sysUser = userService.obtainUserById(userId);
        if (sysUser == null) {
            log.warn("parse user token, user not exists, by id: {}", userId);
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_NOT_EXISTS);
        }

        Integer tokenId = sysUser.getTokenId();
        if (tokenId == null || tokenId < 0 || !tokenIdStr.equals(tokenId + "")) {
            log.warn("tokenId(SysUser): {}, tokenId(JWT): {}", tokenId, tokenIdStr);
            throw ExceptionUtil.business(SysWebErrorCodeEnums.TOKEN_ERROR_OR_EXPIRE);
        }

        return new LoginUser(sysUser, permissionService);
    }
}
