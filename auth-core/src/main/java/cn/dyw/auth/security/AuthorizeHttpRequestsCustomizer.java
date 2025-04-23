package cn.dyw.auth.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * 自定义修改权限配置
 *
 * @author dyw770
 * @since 2025-04-23
 */
@FunctionalInterface
public interface AuthorizeHttpRequestsCustomizer {

    /**
     * 自定配置
     *
     * @param authorize 配置
     */
    void consume(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize);
}
