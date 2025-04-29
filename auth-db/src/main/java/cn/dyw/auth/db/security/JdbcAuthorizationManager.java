package cn.dyw.auth.db.security;

import cn.dyw.auth.db.Constants;
import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.model.ApiResourceDto;
import cn.dyw.auth.db.model.AuthDto;
import cn.dyw.auth.db.model.ParentRoleDto;
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

    private final GrantedAuthorityDefaults grantedAuthorityDefaults;

    private final ISysRoleService roleService;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final AuthorizationManager<RequestAuthorizationContext> permitAllAuthorizationManager = (a, o) -> new AuthorizationDecision(true);

    private final AuthorizationManager<RequestAuthorizationContext> denyAllAuthorizationManager = (a, o) -> new AuthorizationDecision(false);

    public JdbcAuthorizationManager(ISysApiResourceService apiResourceService, ApplicationContext context, GrantedAuthorityDefaults grantedAuthorityDefaults, ISysRoleService roleService) {
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
        return denyAllAuthorizationManager.check(authentication, requestContext);
    }

    @SuppressWarnings("deprecation")
    private AuthorizationDecision doCheck(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
        for (ApiResourceRequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : this.mappings) {

            RequestMatcher matcher = mapping.getRequestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(requestContext.getRequest());
            if (matchResult.isMatch()) {
                if (log.isDebugEnabled()) {
                    log.debug("请求 {} 成功匹配, id: {}, path: {}, method: {}, type: {}", RequestUtils.requestLine(requestContext.getRequest()), mapping.getDto().getId(), mapping.getDto().getApiPath(), mapping.getDto().getApiMethod(), mapping.getDto().getMatchType());
                }
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();

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

        List<ApiResourceDto> resourceList = apiResourceService.listAll();
        Map<String, List<String>> roleMap = getRoleMap();

        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            mappings.clear();
            initAuthorizationManager(resourceList, roleMap);
        } catch (Exception e) {
            log.error("更新资源授权信息失败", e);
        } finally {
            writeLock.unlock();
        }

        stopWatch.stop();
        log.info("初始化 jdbc api 授权信息 \n{}", stopWatch.prettyPrint());
    }

    private Map<String, List<String>> getRoleMap() {
        List<ParentRoleDto> list = roleService.parentRoleList();
        return list.stream()
                .collect(Collectors.toMap(
                        ParentRoleDto::getRoleCode,
                        ParentRoleDto::getParentRoleCode
                ));
    }

    private void initAuthorizationManager(List<ApiResourceDto> resourceList, Map<String, List<String>> roleMap) {
        for (ApiResourceDto apiResourceDto : resourceList) {
            initAuthorizationManager(apiResourceDto, roleMap);
        }
    }

    private void initAuthorizationManager(ApiResourceDto resource, Map<String, List<String>> roleMap) {

        if (StringUtils.isBlank(resource.getApiPath())) {
            return;
        }

        List<AuthorizationManager<RequestAuthorizationContext>> managers = getAuthorizationManagers(resource, roleMap);

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
     * @param roleMap  角色层级关系
     * @return 授权管理器
     */
    private List<AuthorizationManager<RequestAuthorizationContext>> getAuthorizationManagers(ApiResourceDto resource, Map<String, List<String>> roleMap) {

        Map<SysApiResourceAuth.AuthType, List<AuthDto>> authMap = resource.getAuths().stream().collect(Collectors.groupingBy(AuthDto::getAuthType));

        List<AuthorizationManager<RequestAuthorizationContext>> managers = new ArrayList<>();
        for (SysApiResourceAuth.AuthType authType : authMap.keySet()) {

            switch (authType) {
                case ROLE -> managers.add(roleAuthorizationManagers(authMap.get(authType), roleMap));
                case IP -> managers.add(ipAuthorizationManagers(authMap.get(authType)));
                case STATIC -> managers.add(staticAuthorizationManagers(authMap.get(authType)));
                default -> log.warn("未知的授权类型: {}", authType);
            }
        }

        return managers;
    }

    /**
     * 角色授权
     *
     * @param auths   角色授权信息
     * @param roleMap 角色层级信息
     * @return 授权管理器
     */
    private AuthorizationManager<RequestAuthorizationContext> roleAuthorizationManagers(List<AuthDto> auths, Map<String, List<String>> roleMap) {

        String[] roles = auths
                .stream()
                .filter(auth -> auth.getAuthType() == SysApiResourceAuth.AuthType.ROLE)
                .flatMap(auth -> roleMap.get(auth.getAuthObject()).stream())
                .distinct()
                .toArray(String[]::new);

        return AuthorityAuthorizationManager.hasAnyRole(grantedAuthorityDefaults.getRolePrefix(), roles);
    }

    /**
     * ip授权
     *
     * @param auths 授权信息
     * @return 授权管理器
     */
    private AuthorizationManager<RequestAuthorizationContext> ipAuthorizationManagers(List<AuthDto> auths) {
        List<String> ips = auths.stream()
                .filter(auth -> auth.getAuthType() == SysApiResourceAuth.AuthType.IP)
                .map(AuthDto::getAuthObject)
                .toList();

        return IpAddressAuthorizationManager.hasIpAddress(ips);
    }

    /**
     * 静态规则授权
     *
     * @param auths 授权信息
     * @return 授权管理器
     */
    private AuthorizationManager<RequestAuthorizationContext> staticAuthorizationManagers(List<AuthDto> auths) {
        long count = auths.stream().filter(auth ->
                StringUtils.equalsIgnoreCase(auth.getAuthObject(), Constants.AUTH_OBJECT_STATIC_PUBLIC)
        ).count();

        return count > 0 ? permitAllAuthorizationManager : denyAllAuthorizationManager;
    }

}
