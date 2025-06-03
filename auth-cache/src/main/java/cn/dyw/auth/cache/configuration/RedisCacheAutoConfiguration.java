package cn.dyw.auth.cache.configuration;

import cn.dyw.auth.cache.CacheNames;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Collections;

/**
 * @author dyw770
 * @since 2025-06-03
 */
@EnableCaching
@Configuration
public class RedisCacheAutoConfiguration {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration defaultConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .prefixCacheNameWith(":");

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfiguration)
                .transactionAware()
                .withInitialCacheConfigurations(Collections.singletonMap(CacheNames.ROLE_CACHE,
                        RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()))
                .build();
    }
}
