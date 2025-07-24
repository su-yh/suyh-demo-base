package com.base.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Map;

/**
 * @author suyh
 * @since 2024-08-28
 */
@Slf4j
public final class TokenUtils {
    public static final String USER_ID_KEY = "userId";

    public static String createToken(
            String base64EncodedSecretKey,
            Map<String, Object> claims, int tokenId, String username, Integer tokenSeconds) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + tokenSeconds * 1000L);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setId(tokenId + "")
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, base64EncodedSecretKey).compact();
    }

    @Nullable
    public static Claims parseToken(String base64EncodedSecretKey, String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(base64EncodedSecretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception exception) {
            log.warn("token parse failed. token: {}, message: {}", token, exception.getMessage());
            return null;
        }
    }
}
