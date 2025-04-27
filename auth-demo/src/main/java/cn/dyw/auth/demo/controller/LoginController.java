package cn.dyw.auth.demo.controller;

import cn.dyw.auth.annotation.SystemEvent;
import cn.dyw.auth.demo.message.rq.LoginRq;
import cn.dyw.auth.message.Result;
import cn.dyw.auth.security.LoginLogoutHandler;
import cn.dyw.auth.security.TokenAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@RestController
@RequestMapping("/")
public class LoginController {

    private final LoginLogoutHandler loginLogoutHandler;

    public LoginController(LoginLogoutHandler loginLogoutHandler) {
        this.loginLogoutHandler = loginLogoutHandler;
    }

    @PostMapping("/login")
    @SystemEvent(message = "用户 {0} 登陆结果 {1}", module = "user", execute = {"#p0.username", "#result.msg"}, throwable = "'用户 ' + #p0.username + ' 登陆失败：' + #throwable.message")
    public Result<String> login(@RequestBody LoginRq loginRq) {
        TokenAuthenticationToken login = loginLogoutHandler.login(loginRq.username(), loginRq.password());
        return Result.createSuccess("登录成功", login.getToken());
    }

    @GetMapping("/logout")
    public Result<String> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        loginLogoutHandler.logout(authentication, request, response);
        return Result.createSuccess("登出成功", "bbbb");
    }

}
