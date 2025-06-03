package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.domain.SysUserRole;
import cn.dyw.auth.db.message.rq.UserCreateRq;
import cn.dyw.auth.db.message.rq.UserEditRq;
import cn.dyw.auth.db.message.rq.UserSearchRq;
import cn.dyw.auth.db.message.rs.UserRs;
import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysUserRoleService;
import cn.dyw.auth.db.service.ISysUserService;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户管理
 *
 * @author dyw770
 * @since 2025-05-03
 */
@Validated
@RestController
@RequestMapping("${app.auth.api-context-path:/admin}/user")
public class UserManageController {

    private final ISysUserService userService;

    private final PasswordEncoder passwordEncoder;

    private final ISysUserRoleService userRoleService;

    public UserManageController(ISysUserService userService, PasswordEncoder passwordEncoder, ISysUserRoleService userRoleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;
    }

    /**
     * 查询用户列表
     *
     * @param rq 参数
     * @return 结果
     */
    @PostMapping("list")
    public Result<Page<UserRs>> userList(@RequestBody @Validated UserSearchRq rq) {
        return Result.createSuccess(userService.userList(rq));
    }

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping("/{username}")
    public Result<UserDto> user(@PathVariable("username") String username) {
        UserDto userDto = userService.getUserByUsername(username);
        userDto.setPassword("");
        return Result.createSuccess(userDto);
    }

    @GetMapping("roles")
    public Result<List<String>> userRoles(@RequestParam("username") String username) {
        List<SysUserRole> list = userRoleService.lambdaQuery()
                .eq(SysUserRole::getUsername, username)
                .list();
        List<String> roles = list.stream()
                .map(SysUserRole::getRoleCode)
                .toList();
        return Result.createSuccess(roles);
    }

    @PostMapping("add")
    public Result<Void> addUser(@RequestBody @Validated UserCreateRq rq) {
        SysUser sysUser = userService.getById(rq.getUsername());
        if (ObjectUtils.isNotEmpty(sysUser)) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "用户名已存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(rq, user);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setPassword(passwordEncoder.encode(rq.getPassword()));
        userService.save(user);
        return Result.createSuccess();
    }

    /**
     * 修改用户信息
     *
     * @param rq 用户信息
     * @return 修改成功
     */
    @PutMapping("edit")
    public Result<Void> editUser(@RequestBody @Validated UserEditRq rq) {
        SysUser sysUser = userService.getById(rq.getUsername());
        if (ObjectUtils.isEmpty(sysUser)) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "用户名不存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(rq, user);
        user.setUpdateTime(LocalDateTime.now());

        if (StringUtils.isNotBlank(rq.getPassword())) {
            user.setPassword(passwordEncoder.encode(rq.getPassword()));
        }
        userService.updateById(user);

        return Result.createSuccess();
    }

    /**
     * 锁定用户
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping("/lock/{username}")
    public Result<Void> lockUser(@PathVariable @NotBlank String username) {
        SysUser sysUser = userService.getById(username);
        if (ObjectUtils.isEmpty(sysUser)) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "用户名不存在");
        }
        userService.lambdaUpdate()
                .eq(SysUser::getUsername, username)
                .set(SysUser::getAccountNonLocked, !sysUser.getAccountNonLocked())
                .update();
        return Result.createSuccess();
    }

    /**
     * 启用用户
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping("/enable/{username}")
    public Result<Void> enableUser(@PathVariable @NotBlank String username) {
        SysUser sysUser = userService.getById(username);
        if (ObjectUtils.isEmpty(sysUser)) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "用户名不存在");
        }
        userService.lambdaUpdate()
                .eq(SysUser::getUsername, username)
                .set(SysUser::getEnabled, !sysUser.getEnabled())
                .update();
        return Result.createSuccess();
    }

    /**
     * 设置用户角色
     *
     * @param role     用户角色
     * @param username 用户名
     * @param del      删除角色
     * @return 结果
     */
    @PostMapping("/auth/{username}")
    public Result<Void> authRoleForUser(@RequestParam("role") @NotBlank String role, @PathVariable String username, @RequestParam("del") boolean del) {
        userService.addRoleForUser(username, role, del);
        return Result.createSuccess();
    }
}
