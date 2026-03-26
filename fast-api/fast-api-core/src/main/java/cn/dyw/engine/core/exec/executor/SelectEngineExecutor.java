package cn.dyw.engine.core.exec.executor;

import cn.dyw.engine.core.context.SelectExecContext;
import cn.dyw.engine.core.exception.EngineExecException;
import cn.dyw.engine.core.exception.SqlException;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.exec.result.DataSetResult;
import cn.dyw.engine.core.sql.template.ITemplateEngine;
import cn.dyw.engine.core.sql.template.SqlSources;
import cn.dyw.engine.core.utils.DatabaseResultSetUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dyw770
 * @since 2022-09-22
 */
@Slf4j
public class SelectEngineExecutor extends AbstractEngineExecutor {

    private final SelectExecContext context;

    public SelectEngineExecutor(SelectExecContext context, Connection connection, ITemplateEngine templateEngine) {
        super(context.getConfiguration().getPlugins(), context, connection, templateEngine);
        this.context = context;
    }

    @Override
    public ExecResult exec() throws SqlException {

        DataSetResult result = new DataSetResult(context);
        SqlSources sqlSources = sqlSources();

        log.debug("最终执行SQL: {}", sqlSources.render());
        try (PreparedStatement statement = sqlSources.prepareStatement(this);
             ResultSet resultSet = statement.executeQuery()) {

            DatabaseResultSetUtils.handleResultSet(resultSet, result);

            return result;
        } catch (SQLException e) {
            throw new EngineExecException("执行异常", e);
        }
    }

}

