package cn.dyw.auth.db.security;

import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.model.AuthDto;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;

/**
 * 权限授权管理器
 * <p>
 * 普通权限和菜单子权限实际上是同一个功能实现, 只是为了在使用上区分, 因此使用同一个工厂类来创建
 *
 * @author dyw770
 * @since 2025-05-24
 */
public class AuthorityAuthorizationManagerFactory implements AuthorizationManagerFactory {

    @Override
    public List<AuthorizationManager<RequestAuthorizationContext>> createAuthorizationManagers(List<AuthDto> auths) {
        String[] permissions = auths.stream()
                .filter(auth ->
                        auth.getAuthType() == SysApiResourceAuth.AuthType.MENU_SUB_PERMISSION ||
                                auth.getAuthType() == SysApiResourceAuth.AuthType.PERMISSION)
                .map(AuthDto::getAuthObject)
                .toArray(String[]::new);
        if (ArrayUtils.isEmpty(permissions)) {
            return List.of();
        }

        return List.of(AuthorityAuthorizationManager.hasAnyAuthority(permissions));
    }

}
