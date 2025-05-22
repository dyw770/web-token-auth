package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * token 存储
 *
 * @author dyw770
 * @since 2025-02-13
 */
public abstract class AbstractSecurityTokenRepository implements SecurityTokenRepository {
    
    @Getter
    private final UserDetailsService userDetailsService;

    public AbstractSecurityTokenRepository(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    public TokenAuthenticationToken loadToken(String token) {
        UserLoginDetails details = internalLoadToken(token);
        if (ObjectUtils.isEmpty(details)) {
            return null;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(details.username());
        return new TokenAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                details,
                userDetails.getAuthorities());
    }
    
    protected abstract UserLoginDetails internalLoadToken(String token);
}
