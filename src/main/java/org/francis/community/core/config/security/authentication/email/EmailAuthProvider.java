package org.francis.community.core.config.security.authentication.email;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.config.security.authentication.LoginSuccessToken;
import org.francis.community.core.enums.AuthErrorEnums;
import org.francis.community.core.constant.LoginConstants;
import org.francis.community.modules.user.model.User;
import org.francis.community.modules.user.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailAuthProvider implements AuthenticationProvider {

    private final UserService userService;

    private final StringRedisTemplate redisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailAuthToken token = (EmailAuthToken) authentication;
        String email = token.getEmail();
        String code = token.getCode();

        if (!StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            throw new BadCredentialsException(AuthErrorEnums.verifyCodeIncorrect.getName());
        }

        User user = userService.findByEmail(email);

        if (Objects.isNull(user)) {
            throw new BadCredentialsException(AuthErrorEnums.accountNotExist.getName());
        } else {
            // 查询redis验证code
            String realCode = redisTemplate.opsForValue().get(LoginConstants.EMAIL_CODE_KEY + email);
            boolean isCodeEq = StringUtils.hasText(realCode) && Objects.equals(realCode, code);
            if (isCodeEq) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", user.getId());
                claims.put("username", user.getUsername());
                String jwtToken = Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS512, "secret").compact();
                // 删除验证码
                redisTemplate.delete(LoginConstants.EMAIL_CODE_KEY + email);
                log.info("用户 id:{},username:{} 通过邮箱验证码方式登录成功", user.getId(), user.getUsername());
                return new LoginSuccessToken(jwtToken, user.getUsername());
            } else {
                throw new BadCredentialsException(AuthErrorEnums.verifyCodeIncorrect.getName());
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthToken.class.isAssignableFrom(authentication);
    }
}
