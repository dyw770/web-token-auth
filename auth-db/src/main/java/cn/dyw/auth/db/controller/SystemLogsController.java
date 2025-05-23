package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysApiAccessLog;
import cn.dyw.auth.db.domain.SysSystemOperationLog;
import cn.dyw.auth.db.message.rq.ApiAccessLogSearchRq;
import cn.dyw.auth.db.message.rq.SystemOperationSearchRq;
import cn.dyw.auth.db.service.ISysApiAccessLogService;
import cn.dyw.auth.db.service.ISysSystemOperationLogService;
import cn.dyw.auth.message.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dyw770
 * @since 2025-05-18
 */
@Validated
@RestController
@RequestMapping("${app.auth.api-context-path:/admin}/logs")
public class SystemLogsController {

    private final ISysSystemOperationLogService systemOperationLogService;

    private final ISysApiAccessLogService apiAccessLogService;

    public SystemLogsController(ISysSystemOperationLogService systemOperationLogService, ISysApiAccessLogService apiAccessLogService) {
        this.systemOperationLogService = systemOperationLogService;
        this.apiAccessLogService = apiAccessLogService;
    }

    /**
     * 查询系统操作日志
     *
     * @param rq 请求
     * @return 结果
     */
    @PostMapping("/event")
    public Result<Page<SysSystemOperationLog>> systemEventLog(@RequestBody @Validated SystemOperationSearchRq rq) {
        
        Page<SysSystemOperationLog> page = systemOperationLogService.lambdaQuery()
                .eq(StringUtils.isNotBlank(rq.getOperationEvent()), SysSystemOperationLog::getOperationEvent, rq.getOperationEvent())
                .eq(StringUtils.isNotBlank(rq.getUsername()), SysSystemOperationLog::getUsername, rq.getUsername())
                .ge(ObjectUtils.isNotEmpty(rq.getStartTime()), SysSystemOperationLog::getOperationTime, rq.getStartTime())
                .le(ObjectUtils.isNotEmpty(rq.getEndTime()), SysSystemOperationLog::getOperationTime, rq.getEndTime())
                .orderByDesc(SysSystemOperationLog::getOperationTime)
                .page(rq.toPage());

        return Result.createSuccess(page);
    }

    /**
     * 查询api访问日志
     *
     * @param rq 请求参数
     * @return 结果
     */
    @PostMapping("/access")
    public Result<Page<SysApiAccessLog>> apiAccessLog(@RequestBody @Validated ApiAccessLogSearchRq rq) {
        
        Page<SysApiAccessLog> page = apiAccessLogService
                .lambdaQuery()
                .likeRight(StringUtils.isNotBlank(rq.getApiPath()), SysApiAccessLog::getApiPath, rq.getApiPath())
                .eq(StringUtils.isNotBlank(rq.getApiAccessIp()), SysApiAccessLog::getApiAccessIp, rq.getApiAccessIp())
                .eq(StringUtils.isNotBlank(rq.getUsername()), SysApiAccessLog::getUsername, rq.getUsername())
                .ge(ObjectUtils.isNotEmpty(rq.getStartTime()), SysApiAccessLog::getApiAccessTime, rq.getStartTime())
                .le(ObjectUtils.isNotEmpty(rq.getEndTime()), SysApiAccessLog::getApiAccessTime, rq.getEndTime())
                .orderByDesc(SysApiAccessLog::getApiAccessTime)
                .page(rq.toPage());
        return Result.createSuccess(page);
    }
}
