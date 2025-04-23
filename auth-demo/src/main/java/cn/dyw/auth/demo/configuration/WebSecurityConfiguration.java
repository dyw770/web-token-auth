package cn.dyw.auth.demo.configuration;

import cn.dyw.auth.security.AuthorizeHttpRequestsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@Configuration
public class WebSecurityConfiguration {

    @Bean
    public AuthorizeHttpRequestsCustomizer authorizeHttpRequestsCustomizer() throws Exception {
        return (authorize) -> authorize
                .requestMatchers("/login").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/doc/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll();
    }


}
