package cn.dyw.auth.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dyw770
 * @since 2025-02-20
 */
@Data
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {

    /**
     * token 有效期
     */
    private long expireTime = 60 * 30;

    /**
     * token 请求头名字
     */
    private String authHeaderName = "Authorization";

    /**
     * token redis key 前缀
     */
    private String redisKeyPrefix = "auth:token:";

    /**
     * url前缀
     */
    private String apiContextPath = "/admin";

    /**
     * token 存储方式
     */
    private TokenRepositoryType tokenRepository = TokenRepositoryType.local;

    
    public enum TokenRepositoryType {
        /**
         * 本地存储
         */
        local,
        /**
         * redis 存储
         */
        redis
    }
}
