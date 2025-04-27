package cn.dyw.auth.support.access;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

/**
 * 配置过滤器
 *
 * @author dyw770
 * @since 2025-04-27
 */
public class SystemAccessConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractHttpConfigurer<SystemAccessConfigurer<H>, H> {

    @Override
    public void configure(H http) {
        SystemAccessFilter systemAccessFilter = new SystemAccessFilter();

        http.addFilterBefore(systemAccessFilter, WebAsyncManagerIntegrationFilter.class);
    }
}
