package cn.dyw.auth.cache.configuration;

import cn.dyw.auth.cache.CacheNames;
import cn.dyw.auth.utils.ObjectMapperUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.Collections;

/**
 * @author dyw770
 * @since 2025-06-03
 */
@EnableCaching
@Configuration
@EnableConfigurationProperties(AuthCacheProperties.class)
@ConditionalOnProperty(name = "app.auth.cache.use-custom-redis", havingValue = "false", matchIfMissing = true)
public class RedisCacheAutoConfiguration {


    private final AuthCacheProperties cacheProperties;

    public RedisCacheAutoConfiguration(AuthCacheProperties properties) {
        this.cacheProperties = properties;
    }


    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheWriter cacheWriter = RedisCacheWriter
                .nonLockingRedisCacheWriter(connectionFactory, BatchStrategies.scan(1000));
        
        return RedisCacheManager.builder(cacheWriter)
                .cacheDefaults(defaultConfiguration(cacheProperties))
                .transactionAware()
                .initialCacheNames(Collections.singleton(CacheNames.ROLE_CACHE))
                .build();
    }

    public static RedisCacheConfiguration defaultConfiguration(AuthCacheProperties cacheProperties) {

        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(ObjectMapperUtils.createRedisObjectMapper()))
                )
                .entryTtl(cacheProperties.getExpireTime())
                .enableTimeToIdle()
                .computePrefixWith(cacheName -> cacheProperties.getCachePrefix() + cacheName + ":");
    }
}
