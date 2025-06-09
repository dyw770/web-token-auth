package cn.dyw.auth.security.configuration;

import cn.dyw.auth.security.AuthProperties;
import cn.dyw.auth.security.repository.RedisSecurityTokenRepository;
import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.repository.TokenResolve;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import cn.dyw.auth.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * redis token存储配置
 *
 * @author dyw770
 * @since 2025-02-14
 */
@Slf4j
@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class SecurityRedisAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "app.auth", name = "token-repository", havingValue = "redis")
    public RedisTemplate<String, UserLoginDetails> tokenRedisTemplate(RedisConnectionFactory factory) {
        // 创建 ObjectMapper
        ObjectMapper objectMapper = ObjectMapperUtils.createRedisObjectMapper();
        
        Jackson2JsonRedisSerializer<UserLoginDetails> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, UserLoginDetails.class);

        RedisTemplate<String, UserLoginDetails> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.auth", name = "token-repository", havingValue = "redis")
    public SecurityTokenRepository redisSecurityTokenRepository(AuthProperties authProperties,
                                                                TokenResolve tokenResolve,
                                                                RedisTemplate<String, UserLoginDetails> redisTemplate,
                                                                UserDetailsService userDetailsService) {
        return new RedisSecurityTokenRepository(
                redisTemplate,
                tokenResolve,
                authProperties.getExpireTime(),
                authProperties.getRedisKeyPrefix(),
                userDetailsService);
    }

}



