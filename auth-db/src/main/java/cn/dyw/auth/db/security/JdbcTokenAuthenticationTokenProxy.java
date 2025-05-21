package cn.dyw.auth.db.security;

import cn.dyw.auth.db.service.ISysUserService;
import cn.dyw.auth.security.TokenAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author dyw770
 * @since 2025-05-21
 */
public class JdbcTokenAuthenticationTokenProxy extends TokenAuthenticationToken {

    private final TokenAuthenticationToken target;

    private final ISysUserService userService;

    public JdbcTokenAuthenticationTokenProxy(TokenAuthenticationToken target, ISysUserService userService) {
        super(target.getPrincipal(),
                target.getCredentials(),
                target.getToken(),
                target.getLoginUserAgent(),
                target.getAuthorities());
        this.target = target;
        this.userService = userService;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return userService.userAuthority(this.target.getName());
    }
}
