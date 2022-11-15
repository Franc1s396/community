package org.francis.community.core.config.security.authentication;

import org.francis.community.core.model.LoginUser;
import org.francis.community.core.utils.JwtUtils;
import org.francis.community.core.utils.SecurityUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String HEADER_AUTHORIZATION = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);
        // 解析token
        LoginUser loginUser = JwtUtils.getLoginUser(bearerToken);
        boolean isAuthenticate = Objects.nonNull(SecurityUtils.getAuthentication());
        // 如果不为空
        if (Objects.nonNull(loginUser) && !isAuthenticate) {
            // 设置authentication

        }
        filterChain.doFilter(request, response);
    }
}
