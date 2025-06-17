package cn.dyw.auth.security.repository;

import cn.dyw.auth.token.SimpleToken;
import cn.dyw.auth.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * @author dyw770
 * @since 2025-02-14
 */
public class RequestTokenResolve implements TokenResolve {

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String TOKEN_SPLIT = "@";


    /**
     * token header名称
     */
    private final String tokenHeaderName;

    public RequestTokenResolve(String tokenHeaderName) {
        this.tokenHeaderName = tokenHeaderName;
    }

    @Override
    public String tokenResolve(HttpServletRequest request) {
        // 获取Authorization头部的内容
        String authorizationHeader = request.getHeader(tokenHeaderName);

        // 判断Authorization头部是否为空以及是否以"Bearer "开头
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            // 返回令牌内容，去掉"Bearer "前缀
            return authorizationHeader.substring(7);
        }

        // 如果没有找到令牌，返回null或者你自定义的处理逻辑
        return "";
    }

    @Override
    public Token createToken(Authentication token) {
        String username = ((UserDetails) token.getPrincipal()).getUsername();
        String tokenStr = username + TOKEN_SPLIT + LocalDateTime.now();

        return new SimpleToken(Base64.getEncoder().encodeToString(tokenStr.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public String parseUser(String token) {
        byte[] decode = Base64.getDecoder().decode(token);
        String tmpStr = new String(decode, StandardCharsets.UTF_8);
        String[] split = tmpStr.split(TOKEN_SPLIT);
        if (split.length == 2) {
            return split[0];
        }
        throw new RuntimeException("无法从token中解析出用户ID");
    }

    @Override
    public boolean checkToken(String token) {
        try {
            byte[] decode = Base64.getDecoder().decode(token);
            String tmpStr = new String(decode, StandardCharsets.UTF_8);
            String[] split = tmpStr.split(TOKEN_SPLIT);
            return split.length == 2;
        } catch (Exception e) {
            return false;
        }
    }
}
