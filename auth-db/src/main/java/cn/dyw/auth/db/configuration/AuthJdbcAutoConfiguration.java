package cn.dyw.auth.db.configuration;

import cn.dyw.auth.db.event.AuthChangedApplicationListener;
import cn.dyw.auth.db.security.JdbcAuthorizationManager;
import cn.dyw.auth.db.security.JdbcTokenAuthenticationProxyFactory;
import cn.dyw.auth.db.security.JdbcUserDetailsService;
import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.db.service.ISysRoleService;
import cn.dyw.auth.db.service.ISysUserService;
import cn.dyw.auth.security.TokenAuthenticationProxyFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author dyw770
 * @since 2025-04-22
 */
@Configuration
@MapperScan(basePackages = "cn.dyw.auth.db.mapper")
@ComponentScan(basePackages = "cn.dyw.auth.db")
@EnableConfigurationProperties(JdbcAuthProperties.class)
@ConditionalOnProperty(prefix = "app.auth.jdbc", name = "enable-jdbc-api-auth", havingValue = "true")
public class AuthJdbcAutoConfiguration {

    @Bean
    public JdbcAuthorizationManager jdbcAuthorizationManager(ISysApiResourceService apiResourceService,
                                                             ApplicationContext context,
                                                             GrantedAuthorityDefaults grantedAuthorityDefaults,
                                                             ISysRoleService roleService) {
        return new JdbcAuthorizationManager(apiResourceService, context, grantedAuthorityDefaults, roleService);
    }

    @Bean
    public AuthChangedApplicationListener authChangedApplicationListener(JdbcAuthorizationManager authorizationManager) {
        return new AuthChangedApplicationListener(authorizationManager);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(JdbcAuthProperties jdbcAuthProperties) {
        return new GrantedAuthorityDefaults(jdbcAuthProperties.getRolePrefix());
    }

    @Bean
    public UserDetailsService userDetailsService(ISysUserService userService, GrantedAuthorityDefaults grantedAuthorityDefaults) {
        return new JdbcUserDetailsService(userService, grantedAuthorityDefaults);
    }
    
    @Bean
    public TokenAuthenticationProxyFactory jdbcTokenAuthenticationProxyFactory(ISysUserService userService) {
        return new JdbcTokenAuthenticationProxyFactory(userService);
    }
}
