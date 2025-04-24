package cn.dyw.auth.db.configuration;

import cn.dyw.auth.db.security.JdbcAuthorizationManager;
import cn.dyw.auth.db.service.ISysApiResourceService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

/**
 * @author dyw770
 * @since 2025-04-22
 */
@Configuration
@MapperScan(basePackages = "cn.dyw.auth.db.mapper")
@ComponentScan(basePackages = "cn.dyw.auth.db")
@EnableConfigurationProperties(JdbcAuthProperties.class)
public class AuthJdbcAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "app.auth.jdbc", name = "enable-jdbc-api-auth", havingValue = "true")
    public JdbcAuthorizationManager jdbcAuthorizationManager(ISysApiResourceService apiResourceService,
                                                             ApplicationContext context,
                                                             GrantedAuthorityDefaults grantedAuthorityDefaults) {
        return new JdbcAuthorizationManager(apiResourceService, context, grantedAuthorityDefaults);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("ROLE_");
    }
}
