package cn.dyw.engine.server.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.engine.server.db.domain.SysFastDataSource;
import cn.dyw.engine.server.message.rq.DataSourceCreateRq;
import cn.dyw.engine.server.message.rq.DataSourceEditRq;
import cn.dyw.engine.server.service.IDataSourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源管理控制器
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Validated
@RestController
@RequestMapping("${app.fast-api.api-context-path:/data/source}")
public class FastDataSourceController {

    private final IDataSourceService dataSourceService;

    public FastDataSourceController(IDataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    /**
     * 查询数据源列表
     *
     * @return 数据源列表
     */
    @GetMapping("/list")
    public Result<List<SysFastDataSource>> list() {
        return Result.createSuccess(dataSourceService.list());
    }

    /**
     * 添加数据源
     *
     * @param rq 创建参数
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated DataSourceCreateRq rq) {
        dataSourceService.add(rq);
        return Result.createSuccess();
    }

    /**
     * 编辑数据源
     *
     * @param rq 编辑参数
     * @return 操作结果
     */
    @PutMapping("/edit")
    public Result<Void> edit(@RequestBody @Validated DataSourceEditRq rq) {
        dataSourceService.update(rq);
        return Result.createSuccess();
    }

    /**
     * 删除数据源
     *
     * @param name 数据源名称
     * @return 操作结果
     */
    @DeleteMapping("/delete/{name}")
    public Result<Void> delete(@PathVariable String name) {
        dataSourceService.remove(name);
        return Result.createSuccess();
    }

    /**
     * 刷新数据源
     *
     * @param name 数据源名称
     * @return 操作结果
     */
    @GetMapping("/refresh/{name}")
    public Result<Void> refresh(@PathVariable String name) {
        dataSourceService.refresh(name);
        return Result.createSuccess();
    }
}
