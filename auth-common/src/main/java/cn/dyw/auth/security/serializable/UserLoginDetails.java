package cn.dyw.auth.security.serializable;

import cn.dyw.auth.token.Token;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * token 序列化类
 *
 * @param token          token
 * @param username       用户名
 * @param createTime     创建时间
 * @param loginUserAgent ua
 * @param loginIp        ip
 * @author dyw770
 * @since 2025-05-22
 */
public record UserLoginDetails(
        Token token,
        String username,
        LocalDateTime createTime,
        String loginUserAgent,
        String loginIp
) implements Serializable {


}
