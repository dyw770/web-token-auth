package cn.dyw.auth.demo.service;

import cn.dyw.auth.db.domain.SysApiAccessLog;
import cn.dyw.auth.db.service.ISysApiAccessLogService;
import cn.dyw.auth.support.SystemAccessHandler;
import cn.dyw.auth.support.SystemAccessModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author dyw770
 * @since 2025-05-18
 */
@Service
public class JdbcSystemAccessHandler implements SystemAccessHandler {
    
    private final ISysApiAccessLogService apiAccessLogService;

    public JdbcSystemAccessHandler(ISysApiAccessLogService apiAccessLogService) {
        this.apiAccessLogService = apiAccessLogService;
    }

    @Override
    public void handle(SystemAccessModel model) {
        SysApiAccessLog apiAccessLog = new SysApiAccessLog();
        BeanUtils.copyProperties(model, apiAccessLog);
        apiAccessLogService.save(apiAccessLog);
    }
}
