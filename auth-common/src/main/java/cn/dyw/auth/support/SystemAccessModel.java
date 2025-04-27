package cn.dyw.auth.support;

import java.time.LocalDateTime;

/**
 * 访问日志数据
 *
 * @author dyw770
 * @since 2025-04-27
 */
public record SystemAccessModel(String username, String apiPath, String method, LocalDateTime accessTime,
                                long durationTime, String accessIp, String accessUa, String accessResultType,
                                int accessResultCode, int accessResponseCode) {
}
