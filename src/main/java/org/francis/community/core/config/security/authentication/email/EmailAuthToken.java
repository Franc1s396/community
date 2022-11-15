package org.francis.community.core.config.security.authentication.email;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
public class EmailAuthToken extends AbstractAuthenticationToken {

    @Getter
    private final String email;

    @Getter
    private final String code;

    public EmailAuthToken(Collection<? extends GrantedAuthority> authorities, String email, String code) {
        super(authorities);
        this.email = email;
        this.code = code;
    }

    @Override
    public Object getCredentials() {
        return email;
    }

    @Override
    public Object getPrincipal() {
        return code;
    }
}
