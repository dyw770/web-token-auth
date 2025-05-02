package cn.dyw.auth.demo.controller;

import cn.dyw.auth.annotation.SystemEvent;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysMenusService;
import cn.dyw.auth.db.service.ISysUserService;
import cn.dyw.auth.demo.message.rq.LoginRq;
import cn.dyw.auth.demo.message.rs.UserInfoRs;
import cn.dyw.auth.demo.message.rs.UserMenuMetaRs;
import cn.dyw.auth.demo.message.rs.UserMenuRs;
import cn.dyw.auth.message.Result;
import cn.dyw.auth.security.LoginLogoutHandler;
import cn.dyw.auth.security.TokenAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class UserController {

    private final LoginLogoutHandler loginLogoutHandler;

    private final ISysUserService userService;

    private final ISysMenusService menusService;

    public UserController(LoginLogoutHandler loginLogoutHandler, ISysUserService userService, ISysMenusService menusService) {
        this.loginLogoutHandler = loginLogoutHandler;
        this.userService = userService;
        this.menusService = menusService;
    }

    /**
     * 登录
     *
     * @param loginRq 账号和密码
     * @return 结果
     */
    @PostMapping("/login")
    @SystemEvent(message = "用户 {0} 登陆结果 {1}", module = "user", execute = {"#p0.username", "#result.msg"}, throwable = "'用户 ' + #p0.username + ' 登陆失败：' + #throwable.message")
    public Result<String> login(@RequestBody @Validated LoginRq loginRq) {
        TokenAuthenticationToken login = loginLogoutHandler.login(loginRq.username(), loginRq.password());
        return Result.createSuccess("登录成功", login.getToken());
    }

    /**
     * 登出
     *
     * @param authentication 授权信息
     * @param request        request
     * @param response       response
     * @return 结果
     */
    @GetMapping("/logout")
    public Result<String> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        loginLogoutHandler.logout(authentication, request, response);
        return Result.createSuccess("登出成功");
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

    /**
     * 查询用户的菜单
     *
     * @param user 用户信息
     * @return 菜单
     */
    @GetMapping("/menu")
    public Result<List<UserMenuRs>> menu(@AuthenticationPrincipal User user) {
        List<MenuDto> menuList = menusService.userMenuList(user.getUsername());

        List<UserMenuRs> menuRsList = menuList.stream()
                .map(menuDto -> {
                    UserMenuRs menuRs = new UserMenuRs();
                    menuRs.setPath(menuDto.getMenuRouter());
                    menuRs.setMenuId(menuDto.getId());
                    UserMenuMetaRs metaRs = new UserMenuMetaRs();
                    metaRs.setTitle(menuDto.getMenuName());
                    metaRs.setIcon(menuDto.getMenuIcon());
                    metaRs.setOrder(menuDto.getMenuOrder());
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
