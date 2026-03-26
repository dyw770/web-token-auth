package cn.dyw.engine.core.exec;


import cn.dyw.engine.core.exception.SqlException;
import cn.dyw.engine.core.sql.template.ITemplateEngine;
import cn.dyw.engine.core.sql.template.SqlSources;

import java.sql.PreparedStatement;

/**
 * @author dyw770
 * @since 2022-09-22
 */
public interface EngineExecutor {

    /**
     * 执行器
     *
     * @return 执行结果
     * @throws SqlException sql异常
     */
    ExecResult exec() throws SqlException;

    /**
     * 执行器
     *
     * @return 执行结果
     * @throws SqlException sql异常
     */
    ExecResult execWithPlugin() throws SqlException;

    /**
     * 创建执行
     *
     * @param sql sql
     * @return 结果
     * @throws SqlException 异常
     */
    PreparedStatement createStatement(String sql) throws SqlException;
    
    /**
     * 获取模板引擎
     *
     * @return 模板引擎
     */
    ITemplateEngine templateEngine();

    /**
     * 重新应用上下文
     */
    void applyContext();

    /**
     * 返回SQL
     *
     * @return SqlSources
     */
    SqlSources sqlSources();
}
