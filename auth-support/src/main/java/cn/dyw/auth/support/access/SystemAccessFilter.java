package cn.dyw.auth.support.access;

import cn.dyw.auth.support.SystemAccessHandler;
import cn.dyw.auth.support.SystemAccessModel;
import cn.dyw.auth.utils.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * 记录访问连接日志
 *
 * @author dyw770
 * @since 2025-04-27
 */
@Slf4j
public class SystemAccessFilter extends OncePerRequestFilter {

    private static final String ANONYMOUS_USER = "anonymousUser";

    @Getter
    @Setter
    private SystemAccessHandler systemAccessHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LocalDateTime startTime = LocalDateTime.now();
        String username = getUsername();
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.info("过滤器链执行时发生异常", e);
            throw e;
        } finally {
            LocalDateTime endTime = LocalDateTime.now();
            accessLog(request, response, startTime, endTime, username);
        }
    }

    private void accessLog(HttpServletRequest request,
                           HttpServletResponse response,
                           LocalDateTime startTime,
                           LocalDateTime endTime,
                           String username) {
        try {
            String url = request.getRequestURL().toString();
            String method = request.getMethod();
            Long duration = Duration.between(startTime, endTime).getSeconds();
            String accessIp = RequestUtils.getClientIp(request);
            String accessUa = request.getHeader(HttpHeaders.USER_AGENT);
            String accessResultType = response.getContentType();
            // TODO 暂未实现记录这个值
            int accessResultCode = 0;
            int accessResponseCode = response.getStatus();

            log.debug("记录访问日志，username={}," +
                            "url={},method={}," +
                            "duration={},accessIp={}," +
                            "accessUa={},accessResultType={}," +
                            "accessResultCode={},accessResponseCode={}",
                    username, url, method, duration, accessIp, accessUa, accessResultType, accessResultCode, accessResponseCode);

            systemAccessHandler.handle(new SystemAccessModel(
                    username,
                    url,
                    method,
                    startTime,
                    duration.intValue(),
                    accessIp,
                    accessUa,
                    accessResultType,
                    accessResultCode,
                    accessResponseCode
            ));

        } catch (Exception e) {
            log.error("记录访问日志时发生异常，日志将放弃记录", e);
        }
    }

    private String getUsername() {
        SecurityContext context = SecurityContextHolder.getDeferredContext().get();
        if (ObjectUtils.isNotEmpty(context) && ObjectUtils.isNotEmpty(context.getAuthentication())) {
            return context.getAuthentication().getName();
        }
        return ANONYMOUS_USER;
    }
}
