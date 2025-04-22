package cn.dyw.auth.security.configuration;

import cn.dyw.auth.security.AuthProperties;
import cn.dyw.auth.security.LoginLogoutHandler;
import cn.dyw.auth.security.SecurityExceptionResolverHandler;
import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.repository.*;
import cn.dyw.auth.security.repository.jackson.TokenAuthenticationTokenMixin;
import cn.dyw.auth.security.repository.jackson.TokenWrapper;
import cn.dyw.auth.security.repository.jackson.TokenWrapperMixin;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-02-14
 */
@Configuration
public class SecurityAutoConfiguration {

    @Bean
    public TokenResolve tokenResolve(AuthProperties authProperties) {
        return new RequestTokenResolve(authProperties.getAuthHeaderName());
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public LoginLogoutHandler loginLogoutHandler(AuthenticationManager authenticationManager,
                                                 SecurityTokenRepository tokenRepository,
                                                 TokenResolve tokenResolve) {
        return new LoginLogoutHandler(authenticationManager, tokenRepository, tokenResolve);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityExceptionResolverHandler securityExceptionResolverHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return new SecurityExceptionResolverHandler(resolver);
    }

    @Bean
    public AuthProperties authProperties() {
        return new AuthProperties();
    }

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
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
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnProperty(prefix = "app.auth", name = "token-repository", havingValue = "redis")
    public SecurityTokenRepository redisSecurityTokenRepository(AuthProperties authProperties,
                                                                TokenResolve tokenResolve,
                                                                RedisTemplate<String, TokenWrapper> redisTemplate) {
        return new RedisMapSecurityTokenRepository(
                redisTemplate,
                tokenResolve,
                authProperties.getExpireTime(),
                authProperties.getRemoveTime(),
                authProperties.getRedisKeyPrefix());
    }

    @Bean
    @ConditionalOnMissingBean(SecurityTokenRepository.class)
    public SecurityTokenRepository localSecurityTokenRepository(AuthProperties authProperties) {
        return new LocalMapSecurityTokenRepository(authProperties.getExpireTime(), authProperties.getRemoveTime());
    }

    @Slf4j
    @Configuration
    @EnableScheduling
    @ConditionalOnProperty(prefix = "app.auth", name = "token-repository", havingValue = "local", matchIfMissing = true)
    public static class LocalTokenCleaners {

        private final SecurityTokenRepository tokenRepository;

        public LocalTokenCleaners(SecurityTokenRepository tokenRepository) {
            this.tokenRepository = tokenRepository;
        }

        //每3秒执行一次
        @Scheduled(cron = "0 */5 * * * *")
        public void clearToken() {
            log.info("清理过期token");
            this.tokenRepository.removeExpireToken();
        }
    }

}



