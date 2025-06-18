package cn.dyw.auth.jwt.controller;

import cn.dyw.auth.jwt.message.rq.LoginRq;
import cn.dyw.auth.jwt.security.JwtLoginLogoutHandler;
import cn.dyw.auth.jwt.security.JwtTokenAuthenticationToken;
import cn.dyw.auth.jwt.token.JwtToken;
import cn.dyw.auth.message.Result;
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


    private final JwtLoginLogoutHandler loginLogoutHandler;

    public UserController(JwtLoginLogoutHandler loginLogoutHandler) {
        this.loginLogoutHandler = loginLogoutHandler;
    }

    /**
     * 登录
     *
     * @param loginRq 账号和密码
     * @return 结果
     */
    @PostMapping("/login")
    public Result<JwtToken> login(@RequestBody @Validated LoginRq loginRq, HttpServletRequest request) {
        JwtTokenAuthenticationToken login = loginLogoutHandler.login(loginRq.username(), loginRq.password(), request);
        return Result.createSuccess("登录成功", login.getJwtToken());
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
