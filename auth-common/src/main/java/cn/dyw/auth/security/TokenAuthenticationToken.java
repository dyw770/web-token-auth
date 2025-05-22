package cn.dyw.auth.security;

import cn.dyw.auth.security.serializable.UserLoginDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author dyw770
 * @since 2025-02-13
 */
public class TokenAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private final UserLoginDetails loginDetails;

    public TokenAuthenticationToken(Object principal,
                                    Object credentials,
                                    UserLoginDetails loginDetails,
                                    Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.loginDetails = loginDetails;
    }


    @Override
    public UserLoginDetails getDetails() {
        return loginDetails;
    }

    public String getToken() {
        return loginDetails.token();
    }
}
