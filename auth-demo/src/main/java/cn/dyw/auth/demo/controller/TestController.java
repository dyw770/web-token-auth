package cn.dyw.auth.demo.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.auth.security.LoginLogoutHandler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    private final LoginLogoutHandler loginLogoutHandler;

    public TestController(LoginLogoutHandler loginLogoutHandler) {
        this.loginLogoutHandler = loginLogoutHandler;
    }

    /**
     * 测试
     * @param user 登陆信息
     * @return 结果
     */
    @GetMapping("/login")
    public Result<UserDetails> login(@AuthenticationPrincipal UserDetails user) {
        return Result.createSuccess("当前登陆", user);
    }
    

}
