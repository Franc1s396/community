package org.francis.community.core.config.security.authentication.pass;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.config.security.authentication.LoginSuccessToken;
import org.francis.community.core.config.security.enums.AuthErrorEnums;
import org.francis.community.core.constant.LoginConstants;
import org.francis.community.modules.user.model.User;
import org.francis.community.modules.user.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UsernamePasswordProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authentication;
        if (!StringUtils.hasText(token.getUsername()) || !StringUtils.hasText(token.getPassword())) {
            throw new BadCredentialsException(AuthErrorEnums.passwordIncorrect.getName());
        }
        User user = userService.findByUsername(token.getUsername());
        if (Objects.isNull(user)) {
            throw new BadCredentialsException(AuthErrorEnums.passwordIncorrect.getName());
        } else {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            boolean matches = bCryptPasswordEncoder.matches(token.getPassword(),
                    user.getPassword());
            if (matches) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", user.getId());
                claims.put("username",user.getUsername());
                String jwtToken = Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS512, "secret").compact();
                return new LoginSuccessToken(jwtToken, user.getUsername());
            } else {
                throw new BadCredentialsException(AuthErrorEnums.passwordIncorrect.getName());
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordToken.class.isAssignableFrom(authentication);
    }
}
