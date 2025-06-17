package cn.dyw.auth.token;

import java.time.LocalDateTime;

/**
 * @author dyw770
 * @since 2025-06-17
 */
public interface Token {
    
    String token();
    
    LocalDateTime createTime();
}
