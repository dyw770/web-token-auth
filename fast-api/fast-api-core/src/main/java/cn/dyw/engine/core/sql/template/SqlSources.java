package cn.dyw.engine.core.sql.template;

import cn.dyw.engine.core.exception.SqlException;
import cn.dyw.engine.core.exec.EngineExecutor;

import java.sql.PreparedStatement;

/**
 * Sql
 *
 * @author dyw770
 * @since 2022-09-26
 */
public interface SqlSources {

    /**
     * 渲染SQL
     *
     * @return 结果
     * @throws SqlException 异常
     */
    String render();

    /**
     * 返回最初始渲染后的SQL
     *
     * @return 结果
     */
    String original();

    /**
     * 改变SQL
     *
     * @param sql SQL
     */
    void apply(String sql);

    /**
     * 预处理SQL
     *
     * @param executor 连接
     * @return 结果
     * @throws SqlException 异常
     */
    PreparedStatement prepareStatement(EngineExecutor executor) throws SqlException;

    /**
     * 预处理SQL
     *
     * @param sql      sql
     * @param executor 连接
     * @return 结果
     * @throws SqlException 异常
     */
    PreparedStatement prepareStatement(String sql, EngineExecutor executor) throws SqlException;

}
