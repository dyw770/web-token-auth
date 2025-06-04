package cn.dyw.auth.cache.configuration;

import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author dyw770
 * @since 2025-06-04
 */
@Data
@ConfigurationProperties(prefix = "app.auth.cache")
public class AuthCacheProperties {

    /**
     * 缓存前缀
     */
    private String cachePrefix = "auth:cache:";

    /**
     * 默认缓存过期时间
     */
    private Duration expireTime = Duration.ofMinutes(30);

    /**
     * 使用自定义redis
     */
    private Boolean useCustomRedis = false;

    /**
     * redis配置
     */
    private RedisProperties redis = new RedisProperties();
}
