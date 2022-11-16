package org.francis.community.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.francis.community.core.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 解析token获取登录用户信息
     *
     * @param bearerToken token
     * @return 登录用户信息
     */
    public static LoginUser getLoginUser(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            String token = getToken(bearerToken);
            Claims claims = null;
            try {
                claims = parseToken(token);
                Long userId = claims.get("userId", Long.class);
                String username = claims.get("username", String.class);
                LoginUser loginUser = new LoginUser();
                loginUser.setUserId(userId);
                loginUser.setUsername(username);
                return loginUser;
            } catch (RuntimeException e) {
                log.error("token解析异常,bearerToken:{}", bearerToken);
            }
        }
        return null;
    }

    /**
     * 获取信息集合
     *
     * @param token token
     * @return 信息集合
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取token
     *
     * @param bearerToken bearerToken
     * @return token
     */
    public static String getToken(String bearerToken) {
        return bearerToken.replace(TOKEN_PREFIX, "");
    }

    /**
     * 生成token
     * @param claims 信息map
     * @return token
     */
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "secret").compact();
    }
}
