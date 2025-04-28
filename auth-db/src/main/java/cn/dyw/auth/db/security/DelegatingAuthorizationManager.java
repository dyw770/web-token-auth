package cn.dyw.auth.db.security;

import cn.dyw.auth.db.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Supplier;

/**
 * 授权管理器
 * 用来包装其他授权管理器
 *
 * @author dyw770
 * @since 2025-04-24
 */
@Slf4j
public class DelegatingAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final List<AuthorizationManager<RequestAuthorizationContext>> authorizationManagers;

    private final AuthorizationDecision allow = new AuthorizationDecision(true);

    private final AuthorizationDecision deny = new AuthorizationDecision(false);

    public DelegatingAuthorizationManager(
            List<AuthorizationManager<RequestAuthorizationContext>> authorizationManagers) {
        Assert.notEmpty(authorizationManagers, "authorizationManagers cannot be empty");
        this.authorizationManagers = authorizationManagers;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        for (AuthorizationManager<RequestAuthorizationContext> authorizationManager : authorizationManagers) {
            AuthorizationDecision check = authorizationManager.check(authentication, object);
            if (log.isDebugEnabled()) {
                log.debug("请求: {}, 授权对象: {}, 授权管理器: {} 权限认证结果: {}",
                        RequestUtils.requestLine(object.getRequest()),
                        authentication.get().getName(),
                        authorizationManager,
                        check);
            }
            if (check != null && check.isGranted()) {
                continue;
            }
            return deny;
        }
        return allow;
    }

    @Override
    public String toString() {
        return "DelegatingAuthorizationManager{" +
                "authorizationManagers=" + authorizationManagers +
                '}';
    }
}
