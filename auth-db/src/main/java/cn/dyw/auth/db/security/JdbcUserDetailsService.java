package cn.dyw.auth.db.security;

import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

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

        List<? extends GrantedAuthority> authorities
                = userService.userAuthorities(username, grantedAuthorityDefaults.getRolePrefix());
 
        return new User(userDto.getUsername(),
                userDto.getPassword(),
                userDto.getEnabled(),
                userDto.getAccountNonExpired(),
                userDto.getCredentialsNonExpired(),
                userDto.getAccountNonLocked(),
                authorities);
    }
}
