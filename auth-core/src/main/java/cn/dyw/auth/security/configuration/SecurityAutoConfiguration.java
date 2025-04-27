package cn.dyw.auth.security.configuration;

import cn.dyw.auth.core.AuthorizeHttpRequestsCustomizer;
import cn.dyw.auth.core.HttpSecurityCustomizer;
import cn.dyw.auth.security.AuthProperties;
import cn.dyw.auth.security.LoginLogoutHandler;
import cn.dyw.auth.security.SecurityExceptionResolverHandler;
import cn.dyw.auth.security.filter.SecurityTokenContextConfigurer;
import cn.dyw.auth.security.repository.LocalMapSecurityTokenRepository;
import cn.dyw.auth.security.repository.RequestTokenResolve;
import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.repository.TokenResolve;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

/**
 * spring security 自动配置
 *
 * @author dyw770
 * @since 2025-02-14
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityAutoConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationManager authenticationManager,
                                           SecurityTokenRepository securityTokenRepository,
                                           TokenResolve tokenResolve,
                                           SecurityExceptionResolverHandler exceptionResolverHandler,
                                           AuthorizeHttpRequestsCustomizer customizer,
                                           List<HttpSecurityCustomizer> httpSecurityCustomizers,
                                           @Autowired(required = false) AuthorizationManager<RequestAuthorizationContext> authorizationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .securityContext(AbstractHttpConfigurer::disable)
                .anonymous(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .requestCache(AbstractHttpConfigurer::disable)
                .with(
                        new SecurityTokenContextConfigurer<>(),
                        configurer ->
                                configurer.securityContextRepository(securityTokenRepository)
                                        .tokenResolve(tokenResolve)
                )
                .exceptionHandling(
                        configurer ->
                                configurer.accessDeniedHandler(exceptionResolverHandler)
                                        .authenticationEntryPoint(exceptionResolverHandler)
                )
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(authorize -> {
                            customizer.consume(authorize);
                            if (ObjectUtils.isEmpty(authorizationManager)) {
                                authorize.anyRequest().authenticated();
                            } else {
                                authorize.anyRequest().access(authorizationManager);
                            }

                        }
                );
        
        for (HttpSecurityCustomizer httpSecurityCustomizer : httpSecurityCustomizers) {
            httpSecurityCustomizer.customize(http);
        }

        return http.build();
    }

    @Bean
    public TokenResolve tokenResolve(AuthProperties authProperties) {
        return new RequestTokenResolve(authProperties.getAuthHeaderName());
    }

    @Bean
    @SuppressWarnings("deprecation")
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
    @ConditionalOnMissingBean(SecurityTokenRepository.class)
    @ConditionalOnProperty(prefix = "app.auth", name = "token-repository", havingValue = "local", matchIfMissing = true)
    public SecurityTokenRepository localSecurityTokenRepository(AuthProperties authProperties) {
        log.info("默认使用本地内存存储token");
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

        // 每5分钟执行一次
        @Scheduled(cron = "0 */5 * * * *")
        public void clearToken() {
            log.info("清理过期token");
            this.tokenRepository.removeExpireToken();
        }
    }

}



