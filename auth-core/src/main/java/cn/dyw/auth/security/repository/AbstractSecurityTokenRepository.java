package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.TokenAuthenticationProxyFactory;
import cn.dyw.auth.security.TokenAuthenticationToken;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * token 存储
 *
 * @author dyw770
 * @since 2025-02-13
 */
public abstract class AbstractSecurityTokenRepository implements SecurityTokenRepository {
    
    @Getter
    @Setter
    private TokenAuthenticationProxyFactory proxyFactory;
    

    @Override
    public TokenAuthenticationToken loadToken(String token) {
        if (ObjectUtils.isEmpty(proxyFactory)) {
            return internalLoadToken(token);
        }
        return proxyFactory.proxy(internalLoadToken(token));
    }
    
    protected abstract TokenAuthenticationToken internalLoadToken(String token);
}
