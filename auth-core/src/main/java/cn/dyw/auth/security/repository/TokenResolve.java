package cn.dyw.auth.security.repository;

import cn.dyw.auth.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * token 解析器
 *
 * @author dyw770
 * @since 2025-02-14
 */
public interface TokenResolve {

    /**
     * 解析token
     *
     * @param request 请求
     * @return token
     */
    String tokenResolve(HttpServletRequest request);

    /**
     * 生成token
     *
     * @param token 用户信息
     * @return token
     */
    Token createToken(Authentication token);

    /**
     * 从token字符串中解析出用户ID
     *
     * @param token token
     * @return 用户ID
     */
    String parseUser(String token);

    /**
     * 校验token是否是本机生成
     *
     * @param token token
     * @return 结果
     */
    boolean checkToken(String token);
}
