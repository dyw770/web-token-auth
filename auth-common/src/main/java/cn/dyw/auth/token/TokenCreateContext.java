package cn.dyw.auth.token;

import lombok.Data;

/**
 * @author dyw770
 * @since 2025-06-17
 */
@Data
public class TokenCreateContext {

    /**
     * 用户名
     */
    private final String username;

    /**
     * 登录用户代理
     */
    private String loginUserAgent;

    /**
     * 登录IP
     */
    private String loginIp;


    public TokenCreateContext(String username) {
        this.username = username;
    }
}
