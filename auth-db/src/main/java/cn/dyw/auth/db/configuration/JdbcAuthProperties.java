package cn.dyw.auth.db.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dyw770
 * @since 2025-04-24
 */
@Data
@ConfigurationProperties(prefix = "app.auth.jdbc")
public class JdbcAuthProperties {
    
    /**
     * spring security 角色前缀
     */
    private String rolePrefix = "ROLE_";

    /**
     * 是否启用jdbc api权限认证
     */
    private boolean enableJdbcApiAuth = false;
    
}
