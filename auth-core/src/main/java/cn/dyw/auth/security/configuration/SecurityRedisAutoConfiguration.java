package cn.dyw.auth.security.configuration;

import cn.dyw.auth.security.AuthProperties;
import cn.dyw.auth.security.TokenAuthenticationProxyFactory;
import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.repository.RedisMapSecurityTokenRepository;
import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.repository.TokenResolve;
import cn.dyw.auth.security.repository.jackson.TokenAuthenticationTokenMixin;
import cn.dyw.auth.security.repository.jackson.TokenWrapper;
import cn.dyw.auth.security.repository.jackson.TokenWrapperMixin;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import java.util.List;

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
    @ConditionalOnProperty(prefix = "app.auth", name = "token-repository", havingValue = "redis")
    public RedisTemplate<String, TokenWrapper> tokenRedisTemplate(RedisConnectionFactory factory) {
        ObjectMapper mapper = new ObjectMapper();

        List<Module> modules = SecurityJackson2Modules.getModules(getClass().getClassLoader());
        mapper.registerModules(modules);
        mapper.addMixIn(TokenWrapper.class, TokenWrapperMixin.class);
        mapper.addMixIn(TokenAuthenticationToken.class, TokenAuthenticationTokenMixin.class);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, Object.class);

        RedisTemplate<String, TokenWrapper> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.auth", name = "token-repository", havingValue = "redis")
    public SecurityTokenRepository redisSecurityTokenRepository(AuthProperties authProperties,
                                                                TokenResolve tokenResolve,
                                                                RedisTemplate<String, TokenWrapper> redisTemplate,
                                                                UserDetailsService userDetailsService,
                                                                @Autowired(required = false) TokenAuthenticationProxyFactory proxyFactory) {
        RedisMapSecurityTokenRepository repository = new RedisMapSecurityTokenRepository(
                redisTemplate,
                tokenResolve,
                authProperties.getExpireTime(),
                authProperties.getRemoveTime(),
                authProperties.getRedisKeyPrefix(),
                userDetailsService);
        repository.setProxyFactory(proxyFactory);
        return repository;
    }

}



