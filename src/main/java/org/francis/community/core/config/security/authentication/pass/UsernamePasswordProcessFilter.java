package org.francis.community.core.config.security.authentication.pass;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.enums.AuthEnums;
import org.francis.community.core.enums.AuthErrorEnums;
import org.francis.community.core.config.security.handler.CustomAuthenticationFailureHandler;
import org.francis.community.core.config.security.handler.CustomAuthenticationSuccessHandler;
import org.francis.community.core.exception.security.MethodNotSupportException;
import org.francis.community.core.model.request.PasswordLoginRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
@Slf4j
public class UsernamePasswordProcessFilter extends AbstractAuthenticationProcessingFilter {
private final CustomAuthenticationSuccessHandler successHandler;
private final CustomAuthenticationFailureHandler failureHandler;

    public UsernamePasswordProcessFilter(AuthenticationManager authenticationManager,
                                           CustomAuthenticationFailureHandler failureHandler,
                                           CustomAuthenticationSuccessHandler successHandler) {
        super(AuthEnums.PASS.getUrl(),authenticationManager);
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        checkMethod(request);
        ObjectMapper mapper = new ObjectMapper();
        PasswordLoginRequest loginRequest = mapper.readValue(request.getReader(),
                PasswordLoginRequest.class);
        UsernamePasswordToken token = new UsernamePasswordToken(null, loginRequest.getUsername(), loginRequest.getPassword());
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private void checkMethod(HttpServletRequest request){
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new MethodNotSupportException(AuthErrorEnums.methodNotSupport.getName());
        }
    }
}
