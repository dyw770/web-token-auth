package cn.dyw.auth.support.access;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * 记录访问连接日志
 *
 * @author dyw770
 * @since 2025-04-27
 */
@Slf4j
public class SystemAccessFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            filterChain.doFilter(request, response);
            
            try {
                SecurityContext context = SecurityContextHolder.getDeferredContext().get();
            } catch (Exception e) {
                log.error("记录访问日志时发生异常，日志将放弃记录", e);
            }
        } catch (Exception e) {
            log.info("过滤器链执行时发生异常", e);
            throw e;
        }
    }
}
