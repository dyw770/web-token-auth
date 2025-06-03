package cn.dyw.auth.security.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-18
 */
@RestController
@RequestMapping("${app.auth.api-context-path:/admin}/user")
public class UserManageSupportController {

    private final SecurityTokenRepository tokenRepository;

    public UserManageSupportController(SecurityTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * 获取用户所有的token
     *
     * @param username 用户名
     * @return tokens
     */
    @GetMapping("/tokens")
    public Result<List<UserLoginDetails>> tokens(@RequestParam("username") String username) {
        return Result.createSuccess(tokenRepository.listUserTokens(username));
    }

    /**
     * 强制用户下线
     *
     * @param token 用户token
     * @return 结果
     */
    @GetMapping("/offline")
    public Result<Void> offline(@RequestParam("token") String token) {
        tokenRepository.expireToken(token);
        return Result.createSuccess();
    }
}
