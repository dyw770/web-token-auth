package cn.dyw.engine.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.engine.db.domain.SysFastSql;
import cn.dyw.engine.db.service.ISysFastSqlService;
import cn.dyw.engine.message.rq.SqlSearchRq;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Validated
@RestController
@RequestMapping("${app.auth.api-context-path:/sql}")
public class SqlController {

    private final ISysFastSqlService sysFastSqlService;

    public SqlController(ISysFastSqlService sysFastSqlService) {
        this.sysFastSqlService = sysFastSqlService;
    }

    /**
     * 查询sql模版列表
     *
     * @param rq 参数
     * @return 列表
     */
    @GetMapping("/list")
    public Result<Page<SysFastSql>> list(@RequestBody @Validated SqlSearchRq rq) {
        return Result.createSuccess(sysFastSqlService.queryList(rq));
    }
}
