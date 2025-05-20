package cn.dyw.auth.db.support;

import cn.dyw.auth.db.domain.SysSystemOperationLog;
import cn.dyw.auth.db.service.ISysSystemOperationLogService;
import cn.dyw.auth.support.SystemEventHandler;
import cn.dyw.auth.support.SystemEventModel;
import cn.dyw.auth.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;
import java.time.LocalDateTime;

/**
 * @author dyw770
 * @since 2025-05-18
 */
@Slf4j
@Service
public class JdbcSystemEventHandler implements SystemEventHandler {

    private final ISysSystemOperationLogService systemOperationLogService;

    public JdbcSystemEventHandler(ISysSystemOperationLogService systemOperationLogService) {
        this.systemOperationLogService = systemOperationLogService;
    }

    @Override
    public void handle(SystemEventModel model, Object[] args) {
        String format = MessageFormat.format(model.message(), args);
        SysSystemOperationLog log = createLog();
        log.setOperationEvent(model.module());
        log.setOperationContent(format);
        log.setOperationResultType("success");
        log.setOperationResultCode(0);
        systemOperationLogService.save(log);
    }

    @Override
    public void handleThrowable(SystemEventModel model, Throwable throwable, Object args) {
        SysSystemOperationLog log = createLog();
        log.setOperationEvent(model.module());
        log.setOperationContent(args.toString());
        log.setOperationResultType("fail");
        log.setOperationResultCode(-1);
        systemOperationLogService.save(log);
    }

    private SysSystemOperationLog createLog() {
        SysSystemOperationLog log = new SysSystemOperationLog();
        log.setUsername(getUsername());
        log.setOperationTime(LocalDateTime.now());
        log.setOperationIp(getRequestClientIp());
        log.setOperationUa(getRequestUserAgent());
        return log;
    }

    private String getUsername() {
        SecurityContext context = SecurityContextHolder.getDeferredContext().get();
        if (ObjectUtils.isNotEmpty(context) && ObjectUtils.isNotEmpty(context.getAuthentication())) {
            return context.getAuthentication().getName();
        }
        return "anonymousUser";
    }
    
    private String getRequestClientIp() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        if (ObjectUtils.isEmpty(request)) {
            return "";
        }
        return RequestUtils.getClientIp(request);
    }

    private String getRequestUserAgent() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        if (ObjectUtils.isEmpty(request)) {
            return "";
        } 
        return request.getHeader(HttpHeaders.USER_AGENT);
    }
}
