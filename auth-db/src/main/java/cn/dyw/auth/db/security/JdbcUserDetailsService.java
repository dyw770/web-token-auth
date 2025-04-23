package cn.dyw.auth.db.security;

import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-17
 */
@Service
public class JdbcUserDetailsService implements UserDetailsService {

    private final ISysUserService sysUserService;
    
    private final String ROLE_PREFIX = "ROLE_";

    public JdbcUserDetailsService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = sysUserService.getUserByUsername(username);
        if (ObjectUtils.isEmpty(userDto)) {
            throw new UsernameNotFoundException(username + "用户不存在");
        }

        List<SimpleGrantedAuthority> authorities = userDto.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
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
