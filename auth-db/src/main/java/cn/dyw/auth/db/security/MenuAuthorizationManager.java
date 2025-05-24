package cn.dyw.auth.db.security;

import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.model.AuthDto;
import cn.dyw.auth.db.model.MenuPermissionDto;
import cn.dyw.auth.db.service.ISysMenusService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 是否有菜单授权
 *
 * @author dyw770
 * @since 2025-04-28
 */
public class MenuAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final List<Integer> menuIds;

    private final ISysMenusService menusService;

    private MenuAuthorizationManager(List<Integer> menuIds, ISysMenusService menusService) {
        this.menuIds = menuIds;
        this.menusService = menusService;
    }


    public static MenuAuthorizationManager hasMenus(List<Integer> menuIds, ISysMenusService menusService) {
        Assert.notEmpty(menuIds, "menus cannot be null");
        return new MenuAuthorizationManager(menuIds, menusService);
    }

    @Override
    @SuppressWarnings("deprecation")
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestAuthorizationContext) {
        String username = authentication.get().getName();
        List<MenuPermissionDto> menuList = menusService.userMenuList(username);
        Optional<MenuPermissionDto> any = menuList.stream()
                .filter(menuDto -> menuIds.contains(menuDto.getId()))
                .findAny();
        if (any.isPresent()) {
            return new AuthorizationDecision(true);
        } else {
            return new AuthorizationDecision(false);
        }
    }

    @Override
    public String toString() {
        return "IpAddressAuthorizationManager[ip='" + this.menuIds + "']";
    }
    
    
    public static class MenuAuthorizationManagerFactory implements AuthorizationManagerFactory {
        
        private final ISysMenusService menusService;

        public MenuAuthorizationManagerFactory(ISysMenusService menusService) {
            this.menusService = menusService;
        }

        @Override
        public List<AuthorizationManager<RequestAuthorizationContext>> createAuthorizationManagers(List<AuthDto> auths) {
            List<Integer> menuIds = auths.stream()
                    .filter(auth -> auth.getAuthType() == SysApiResourceAuth.AuthType.MENU)
                    .map(auth -> Integer.parseInt(auth.getAuthObject()))
                    .toList();
            if (menuIds.isEmpty()) {
                return List.of();
            }
            return List.of(MenuAuthorizationManager.hasMenus(menuIds, menusService));
        }
    }
}
