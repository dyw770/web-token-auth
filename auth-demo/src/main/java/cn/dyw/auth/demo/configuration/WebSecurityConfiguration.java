package cn.dyw.auth.demo.configuration;

import cn.dyw.auth.core.AuthorizeHttpRequestsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@Configuration
public class WebSecurityConfiguration {

    @Bean
    public AuthorizeHttpRequestsCustomizer authorizeHttpRequestsCustomizer() throws Exception {
        return (authorize) -> authorize
                .requestMatchers("/user/login").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/doc/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(PathPatternParser pathPatternParser, UrlPathHelper urlPathHelper) {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(pathPatternParser);
        source.setUrlPathHelper(urlPathHelper);
        
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
