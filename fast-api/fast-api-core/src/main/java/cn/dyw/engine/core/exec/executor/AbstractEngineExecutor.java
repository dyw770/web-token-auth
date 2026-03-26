package cn.dyw.engine.core.exec.executor;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exception.ExecSqlException;
import cn.dyw.engine.core.exception.SqlException;
import cn.dyw.engine.core.exec.EngineExecutor;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.exec.plugin.DefaultExecPluginChain;
import cn.dyw.engine.core.exec.plugin.ExecPluginChain;
import cn.dyw.engine.core.exec.plugin.SqlExecPlugin;
import cn.dyw.engine.core.sql.template.ITemplateEngine;
import cn.dyw.engine.core.sql.template.SqlSources;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author dyw770
 * @since 2022-09-29
 */
@Slf4j
public abstract class AbstractEngineExecutor implements EngineExecutor {

    private final List<SqlExecPlugin> sqlExecPlugins;

    private final ExecContext context;

    private final Connection connection;

    private final ITemplateEngine templateEngine;

    private SqlSources sqlSources;

    public AbstractEngineExecutor(List<SqlExecPlugin> sqlExecPlugins, ExecContext context,
                                  Connection connection, ITemplateEngine templateEngine) {
        this.sqlExecPlugins = sqlExecPlugins;
        this.context = context;
        this.connection = connection;
        this.templateEngine = templateEngine;
        this.sqlSources = templateEngine.process(context);
    }

    @Override
    public ExecResult execWithPlugin() throws SqlException {
        ExecPluginChain chain = createPluginChain();
        return chain.doChain();
    }

    @Override
    public PreparedStatement createStatement(String sql) throws SqlException {
        try {
            log.debug("创建 statement sql：{}", sql);
            return this.connection.prepareStatement(sql);
        } catch (SQLException e) {
            log.debug("预处理SQL失败, {}", sql);
            throw new ExecSqlException("预处理SQL失败， " + sql, e);
        }
    }

    @Override
    public ITemplateEngine templateEngine() {
        return templateEngine;
    }

    @Override
    public void applyContext() {
        this.sqlSources = templateEngine.process(context);
    }

    @Override
    public SqlSources sqlSources() {
        return sqlSources;
    }

    public ExecPluginChain createPluginChain() {
        return new DefaultExecPluginChain(this.sqlExecPlugins, this.context, this);
    }

}
