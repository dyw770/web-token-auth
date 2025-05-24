package cn.dyw.auth.db.security;

import cn.dyw.auth.db.Constants;
import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.model.AuthDto;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.Optional;

/**
 * 静态授权管理器创建工厂
 * 
 * @author dyw770
 * @since 2025-05-24
 */
public class StaticAuthorizationManagerFactory implements AuthorizationManagerFactory {

    private final AuthorizationManager<RequestAuthorizationContext> permitAllAuthorizationManager = (a, o) -> new AuthorizationDecision(true);

    private final AuthorizationManager<RequestAuthorizationContext> denyAllAuthorizationManager = (a, o) -> new AuthorizationDecision(false);


    @Override
    public List<AuthorizationManager<RequestAuthorizationContext>> createAuthorizationManagers(List<AuthDto> auths) {
        List<AuthDto> list = auths.stream()
                .filter(auth -> auth.getAuthType() == SysApiResourceAuth.AuthType.STATIC)
                .toList();

        if (list.isEmpty()) {
            return List.of();
        }

        Optional<AuthDto> deny = list.stream()
                .filter(auth -> Constants.AUTH_OBJECT_STATIC_DENY.equals(auth.getAuthObject()))
                .findAny();
        if (deny.isPresent()) {
            return List.of(denyAllAuthorizationManager);
        }
        return List.of(permitAllAuthorizationManager);
    }
}
