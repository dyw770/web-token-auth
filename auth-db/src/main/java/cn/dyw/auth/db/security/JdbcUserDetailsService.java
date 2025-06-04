package cn.dyw.auth.db.security;

import cn.dyw.auth.db.model.ParentRoleDto;
import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysRoleService;
import cn.dyw.auth.db.service.ISysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author dyw770
 * @since 2025-04-17
 */
public class JdbcUserDetailsService implements UserDetailsService {

    private final ISysUserService userService;

    private final GrantedAuthorityDefaults grantedAuthorityDefaults;
    
    private final ISysRoleService roleService;

    public JdbcUserDetailsService(ISysUserService userService,
                                  GrantedAuthorityDefaults grantedAuthorityDefaults,
                                  ISysRoleService roleService) {
        this.userService = userService;
        this.grantedAuthorityDefaults = grantedAuthorityDefaults;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.getUserByUsername(username);
        if (ObjectUtils.isEmpty(userDto)) {
            throw new UsernameNotFoundException(username + "用户不存在");
        }

        // 用户授权的角色
        List<String> authRole = roleService.userAuthRole(username);
        // 通过授权的角色查询到权限
        Stream<SimpleGrantedAuthority> permissionStream = userService
                .userPermission(username)
                .stream()
                .map(SimpleGrantedAuthority::new);

        // 根据用户已有的橘色 将其子角色一起构建出来
        List<ParentRoleDto> parentRoles= roleService.parentRoleList();
        Stream<SimpleGrantedAuthority> roleStream = parentRoles
                .stream()
                .filter(item -> CollectionUtils.containsAny(item.getParentRoleCode(), authRole))
                .map(role -> new SimpleGrantedAuthority(grantedAuthorityDefaults.getRolePrefix() + role.getRoleCode()));
        
        
        List<SimpleGrantedAuthority> authorities = Stream.concat(permissionStream, roleStream)
                .toList();

        return new User(userDto.getUsername(),
                userDto.getPassword(),
                userDto.getEnabled(),
                userDto.getAccountNonExpired(),
                userDto.getCredentialsNonExpired(),
                userDto.getAccountNonLocked(),
                authorities);
    }
}
