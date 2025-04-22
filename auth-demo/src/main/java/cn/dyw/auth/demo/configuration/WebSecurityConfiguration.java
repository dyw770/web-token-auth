package cn.dyw.auth.demo.configuration;

import cn.dyw.auth.security.SecurityExceptionResolverHandler;
import cn.dyw.auth.security.filter.SecurityTokenContextConfigurer;
import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.repository.TokenResolve;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationManager authenticationManager,
                                           SecurityTokenRepository securityTokenRepository,
                                           TokenResolve tokenResolve,
                                           SecurityExceptionResolverHandler exceptionResolverHandler) throws Exception {
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
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/doc/**").permitAll()
                                .requestMatchers("/favicon.ico").permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }


}
