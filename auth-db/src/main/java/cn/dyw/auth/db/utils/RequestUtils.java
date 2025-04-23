package cn.dyw.auth.db.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.UrlUtils;

/**
 * 请求工具类
 * 
 * @author dyw770
 * @since 2025-04-23
 */
public final class RequestUtils {

    public static String requestLine(HttpServletRequest request) {
        return request.getMethod() + " " + UrlUtils.buildRequestUrl(request);
    }
}
