package cn.dyw.auth.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author dyw770
 * @since 2025-04-28
 */
public final class RequestUtils {

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For可能包含多个IP地址，取第一个非unknown的有效IP地址
            for (String candidate : ip.split(",")) {
                candidate = candidate.trim();
                if (!"unknown".equalsIgnoreCase(candidate)) {
                    return candidate;
                }
            }
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 如果以上头信息中都没有获取到有效IP，则直接从请求中获取远程地址
        ip = request.getRemoteAddr();
        return ip;
    }
}
