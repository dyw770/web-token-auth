package cn.dyw.auth.db.security;

import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.model.AuthDto;
import cn.dyw.auth.db.model.ParentRoleDto;
import cn.dyw.auth.db.service.ICachedRoleService;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色授权管理器创建工厂
 * 
 * @author dyw770
 * @since 2025-05-24
 */
public class RoleAuthorizationManagerFactory implements AuthorizationManagerFactory {

    private final GrantedAuthorityDefaults grantedAuthorityDefaults;

    private final ICachedRoleService roleService;

    public RoleAuthorizationManagerFactory(GrantedAuthorityDefaults grantedAuthorityDefaults, ICachedRoleService roleService) {
        this.grantedAuthorityDefaults = grantedAuthorityDefaults;
        this.roleService = roleService;
    }

    private Map<String, List<String>> getSystemRoles() {
        // TODO 添加缓存
        List<ParentRoleDto> list = roleService.parentRoleList();
        return list.stream()
                .collect(Collectors.toMap(
                        ParentRoleDto::getRoleCode,
                        ParentRoleDto::getParentRoleCode
                ));
    }

    @Override
    public List<AuthorizationManager<RequestAuthorizationContext>> createAuthorizationManagers(List<AuthDto> auths) {
        List<String> roles = auths.stream()
                .filter(auth -> auth.getAuthType() == SysApiResourceAuth.AuthType.ROLE)
                .map(AuthDto::getAuthObject)
                .toList();
        if (roles.isEmpty()) {
            return List.of();
        }
        Map<String, List<String>> listMap = getSystemRoles();
        String[] array = roles.stream()
                .flatMap(role -> listMap.get(role).stream())
                .distinct()
                .toArray(String[]::new);

        return List.of(AuthorityAuthorizationManager.hasAnyRole(grantedAuthorityDefaults.getRolePrefix(), array));
    }
}
