package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.SpringSecurityAuthApplicationTests;
import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.domain.SysUserRole;
import cn.dyw.auth.db.message.rq.UserSearchRq;
import cn.dyw.auth.db.message.rs.UserRs;
import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysUserRoleService;
import cn.dyw.auth.db.service.ISysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@Slf4j
@SpringBootTest(classes = SpringSecurityAuthApplicationTests.class)
public class SysUserServiceImplTest {

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysUserRoleService userRoleService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void getUserByUsername() {
        UserDto user = userService.getUserByUsername("test");
        log.info("查询用户：{}", user);
        assertNotNull(user);
    }

    @Test
    public void userList() {
        UserSearchRq userSearchRq = new UserSearchRq();
        userSearchRq.setPage(1);
        userSearchRq.setSize(10);
        Page<UserRs> users = userService.userList(userSearchRq);
        log.info("查询用户列表：{}", users);
        assertNotNull(users);
    }
    
    @Test
    @Transactional
    @Rollback(value = false)
    public void testSaveUser() {
        SysUser user = new SysUser();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setNickname("测试");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);

        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUsername("test");
        sysUserRole.setRoleCode("admin");
        sysUserRole.setAuthTime(LocalDateTime.now());
        sysUserRole.setExpiredTime(LocalDateTime.now().plusDays(1));
        userRoleService.save(sysUserRole);
    }
}