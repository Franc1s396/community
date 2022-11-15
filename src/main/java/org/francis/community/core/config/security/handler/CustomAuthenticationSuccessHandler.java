package org.francis.community.core.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.francis.community.core.config.security.authentication.LoginSuccessResponse;
import org.francis.community.core.config.security.authentication.LoginSuccessToken;
import org.francis.community.core.model.AjaxResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        LoginSuccessToken token = (LoginSuccessToken) authentication;
        LoginSuccessResponse res = new LoginSuccessResponse();
        res.setToken(token.getPrincipal().toString());
        res.setUsername(token.getUsername());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getOutputStream().write(objectMapper.writeValueAsBytes(AjaxResult.success(res)));
    }
}
