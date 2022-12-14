package org.francis.community.core.exception.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
public class MethodNotSupportException extends AuthenticationException {
    public MethodNotSupportException(String msg) {
        super(msg);
    }
}
