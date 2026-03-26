package cn.dyw.engine.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.engine.db.domain.SysFastSql;
import cn.dyw.engine.db.service.ISysFastSqlService;
import cn.dyw.engine.message.rq.SqlCreateRq;
import cn.dyw.engine.message.rq.SqlEditRq;
import cn.dyw.engine.message.rq.SqlSearchRq;
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

    public FastSqlController(ISysFastSqlService sysFastSqlService) {
        this.sysFastSqlService = sysFastSqlService;
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
}