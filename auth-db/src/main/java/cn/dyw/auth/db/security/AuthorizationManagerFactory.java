package cn.dyw.auth.db.security;


import cn.dyw.auth.db.model.AuthDto;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-24
 */
@FunctionalInterface
public interface AuthorizationManagerFactory {

    /**
     * 根据资源授权信息创建授权管理器
     *
     * @param auths 资源信息
     * @return 授权管理器
     */
    List<AuthorizationManager<RequestAuthorizationContext>> createAuthorizationManagers(List<AuthDto> auths);

}
