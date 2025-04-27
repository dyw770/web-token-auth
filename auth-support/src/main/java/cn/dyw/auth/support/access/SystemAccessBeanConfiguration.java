package cn.dyw.auth.support.access;

import cn.dyw.auth.core.HttpSecurityCustomizer;
import cn.dyw.auth.support.SystemAccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dyw770
 * @since 2025-04-27
 */
@Configuration
public class SystemAccessBeanConfiguration {

    @Bean
    public HttpSecurityCustomizer systemAccessCustomizer(@Autowired(required = false) SystemAccessHandler systemAccessHandler) {
        return http -> http.with(new SystemAccessConfigurer<>(), configurer -> {
            if (systemAccessHandler != null) {
                configurer.setSystemAccessHandler(systemAccessHandler);
            }
        });
    }

}
