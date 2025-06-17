package cn.dyw.auth.jwt.token;

import cn.dyw.auth.token.Token;

import java.time.LocalDateTime;

/**
 * @author dyw770
 * @since 2025-06-17
 */
public record JwtToken(String token, String refreshToken, LocalDateTime createTime) implements Token {

}
