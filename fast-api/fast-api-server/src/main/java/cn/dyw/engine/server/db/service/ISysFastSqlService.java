package cn.dyw.engine.server.db.service;

import cn.dyw.engine.server.db.domain.SysFastSql;
import cn.dyw.engine.server.message.rq.SqlCreateRq;
import cn.dyw.engine.server.message.rq.SqlEditRq;
import cn.dyw.engine.server.message.rq.SqlSearchRq;
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
    
    /**
     * 新增
     *
     * @param rq 新增参数
     */
    void addSql(SqlCreateRq rq);
    
    /**
     * 修改
     *
     * @param rq 修改参数
     */
    void updateSql(SqlEditRq rq);
    
    /**
     * 删除
     *
     * @param id 删除id
     */
    void deleteSql(Integer id);
}
