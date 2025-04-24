package cn.dyw.auth.db.security;

import cn.dyw.auth.db.model.ApiResourceDto;
import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.db.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.IpAddressAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * jdbc 权限管理器
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Slf4j
public class JdbcAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    private final AuthenticatedAuthorizationManager<RequestAuthorizationContext> authenticatedAuthorizationManager;

    private final ISysApiResourceService apiResourceService;

    private final RequestMatcherRegistry requestMatcherRegistry;

    private final GrantedAuthorityDefaults grantedAuthorityDefaults;

    public JdbcAuthorizationManager(ISysApiResourceService apiResourceService,
                                    ApplicationContext context,
                                    GrantedAuthorityDefaults grantedAuthorityDefaults) {
        this.apiResourceService = apiResourceService;
        this.grantedAuthorityDefaults = grantedAuthorityDefaults;

        mappings = new ArrayList<>();
        authenticatedAuthorizationManager = AuthenticatedAuthorizationManager.authenticated();
        requestMatcherRegistry = new RequestMatcherRegistry(context);

        refresh();
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


    public void refresh() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("初始化jdbc api授权信息");
        
        // TODO 
        // 还需要将角色的层级关系设置到数据中
        List<ApiResourceDto> resourceList = apiResourceService.listAll();
        
        for (ApiResourceDto resource : resourceList) {
            String apiPath = resource.getApiPath();
            String method = resource.getApiMethod();
            if (StringUtils.isBlank(apiPath)) {
                continue;
            }
            List<RequestMatcher> matchers;
            if (StringUtils.isBlank(method) || StringUtils.equalsIgnoreCase(method, "ALL")) {
                matchers = requestMatcherRegistry.requestMatchers(apiPath);
            } else {
                matchers = requestMatcherRegistry.requestMatchers(HttpMethod.valueOf(method.toUpperCase()), apiPath);
            }
            
            List<AuthorizationManager<RequestAuthorizationContext>> managers = getAuthorizationManagers(resource);
            DelegatingAuthorizationManager authorizationManager = new DelegatingAuthorizationManager(managers);
            
            for (RequestMatcher matcher : matchers) {
                // 如果没有配置 则表示需要登陆才能访问
                if (CollectionUtils.isEmpty(managers)) {
                    mappings.add(new RequestMatcherEntry<>(matcher, authenticatedAuthorizationManager));
                } else {
                    mappings.add( new RequestMatcherEntry<>(matcher, authorizationManager));
                }
            }
        }

        stopWatch.stop();
        log.info("初始化 jdbc api 授权信息 \n{}", stopWatch.prettyPrint());
    }

    private List<AuthorizationManager<RequestAuthorizationContext>> getAuthorizationManagers(ApiResourceDto resource) {
        // TODO 
        // 还需要优化，将角色合并到一个AuthorizationManager
        Stream<AuthorizationManager<RequestAuthorizationContext>> managerStream = resource.getAuths()
                .stream()
                .map(auth -> switch (auth.getAuthType()) {
                    case ROLE ->
                            AuthorityAuthorizationManager.hasRole(auth.getAuthObject());
                    case IP -> IpAddressAuthorizationManager.hasIpAddress(auth.getAuthObject());
                });
        return managerStream.toList();
    }

}
