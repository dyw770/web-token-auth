package cn.dyw.engine.server.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.engine.server.db.domain.SysFastApi;
import cn.dyw.engine.server.db.service.ISysFastApiService;
import cn.dyw.engine.server.message.rq.ApiCreateRq;
import cn.dyw.engine.server.message.rq.ApiEditRq;
import cn.dyw.engine.server.message.rq.ApiSearchRq;
import cn.dyw.engine.server.model.FastApi;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * API 管理控制器
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Validated
@RestController
@RequestMapping("${app.fast-api.api-context-path:/api}")
public class FastApiController {

    private final ISysFastApiService sysFastApiService;

    public FastApiController(ISysFastApiService sysFastApiService) {
        this.sysFastApiService = sysFastApiService;
    }

    /**
     * 查询API列表
     *
     * @param rq 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    public Result<Page<SysFastApi>> list(@RequestBody @Validated ApiSearchRq rq) {
        return Result.createSuccess(sysFastApiService.queryList(rq));
    }

    /**
     * 查询API详情列表
     *
     * @param rq 查询参数
     * @return 分页结果
     */
    @PostMapping("/details/list")
    public Result<Page<FastApi>> detailsList(@RequestBody @Validated ApiSearchRq rq) {
        return Result.createSuccess(sysFastApiService.queryDetailsList(rq));
    }

    /**
     * 添加API
     *
     * @param rq 创建参数
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Void> addApi(@RequestBody @Validated ApiCreateRq rq) {
        sysFastApiService.addApi(rq);
        return Result.createSuccess();
    }

    /**
     * 编辑API
     *
     * @param rq 编辑参数
     * @return 操作结果
     */
    @PutMapping("/edit")
    public Result<Void> editApi(@RequestBody @Validated ApiEditRq rq) {
        sysFastApiService.updateApi(rq);
        return Result.createSuccess();
    }

    /**
     * 删除API
     *
     * @param id API ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteApi(@PathVariable @NotNull Integer id) {
        sysFastApiService.deleteApi(id);
        return Result.createSuccess();
    }
}