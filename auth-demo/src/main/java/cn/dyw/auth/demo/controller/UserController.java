package cn.dyw.auth.demo.controller;

import cn.dyw.auth.annotation.SystemEvent;
import cn.dyw.auth.demo.message.rq.LoginRq;
import cn.dyw.auth.message.Result;
import cn.dyw.auth.security.LoginLogoutHandler;
import cn.dyw.auth.security.TokenAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final LoginLogoutHandler loginLogoutHandler;


    public UserController(LoginLogoutHandler loginLogoutHandler) {
        this.loginLogoutHandler = loginLogoutHandler;
    }

    /**
     * 登录
     *
     * @param loginRq 账号和密码
     * @return 结果
     */
    @PostMapping("/login")
    @SystemEvent(message = "用户 {0} 登陆结果 {1}", module = "user", execute = {"#p0.username", "#result.msg"}, throwable = "'用户 ' + #p0.username + ' 登陆失败：' + #throwable.message")
    public Result<String> login(@RequestBody @Validated LoginRq loginRq, HttpServletRequest request) {
        TokenAuthenticationToken login = loginLogoutHandler.login(loginRq.username(), loginRq.password(), request);
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
}
