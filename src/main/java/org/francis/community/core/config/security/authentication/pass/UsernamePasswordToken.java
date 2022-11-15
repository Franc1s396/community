package org.francis.community.core.config.security.authentication.pass;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
public class UsernamePasswordToken extends AbstractAuthenticationToken {
    @Getter
    private final String username;
    @Getter
    private final String password;

    public UsernamePasswordToken(Collection<? extends GrantedAuthority> authorities, String username, String password) {
        super(authorities);
        this.username = username;
        this.password = password;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }
}
