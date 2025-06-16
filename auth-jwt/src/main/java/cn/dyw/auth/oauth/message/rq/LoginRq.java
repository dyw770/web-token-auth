package cn.dyw.auth.oauth.message.rq;

import jakarta.validation.constraints.NotBlank;

/**
 * @param username 用户名
 * @param password 密码
 * @author dyw770
 * @since 2025-02-11
 */
public record LoginRq(
        @NotBlank
        String username,
        @NotBlank
        String password
) {

}
