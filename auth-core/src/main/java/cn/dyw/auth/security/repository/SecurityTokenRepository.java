package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.TokenAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * token 存储
 *
 * @author dyw770
 * @since 2025-02-13
 */
public interface SecurityTokenRepository {

    /**
     * 保存token
     *
     * @param token 用户token
     */
    void savaToken(TokenAuthenticationToken token);

    /**
     * 获取token
     *
     * @param token 用户token
     * @return token对象
     */
    TokenAuthenticationToken loadToken(String token);


    /**
     * 获取securityContext
     *
     * @param token 用户token
     * @return securityContext对象
     */
    default SecurityContext loadContext(String token) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        if (!StringUtils.hasText(token)) {
            return context;
        }
        TokenAuthenticationToken tokenAuthenticationToken = loadToken(token);
        context.setAuthentication(tokenAuthenticationToken);

        return context;
    }

    /**
     * 删除token
     *
     * @param token 用户token
     */
    void removeToken(String token);

    /**
     * 判断token是否存在
     *
     * @param token 用户token
     * @return true/false
     */
    boolean containsToken(String token);

    /**
     * 判断token是否过期
     *
     * @param token 用户token
     * @return 结果
     */
    boolean isExpired(String token);

    /**
     * 更新token过期时间
     *
     * @param token      用户token
     * @param expireTime 过期时间
     */
    void updateExpireTime(String token, long expireTime);


    /**
     * 更新过期时间为默认时间
     *
     * @param token token
     */
    void updateExpireTime(String token);

    /**
     * 过期token
     *
     * @param token token
     */
    void expireToken(String token);

    /**
     * 移出所有过期token
     */
    void removeExpireToken();

    /**
     * 用户token数量
     *
     * @return 数量
     */
    int userTokens(String username);

    /**
     * 用户token数量
     *
     * @return 数量
     */
    List<TokenWrapper> listUserTokens(String username);
}
