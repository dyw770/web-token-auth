package cn.dyw.engine.core.exec.executor;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exception.EngineExecException;
import cn.dyw.engine.core.exception.SqlException;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.exec.result.ExecNumberResult;
import cn.dyw.engine.core.sql.template.ITemplateEngine;
import cn.dyw.engine.core.sql.template.SqlSources;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author dyw770
 * @since 2026-03-17
 */
@Slf4j
public class DefaultEngineExecutor extends AbstractEngineExecutor {

    private final ExecContext context;

    public DefaultEngineExecutor(ExecContext context, Connection connection, ITemplateEngine templateEngine) {
        super(context.getConfiguration().getPlugins(), context, connection, templateEngine);
        this.context = context;
    }

    @Override
    public ExecResult exec() throws SqlException {
        ExecNumberResult result = new ExecNumberResult(context, 0);
        SqlSources sqlSources = sqlSources();

        log.debug("最终执行SQL: {}", sqlSources.render());

        try (PreparedStatement statement = sqlSources.prepareStatement(this)) {
            int totalLine = statement.executeUpdate();
            result.setNumber(totalLine);

            return result;
        } catch (SQLException e) {
            throw new EngineExecException("执行异常", e);
        }
    }
}
