package cn.dyw.auth.db.security;

import cn.dyw.auth.db.Constants;
import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.model.ApiResourceDto;
import cn.dyw.auth.db.model.AuthDto;
import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.db.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * jdbc 权限管理器
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Slf4j
public class JdbcAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final CopyOnWriteArrayList<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    private final AuthenticatedAuthorizationManager<RequestAuthorizationContext> authenticatedAuthorizationManager;

    private final ISysApiResourceService apiResourceService;

    private final RequestMatcherRegistry requestMatcherRegistry;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final List<AuthorizationManagerFactory> authorizationManagerFactories;

    public JdbcAuthorizationManager(ISysApiResourceService apiResourceService,
                                    ApplicationContext context,
                                    List<AuthorizationManagerFactory> authorizationManagerFactories) {
        this.apiResourceService = apiResourceService;
        this.authorizationManagerFactories = authorizationManagerFactories;

        mappings = new CopyOnWriteArrayList<>();
        authenticatedAuthorizationManager = AuthenticatedAuthorizationManager.authenticated();
        requestMatcherRegistry = new RequestMatcherRegistry(context);

        refresh();
    }


    @Override
    @SuppressWarnings("deprecation")
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {

        log.debug("请求授权：{}", RequestUtils.requestLine(requestContext.getRequest()));

        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        try {
            readLock.lock();
            return doCheck(authentication, requestContext);
        } catch (Exception e) {
            log.error("权限校验时出现异常", e);
        } finally {
            readLock.unlock();
        }
        return authenticatedAuthorizationManager.check(authentication, requestContext);
    }

    @SuppressWarnings("deprecation")
    private AuthorizationDecision doCheck(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
        for (ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : this.mappings) {

            RequestMatcher matcher = mapping.requestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(requestContext.getRequest());
            if (matchResult.isMatch()) {
                if (log.isDebugEnabled()) {
                    log.debug("请求 {} 成功匹配, id: {}, path: {}, method: {}, type: {}", RequestUtils.requestLine(requestContext.getRequest()), mapping.dto().getId(), mapping.dto().getApiPath(), mapping.dto().getApiMethod(), mapping.dto().getMatchType());
                }
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.entry();

                AuthorizationDecision check = manager.check(authentication, requestContext);
                if (log.isDebugEnabled()) {
                    log.debug("请求 {} 使用 {} 检测权限结果: {}", RequestUtils.requestLine(requestContext.getRequest()), manager, check);
                }
                return check;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("请求 {} 没有匹配到对应的权限配置", RequestUtils.requestLine(requestContext.getRequest()));
        }

        return authenticatedAuthorizationManager.check(authentication, requestContext);
    }


    public void refresh() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("初始化jdbc api授权信息");

        if (authorizationManagerFactories.isEmpty()) {
            log.warn("当前幻境没有注入权限管理器, 跳过初始化");
        } else {
            List<ApiResourceDto> resourceList = apiResourceService.listAll();

            ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
            try {
                writeLock.lock();
                mappings.clear();
                initAuthorizationManager(resourceList);
            } catch (Exception e) {
                log.error("更新资源授权信息失败", e);
            } finally {
                writeLock.unlock();
            }
        }

        stopWatch.stop();
        log.info("初始化 jdbc api 授权信息 \n{}", stopWatch.prettyPrint());
    }

    private void initAuthorizationManager(List<ApiResourceDto> resourceList) {
        for (ApiResourceDto apiResourceDto : resourceList) {
            initAuthorizationManager(apiResourceDto);
        }
    }

    private void initAuthorizationManager(ApiResourceDto resource) {

        if (StringUtils.isBlank(resource.getApiPath())) {
            return;
        }

        List<AuthorizationManager<RequestAuthorizationContext>> managers = getAuthorizationManagers(resource);

        // 如果没有配置授权模式则不处理，交给最外层的登录授权管理器处理
        if (!CollectionUtils.isEmpty(managers)) {
            List<RequestMatcher> matchers = createMatcher(resource);
            DelegatingAuthorizationManager authorizationManager = new DelegatingAuthorizationManager(managers);
            for (RequestMatcher matcher : matchers) {
                mappings.add(new ApiResourceRequestMatcherEntry<>(resource, matcher, authorizationManager));
            }
        }
    }

    public List<RequestMatcher> createMatcher(ApiResourceDto resource) {
        String apiPath = resource.getApiPath();
        String method = resource.getApiMethod();

        SysApiResource.MatchType matchType = resource.getMatchType();

        return switch (matchType) {
            case ANT -> requestMatcherRegistry.requestMatchers(createMethod(method), apiPath);
            case REGEX -> Collections.singletonList(RegexRequestMatcher.regexMatcher(createMethod(method), apiPath));
        };
    }

    public HttpMethod createMethod(String method) {
        if (StringUtils.isBlank(method) || StringUtils.equalsIgnoreCase(method, Constants.API_RESOURCE_METHOD_ALL)) {
            return null;
        }
        return HttpMethod.valueOf(method.toUpperCase());
    }

    /**
     * 根据资源授权信息创建授权管理器
     *
     * @param resource 资源信息
     * @return 授权管理器
     */
    private List<AuthorizationManager<RequestAuthorizationContext>> getAuthorizationManagers(ApiResourceDto resource) {

        Map<SysApiResourceAuth.AuthType, List<AuthDto>> authMap = resource
                .getAuths()
                .stream()
                .collect(Collectors.groupingBy(AuthDto::getAuthType));

        List<AuthorizationManager<RequestAuthorizationContext>> managers = new ArrayList<>();
        for (SysApiResourceAuth.AuthType authType : authMap.keySet()) {
            for (AuthorizationManagerFactory authorizationManagerFactory : authorizationManagerFactories) {

                List<AuthorizationManager<RequestAuthorizationContext>> authorizationManagers =
                        authorizationManagerFactory.createAuthorizationManagers(authMap.get(authType));
                if (!CollectionUtils.isEmpty(authorizationManagers)) {
                    managers.addAll(authorizationManagers);
                }
            }
        }

        return managers;
    }

}
