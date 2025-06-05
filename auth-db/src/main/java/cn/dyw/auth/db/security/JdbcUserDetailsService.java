package cn.dyw.auth.db.security;

import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author dyw770
 * @since 2025-04-17
 */
public class JdbcUserDetailsService implements UserDetailsService {

    private final ISysUserService userService;

    private final GrantedAuthorityDefaults grantedAuthorityDefaults;
    

    public JdbcUserDetailsService(ISysUserService userService,
                                  GrantedAuthorityDefaults grantedAuthorityDefaults) {
        this.userService = userService;
        this.grantedAuthorityDefaults = grantedAuthorityDefaults;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.getUserByUsername(username);
        if (ObjectUtils.isEmpty(userDto)) {
            throw new UsernameNotFoundException(username + "用户不存在");
        }
    
        // 查询权限
        Stream<SimpleGrantedAuthority> permissionStream = userService
                .userPermission(username)
                .stream()
                .map(SimpleGrantedAuthority::new);

        // 查询角色
        Stream<SimpleGrantedAuthority> roleStream = userService.userAuthRoles(username)
                .stream()
                .map(role -> new SimpleGrantedAuthority(grantedAuthorityDefaults.getRolePrefix() + role));


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
