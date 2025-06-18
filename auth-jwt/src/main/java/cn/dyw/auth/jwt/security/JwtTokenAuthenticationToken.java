package cn.dyw.auth.jwt.security;

import cn.dyw.auth.jwt.token.JwtToken;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author dyw770
 * @since 2025-06-18
 */
public class JwtTokenAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @Getter
    private final JwtToken jwtToken;

    public JwtTokenAuthenticationToken(Object principal, Object credentials, JwtToken jwtToken) {
        super(principal, credentials);
        this.jwtToken = jwtToken;
    }

    public JwtTokenAuthenticationToken(Object principal, Object credentials, JwtToken jwtToken, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.jwtToken = jwtToken;
    }
}
