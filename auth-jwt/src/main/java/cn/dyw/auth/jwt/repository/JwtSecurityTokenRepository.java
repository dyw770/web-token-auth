package cn.dyw.auth.jwt.repository;

import cn.dyw.auth.jwt.security.JwtTokenAuthenticationToken;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-06-17
 */
public interface JwtSecurityTokenRepository {

    /**
     * 保存token
     *
     * @param token 用户token
     */
    void savaToken(JwtTokenAuthenticationToken token, UserLoginDetails details);

    /**
     * 获取token
     *
     * @param token 用户token
     * @return token对象
     */
    JwtTokenAuthenticationToken loadToken(String token);


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
        JwtTokenAuthenticationToken tokenAuthenticationToken = loadToken(token);
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
    List<UserLoginDetails> listUserTokens(String username);
}
