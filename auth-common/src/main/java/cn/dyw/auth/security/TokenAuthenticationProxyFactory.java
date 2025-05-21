package cn.dyw.auth.security;

/**
 * 提供一个可以修改TokenAuthentication的接口
 *
 * @author dyw770
 * @since 2025-05-21
 */
public interface TokenAuthenticationProxyFactory {

    /**
     * 返回被代理的 token
     *
     * @param token token
     * @return proxy token
     */
    TokenAuthenticationToken proxy(TokenAuthenticationToken token);
}
