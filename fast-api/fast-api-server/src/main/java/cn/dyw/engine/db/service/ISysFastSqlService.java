package cn.dyw.engine.db.service;

import cn.dyw.engine.db.domain.SysFastSql;
import cn.dyw.engine.message.rq.SqlSearchRq;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyw770
 * @since 2026-03-25
 */
public interface ISysFastSqlService extends IService<SysFastSql> {


    /**
     * 列表
     *
     * @param rq 查询参数
     * @return 列表
     */
    Page<SysFastSql> queryList(SqlSearchRq rq);
}
