package org.francis.community.core.config.security.authentication.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.francis.community.core.config.security.authentication.pass.UsernamePasswordToken;
import org.francis.community.core.config.security.enums.AuthEnums;
import org.francis.community.core.config.security.enums.AuthErrorEnums;
import org.francis.community.core.config.security.handler.CustomAuthenticationFailureHandler;
import org.francis.community.core.config.security.handler.CustomAuthenticationSuccessHandler;
import org.francis.community.core.exception.security.MethodNotSupportException;
import org.francis.community.core.model.request.EmailLoginRequest;
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
public class EmailAuthProcessFilter extends AbstractAuthenticationProcessingFilter {
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    public EmailAuthProcessFilter(AuthenticationManager authenticationManager,
                                         CustomAuthenticationFailureHandler failureHandler,
                                         CustomAuthenticationSuccessHandler successHandler) {
        super(AuthEnums.EMAIL.getUrl(),authenticationManager);
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        checkMethod(request);
        ObjectMapper mapper = new ObjectMapper();
        EmailLoginRequest loginRequest = mapper.readValue(request.getReader(),
                EmailLoginRequest.class);
        EmailAuthToken token = new EmailAuthToken(null, loginRequest.getEmail(), loginRequest.getCode());
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
