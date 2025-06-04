package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.message.rq.UserUpdatePasswordRq;
import cn.dyw.auth.db.message.rs.UserInfoRs;
import cn.dyw.auth.db.message.rs.UserMenuMetaRs;
import cn.dyw.auth.db.message.rs.UserMenuRs;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.model.MenuPermissionDto;
import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysMenusService;
import cn.dyw.auth.db.service.ISysUserService;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@RestController
@RequestMapping("/user")
public class UserSupportController {

    private final ISysUserService userService;

    private final ISysMenusService menusService;

    private final PasswordEncoder passwordEncoder;

    public UserSupportController(ISysUserService userService,
                                 ISysMenusService menusService,
                                 PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.menusService = menusService;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * 用户信息
     *
     * @param user 当前登录用户
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoRs> info(@AuthenticationPrincipal User user) {
        UserDto userDto = userService.getUserByUsername(user.getUsername());
        UserInfoRs rs = new UserInfoRs();
        BeanUtils.copyProperties(userDto, rs);
        return Result.createSuccess(rs);
    }

    @GetMapping("/permissions")
    public Result<List<String>> permission(@AuthenticationPrincipal User user) {
        return Result.createSuccess(userService.userPermission(user.getUsername()));
    }

    /**
     * 修改密码
     *
     * @param user 当前登录用户
     * @param rq   修改密码请求
     * @return 修改密码结果
     */
    @PostMapping("/update/password")
    public Result<Void> updatePassword(@AuthenticationPrincipal User user,
                                       @RequestBody @Validated UserUpdatePasswordRq rq) {
        SysUser sysUser = userService.getById(user.getUsername());
        if (ObjectUtils.isNotEmpty(sysUser) && passwordEncoder.matches(rq.getPassword(), sysUser.getPassword())) {
            userService.lambdaUpdate()
                    .eq(SysUser::getUsername, user.getUsername())
                    .set(SysUser::getUpdateTime, LocalDateTime.now())
                    .set(SysUser::getPassword, passwordEncoder.encode(rq.getNewPassword()))
                    .update();
            return Result.createSuccess();
        } else {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "旧密码错误");
        }
    }

    /**
     * 查询用户的菜单
     *
     * @param user 用户信息
     * @return 菜单
     */
    @GetMapping("/menu")
    public Result<List<UserMenuRs>> menu(@AuthenticationPrincipal User user) {
        List<MenuPermissionDto> menuList = menusService.userMenuList(user.getUsername());

        List<UserMenuRs> menuRsList = menuList.stream()
                .map(menuDto -> {

                    UserMenuRs menuRs = new UserMenuRs();
                    menuRs.setPath(menuDto.getMenuRouter());
                    menuRs.setMenuId(menuDto.getId());

                    UserMenuMetaRs metaRs = new UserMenuMetaRs();
                    metaRs.setTitle(menuDto.getMenuName());
                    metaRs.setIcon(menuDto.getMenuIcon());
                    metaRs.setOrder(menuDto.getMenuOrder());
                    metaRs.setMenu(menuDto.getNavShow());

                    menuRs.setMeta(metaRs);
                    return menuRs;
                })
                .sorted(Comparator.comparing((rs) -> rs.getMeta().getOrder()))
                .toList();

        Map<Integer, UserMenuRs> rsMap = menuRsList
                .stream()
                .collect(Collectors.toMap(UserMenuRs::getMenuId, menuRs -> menuRs));

        for (MenuDto menuDto : menuList) {
            Integer id = menuDto.getId();
            Integer parentMenuId = menuDto.getParentMenuId();

            if (ObjectUtils.isNotEmpty(parentMenuId)) {
                UserMenuRs parentMenuRs = rsMap.get(parentMenuId);
                if (ObjectUtils.isNotEmpty(parentMenuRs)) {
                    parentMenuRs.addChildren(rsMap.get(id));
                }
            }
        }

        List<UserMenuRs> list = menuList.stream()
                .filter(menuDto -> ObjectUtils.isEmpty(menuDto.getParentMenuId()))
                .map(menuDto -> rsMap.get(menuDto.getId()))
                .toList();

        return Result.createSuccess(list);
    }


}
