package cn.dyw.auth.db.security;

import cn.dyw.auth.db.Constants;
import cn.dyw.auth.db.model.ApiResourceDto;
import cn.dyw.auth.db.model.RoleDto;
import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.db.service.ISysRoleService;
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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * jdbc 权限管理器
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Slf4j
public class JdbcAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final CopyOnWriteArrayList<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    private final AuthenticatedAuthorizationManager<RequestAuthorizationContext> authenticatedAuthorizationManager;

    private final ISysApiResourceService apiResourceService;

    private final RequestMatcherRegistry requestMatcherRegistry;

    private final GrantedAuthorityDefaults grantedAuthorityDefaults;

    private final ISysRoleService roleService;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final AuthorizationManager<RequestAuthorizationContext> permitAllAuthorizationManager =
            (a, o) -> new AuthorizationDecision(true);

    private final AuthorizationManager<RequestAuthorizationContext> denyAllAuthorizationManager =
            (a, o) -> new AuthorizationDecision(false);

    public JdbcAuthorizationManager(ISysApiResourceService apiResourceService,
                                    ApplicationContext context,
                                    GrantedAuthorityDefaults grantedAuthorityDefaults,
                                    ISysRoleService roleService) {
        this.apiResourceService = apiResourceService;
        this.grantedAuthorityDefaults = grantedAuthorityDefaults;
        this.roleService = roleService;

        mappings = new CopyOnWriteArrayList<>();
        authenticatedAuthorizationManager = AuthenticatedAuthorizationManager.authenticated();
        requestMatcherRegistry = new RequestMatcherRegistry(context);

        refresh();
    }


    @Override
    @SuppressWarnings("deprecation")
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestContext) {

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
        return denyAllAuthorizationManager.check(authentication, requestContext);
    }

    @SuppressWarnings("deprecation")
    private AuthorizationDecision doCheck(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
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

        List<ApiResourceDto> resourceList = apiResourceService.listAll();
        Map<String, List<String>> roleMap = getRoleMap();


        for (ApiResourceDto resource : resourceList) {
            initAuthorizationManager(resource, roleMap);
        }

        stopWatch.stop();
        log.info("初始化 jdbc api 授权信息 \n{}", stopWatch.prettyPrint());
    }

    private Map<String, List<String>> getRoleMap() {
        return roleService.roleList()
                .stream()
                .collect(Collectors.toMap(RoleDto::getRoleCode, dto -> {

                    List<String> childrenRoles = dto.getChildren()
                            .stream()
                            .map(RoleDto::getRoleCode)
                            .toList();

                    return Stream.concat(childrenRoles.stream(), Stream.of(dto.getRoleCode()))
                            .distinct()
                            .toList();
                }));
    }

    private void initAuthorizationManager(ApiResourceDto resource, Map<String, List<String>> roleMap) {

        String apiPath = resource.getApiPath();
        String method = resource.getApiMethod();
        if (StringUtils.isBlank(apiPath)) {
            return;
        }
        List<RequestMatcher> matchers;
        if (StringUtils.isBlank(method) || StringUtils.equalsIgnoreCase(method, Constants.API_RESOURCE_METHOD_ALL)) {
            matchers = requestMatcherRegistry.requestMatchers(apiPath);
        } else {
            matchers = requestMatcherRegistry.requestMatchers(HttpMethod.valueOf(method.toUpperCase()), apiPath);
        }

        List<AuthorizationManager<RequestAuthorizationContext>> managers = getAuthorizationManagers(resource, roleMap);
        DelegatingAuthorizationManager authorizationManager = new DelegatingAuthorizationManager(managers);
        List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> list = new ArrayList<>();
        for (RequestMatcher matcher : matchers) {
            // 如果没有配置 则表示需要登陆才能访问
            if (CollectionUtils.isEmpty(managers)) {
                list.add(new RequestMatcherEntry<>(matcher, authenticatedAuthorizationManager));
            } else {
                list.add(new RequestMatcherEntry<>(matcher, authorizationManager));
            }
        }

        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            mappings.clear();
            mappings.addAll(list);
        } catch (Exception e) {
            log.error("更新资源授权信息失败", e);
        } finally {
            writeLock.unlock();
        }
    }

    private List<AuthorizationManager<RequestAuthorizationContext>> getAuthorizationManagers(ApiResourceDto resource,
                                                                                             Map<String, List<String>> roleMap) {
        // TODO 
        // 还需要优化，将角色合并到一个AuthorizationManager
        Stream<AuthorizationManager<RequestAuthorizationContext>> managerStream = resource.getAuths()
                .stream()
                .map(auth -> switch (auth.getAuthType()) {
                    case ROLE -> AuthorityAuthorizationManager.hasAnyRole(
                            grantedAuthorityDefaults.getRolePrefix(),
                            roleMap.get(auth.getAuthObject()).toArray(new String[0]));
                    case IP -> IpAddressAuthorizationManager.hasIpAddress(auth.getAuthObject());
                    case STATIC -> {
                        if (StringUtils.isBlank(auth.getAuthObject()) ||
                                StringUtils.equalsIgnoreCase(auth.getAuthObject(),
                                        Constants.AUTH_OBJECT_STATIC_PUBLIC)) {
                            yield permitAllAuthorizationManager;
                        } else {
                            yield denyAllAuthorizationManager;
                        }
                    }
                });
        return managerStream.toList();
    }

}
