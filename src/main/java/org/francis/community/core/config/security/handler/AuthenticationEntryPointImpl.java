package org.francis.community.core.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.francis.community.core.model.AjaxResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author francis
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        int code = HttpStatus.UNAUTHORIZED.value();
        String msg = "请求访问：{"+request.getRequestURI()+"}，认证失败，无法访问系统资源";
        new ObjectMapper().writeValue(response.getWriter(), AjaxResult.error(code, msg));
    }
}
