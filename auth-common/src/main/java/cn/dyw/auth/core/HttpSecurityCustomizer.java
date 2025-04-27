package cn.dyw.auth.core;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * HttpSecurity customizer
 *
 * @author dyw770
 * @since 2025-04-27
 */
@FunctionalInterface
public interface HttpSecurityCustomizer {

    void customize(HttpSecurity http) throws Exception;

}
