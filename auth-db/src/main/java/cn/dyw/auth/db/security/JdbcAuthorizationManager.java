package cn.dyw.auth.db.security;

import cn.dyw.auth.db.Constants;
import cn.dyw.auth.db.configuration.JdbcAuthProperties;
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

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * jdbc 权限管理器
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Slf4j
public class JdbcAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private CopyOnWriteArrayList<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    private final AuthenticatedAuthorizationManager<RequestAuthorizationContext> authenticatedAuthorizationManager;

    private final ISysApiResourceService apiResourceService;

    private final RequestMatcherRegistry requestMatcherRegistry;

    private final ReentrantLock lock = new ReentrantLock();

    private final List<AuthorizationManagerFactory> authorizationManagerFactories;
    
    private final JdbcAuthProperties jdbcAuthProperties;

    private final AuthorizationManager<RequestAuthorizationContext> denyAllAuthorizationManager = (a, o) -> new AuthorizationDecision(false);

    public JdbcAuthorizationManager(ISysApiResourceService apiResourceService,
                                    ApplicationContext context,
                                    List<AuthorizationManagerFactory> authorizationManagerFactories,
                                    JdbcAuthProperties jdbcAuthProperties) {
        this.apiResourceService = apiResourceService;
        this.authorizationManagerFactories = authorizationManagerFactories;
        this.jdbcAuthProperties = jdbcAuthProperties;

        authenticatedAuthorizationManager = AuthenticatedAuthorizationManager.authenticated();
        requestMatcherRegistry = new RequestMatcherRegistry(context);

        refresh();
    }


    @Override
    @SuppressWarnings("deprecation")
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {

        log.debug("请求授权：{}", RequestUtils.requestLine(requestContext.getRequest()));

        try {
            return doCheck(authentication, requestContext);
        } catch (Exception e) {
            log.error("权限校验时出现异常", e);
        }
        if(jdbcAuthProperties.isEnableAuthError()) {
            return denyAllAuthorizationManager.check(authentication, requestContext);
        } else {
            return authenticatedAuthorizationManager.check(authentication, requestContext);
        }
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
        StopWatch stopWatch = new StopWatch("jdbc 授权加载信息");
        stopWatch.start("初始化jdbc api授权信息");

        if (authorizationManagerFactories.isEmpty()) {
            log.warn("当前环境没有注入权限管理器, 跳过初始化");
        } else {
            List<ApiResourceDto> resourceList = apiResourceService.listAll();

            try {
                lock.lock();
                List<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> entries
                        = initAuthorizationManager(resourceList);
                mappings = new CopyOnWriteArrayList<>(entries);
            } catch (Exception e) {
                log.error("更新资源授权信息失败", e);
            } finally {
                lock.unlock();
            }
        }

        stopWatch.stop();
        log.info("初始化 jdbc api 授权信息 \n{}", stopWatch.prettyPrint());
    }

    public void refresh(List<Integer> resourceIds) {
        StopWatch stopWatch = new StopWatch("刷新jdbc 授权加载信息");
        stopWatch.start("刷新jdbc api授权信息 " + resourceIds);
        try {
            lock.lock();
            List<ApiResourceDto> resources = apiResourceService.getResourceByIds(resourceIds);
            if (CollectionUtils.isEmpty(resources)) {
                return;
            }
            List<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> entries
                    = initAuthorizationManager(resources);

            Map<Integer, ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> map = entries.stream()
                    .collect(toMap(entry -> entry.dto().getId(), entry -> entry));
            Set<Integer> findResourceIds = map.keySet();

            if (!entries.isEmpty()) {
                mappings.removeIf(mapping ->
                        // 如果在刷新列表中, 但是不在从数据库中加载出来的列表 则删除
                        resourceIds.contains(mapping.dto().getId()) && !findResourceIds.contains(mapping.dto().getId()));

                mappings.replaceAll(mapping ->
                        // 如果在刷新列表中, 则替换
                        map.getOrDefault(mapping.dto().getId(), mapping));
            }
        } catch (Exception e) {
            log.error("更新资源授权信息失败", e);
        } finally {
            lock.unlock();
        }
        stopWatch.stop();
        log.info("刷新 jdbc api 授权信息 \n{}", stopWatch.prettyPrint());
    }

    private List<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> initAuthorizationManager(List<ApiResourceDto> resourceList) {
        List<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> entries
                = new ArrayList<>(resourceList.size());
        for (ApiResourceDto apiResourceDto : resourceList) {
            List<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> list
                    = initAuthorizationManager(apiResourceDto);
            entries.addAll(list);
        }

        return entries;
    }

    private List<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> initAuthorizationManager(ApiResourceDto resource) {

        if (StringUtils.isBlank(resource.getApiPath())) {
            return Collections.emptyList();
        }

        List<AuthorizationManager<RequestAuthorizationContext>> managers = getAuthorizationManagers(resource);
        List<ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> entries
                = new ArrayList<>(managers.size());
        // 如果没有配置授权模式则不处理，交给最外层的登录授权管理器处理
        if (!CollectionUtils.isEmpty(managers)) {
            List<RequestMatcher> matchers = createMatcher(resource);
            DelegatingAuthorizationManager authorizationManager = new DelegatingAuthorizationManager(managers);
            for (RequestMatcher matcher : matchers) {
                entries.add(new ApiResourceRequestMatcherEntry<>(resource, matcher, authorizationManager));
            }
        }
        return entries;
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
