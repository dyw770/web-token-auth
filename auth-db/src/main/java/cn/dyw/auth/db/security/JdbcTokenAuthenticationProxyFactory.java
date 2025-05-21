package cn.dyw.auth.db.security;

import cn.dyw.auth.db.service.ISysUserService;
import cn.dyw.auth.security.TokenAuthenticationProxyFactory;
import cn.dyw.auth.security.TokenAuthenticationToken;
import org.apache.commons.lang3.ObjectUtils;

/**
 * token  工厂
 * 
 * @author dyw770
 * @since 2025-05-21
 */
public class JdbcTokenAuthenticationProxyFactory implements TokenAuthenticationProxyFactory {
    
    private final ISysUserService userService;

    public JdbcTokenAuthenticationProxyFactory(ISysUserService userService) {
        this.userService = userService;
    }

    @Override
    public TokenAuthenticationToken proxy(TokenAuthenticationToken token) {
        if (ObjectUtils.isEmpty(token)) {
            return null;
        }
        return new JdbcTokenAuthenticationTokenProxy(token, userService);
    }
}
