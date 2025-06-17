package cn.dyw.auth.jwt.token;

import cn.dyw.auth.token.Token;

/**
 * @author dyw770
 * @since 2025-06-17
 */
public record JwtToken(String token, String refreshToken) implements Token {

}
