package cn.dyw.engine.server.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.server.message.rq.ExecSqlRq;
import cn.dyw.engine.server.service.IQueryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 执行接口
 *
 * @author dyw770
 * @since 2026-03-27
 */
@RestController
@RequestMapping("${app.fast-api.api-context-path:/query}")
public class FastExecController {

    private final IQueryService queryService;

    public FastExecController(IQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * 执行SQL
     *
     * @param rq 请求参数
     * @return 结果
     */
    @PostMapping("/sql")
    public Result<ExecResult> query(ExecSqlRq rq) {
        return Result.createSuccess(queryService.execQuery(rq));
    }

}
