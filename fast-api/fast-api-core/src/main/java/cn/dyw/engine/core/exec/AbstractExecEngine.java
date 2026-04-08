package cn.dyw.engine.core.exec;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.datasource.DataSourceProvide;
import cn.dyw.engine.core.exception.DataSourceException;
import cn.dyw.engine.core.exception.ExecSqlException;
import cn.dyw.engine.core.exception.SqlException;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author dyw770
 * @since 2021-08-10
 */
@Slf4j
public abstract class AbstractExecEngine implements IExecEngine {

    private final DataSourceProvide<? extends DataSource> dataSourceProvide;

    public AbstractExecEngine(DataSourceProvide<? extends DataSource> dataSourceProvide) {
        this.dataSourceProvide = dataSourceProvide;
    }

    private Connection getConnection(ExecContext context) throws ExecSqlException {
        try {
            return dataSourceProvide.provide(context).getConnection();
        } catch (java.sql.SQLException e) {
            throw new ExecSqlException("获取数据库连接失败", e);
        }
    }

    @Override
    public ExecResult exec(ExecContext context)
            throws SqlException {
        try (Connection connection = getConnection(context)) {
            return execDataQuery(context, connection);
        } catch (java.sql.SQLException e) {
            throw new DataSourceException("关闭数据库连接异常", e);
        }
    }

    /**
     * 执行真正的查询
     *
     * @param context 参数
     */
    public ExecResult execDataQuery(ExecContext context, Connection connection) throws ExecSqlException {
        log.debug("待执行SQL模版: {}", context.getSqlTemplate());
        try {
            EngineExecutor executor = executor(context, connection);
            return executor.execWithPlugin();
        } catch (SqlException e) {
            log.error("执行SQL失败, SQL: [{}]", context.getSqlTemplate(), e);
            throw e;
        }
    }

    /**
     * 构建执行器
     *
     * @param context    上下文
     * @param connection 连接
     * @return 结果
     */
    protected abstract EngineExecutor executor(ExecContext context, Connection connection);
}
