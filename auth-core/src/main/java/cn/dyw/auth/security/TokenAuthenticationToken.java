package cn.dyw.auth.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author dyw770
 * @since 2025-02-13
 */
@Getter
public class TokenAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String token;

    private final LocalDateTime createTime;

    private final String loginUserAgent;

    public TokenAuthenticationToken(Object principal, 
                                    Object credentials,
                                    String token, 
                                    String loginUserAgent) {
        super(principal, credentials);
        this.token = token;
        this.loginUserAgent = loginUserAgent;
        this.createTime = LocalDateTime.now();
    }

    public TokenAuthenticationToken(Object principal,
                                    Object credentials,
                                    String token,
                                    String loginUserAgent,
                                    Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.token = token;
        this.loginUserAgent = loginUserAgent;
        this.createTime = LocalDateTime.now();
    }
}
