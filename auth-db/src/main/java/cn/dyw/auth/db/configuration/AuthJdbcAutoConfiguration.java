package cn.dyw.auth.db.configuration;

import cn.dyw.auth.db.event.AuthChangedApplicationListener;
import cn.dyw.auth.db.event.DefaultAuthChangedHandler;
import cn.dyw.auth.db.security.*;
import cn.dyw.auth.db.service.ICachedRoleService;
import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.db.service.ISysUserService;
import cn.dyw.auth.event.AuthChangedHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

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
                                                             List<AuthorizationManagerFactory> authorizationManagerFactories,
                                                             JdbcAuthProperties jdbcAuthProperties) {
        return new JdbcAuthorizationManager(apiResourceService, context, authorizationManagerFactories, jdbcAuthProperties);
    }

    @Bean
    @ConditionalOnMissingClass("cn.dyw.auth.sync.RedisAuthChangedApplicationListener")
    public AuthChangedApplicationListener authChangedApplicationListener(DefaultAuthChangedHandler handler) {
        return new AuthChangedApplicationListener(handler);
    }

    @Bean
    public AuthChangedHandler defaultAuthChangedHandler(JdbcAuthorizationManager authorizationManager) {
        return new DefaultAuthChangedHandler(authorizationManager);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(JdbcAuthProperties jdbcAuthProperties) {
        return new GrantedAuthorityDefaults(jdbcAuthProperties.getRolePrefix());
    }

    @Bean
    public UserDetailsService userDetailsService(ISysUserService userService,
                                                 GrantedAuthorityDefaults grantedAuthorityDefaults) {
        return new JdbcUserDetailsService(userService, grantedAuthorityDefaults);
    }

    @Bean
    public RoleAuthorizationManagerFactory roleAuthorizationManagerFactory(GrantedAuthorityDefaults grantedAuthorityDefaults, ICachedRoleService roleService) {
        return new RoleAuthorizationManagerFactory(grantedAuthorityDefaults, roleService);
    }

    @Bean
    public IpAddressAuthorizationManager.IpAddressAuthorizationManagerFactory ipAddressAuthorizationManagerFactory() {
        return new IpAddressAuthorizationManager.IpAddressAuthorizationManagerFactory();
    }

    @Bean
    public MenuAuthorizationManager.MenuAuthorizationManagerFactory menuAuthorizationManagerFactory(ISysUserService userService) {
        return new MenuAuthorizationManager.MenuAuthorizationManagerFactory(userService);
    }

    @Bean
    public StaticAuthorizationManagerFactory staticAuthorizationManagerFactory() {
        return new StaticAuthorizationManagerFactory();
    }

    @Bean
    public AuthorityAuthorizationManagerFactory authorityAuthorizationManagerFactory() {
        return new AuthorityAuthorizationManagerFactory();
    }
}
