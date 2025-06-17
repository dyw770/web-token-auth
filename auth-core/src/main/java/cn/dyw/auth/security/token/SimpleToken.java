package cn.dyw.auth.security.token;

import cn.dyw.auth.token.Token;

import java.time.LocalDateTime;

/**
 * @author dyw770
 * @since 2025-06-17
 */
public record SimpleToken(String token, LocalDateTime createTime) implements Token {
    
}
