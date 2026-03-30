package cn.dyw.engine.server.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.server.db.domain.SysFastSql;
import cn.dyw.engine.server.db.service.ISysFastSqlService;
import cn.dyw.engine.server.message.rq.ExecParameterRq;
import cn.dyw.engine.server.message.rq.SqlCreateRq;
import cn.dyw.engine.server.message.rq.SqlEditRq;
import cn.dyw.engine.server.message.rq.SqlSearchRq;
import cn.dyw.engine.server.service.IQueryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * SQL模板管理控制器
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Validated
@RestController
@RequestMapping("${app.fast-api.api-context-path:/sql}")
public class FastSqlController {

    private final ISysFastSqlService sysFastSqlService;
    
    private final IQueryService queryService;

    public FastSqlController(ISysFastSqlService sysFastSqlService, IQueryService queryService) {
        this.sysFastSqlService = sysFastSqlService;
        this.queryService = queryService;
    }

    /**
     * 查询SQL模板列表
     *
     * @param rq 查询参数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<Page<SysFastSql>> list(@RequestBody @Validated SqlSearchRq rq) {
        return Result.createSuccess(sysFastSqlService.queryList(rq));
    }

    /**
     * 添加SQL模板
     *
     * @param rq 创建参数
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Void> addSql(@RequestBody @Validated SqlCreateRq rq) {
        sysFastSqlService.addSql(rq);
        return Result.createSuccess();
    }

    /**
     * 编辑SQL模板
     *
     * @param rq 编辑参数
     * @return 操作结果
     */
    @PutMapping("/edit")
    public Result<Void> editSql(@RequestBody @Validated SqlEditRq rq) {
        sysFastSqlService.updateSql(rq);
        return Result.createSuccess();
    }

    /**
     * 删除SQL模板
     *
     * @param id 模板ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteSql(@PathVariable @NotNull @Min(1) Integer id) {
        sysFastSqlService.deleteSql(id);
        return Result.createSuccess();
    }

    /**
     * 执行SQL模板
     *
     * @param rq 执行参数
     * @return 执行结果
     */
    @PostMapping("/exec/{sqlId}")
    public Result<ExecResult> execSql(@RequestBody @Validated ExecParameterRq rq, @PathVariable int sqlId) {
        ExecResult result = queryService.execSql(rq, sqlId);
        return Result.createSuccess(result);
    }
}