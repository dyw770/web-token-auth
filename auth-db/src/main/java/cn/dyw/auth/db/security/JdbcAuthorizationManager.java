package cn.dyw.auth.db.security;

import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.db.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * jdbc 权限管理器
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Slf4j
@Service
public class JdbcAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    private final AuthenticatedAuthorizationManager<RequestAuthorizationContext> authenticatedAuthorizationManager;

    private final ISysApiResourceService apiResourceService;

    public JdbcAuthorizationManager(ISysApiResourceService apiResourceService) {
        this.apiResourceService = apiResourceService;
        mappings = new ArrayList<>();
        authenticatedAuthorizationManager = AuthenticatedAuthorizationManager.authenticated();

//        List<ApiResourceDto> resourceList = apiResourceService.listAll();
    }


    @Override
    @SuppressWarnings("deprecation")
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestContext) {

        log.debug("请求授权：{}", RequestUtils.requestLine(requestContext.getRequest()));

        for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : this.mappings) {

            RequestMatcher matcher = mapping.getRequestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(requestContext.getRequest());
            if (matchResult.isMatch()) {
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();

                log.debug("请求 {} 使用 {} 检测权限", RequestUtils.requestLine(requestContext.getRequest()), manager);
                return manager.check(authentication, requestContext);
            }
        }
        log.debug("请求 {} 没有匹配到对应的权限配置", RequestUtils.requestLine(requestContext.getRequest()));

        return authenticatedAuthorizationManager.check(authentication, requestContext);
    }


}
