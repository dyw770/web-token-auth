package cn.dyw.engine.core.exec;

import cn.dyw.engine.core.context.*;
import cn.dyw.engine.core.datasource.DataSourceProvide;
import cn.dyw.engine.core.exception.EngineExecException;
import cn.dyw.engine.core.exec.executor.DeleteEngineExecutor;
import cn.dyw.engine.core.exec.executor.InsertEngineExecutor;
import cn.dyw.engine.core.exec.executor.SelectEngineExecutor;
import cn.dyw.engine.core.exec.executor.UpdateEngineExecutor;
import cn.dyw.engine.core.sql.template.ITemplateEngine;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 默认实现执行引擎
 *
 * @author dyw770
 * @since 2021-08-03
 */
@Slf4j
public class DefaultExecEngine extends AbstractExecEngine {

    @Getter
    private final ITemplateEngine templateEngine;

    public DefaultExecEngine(DataSourceProvide<? extends DataSource> dataSourceProvide, ITemplateEngine templateEngine) {
        super(dataSourceProvide);
        this.templateEngine = templateEngine;
    }

    @Override
    protected EngineExecutor executor(ExecContext context, Connection connection) {
        EngineExecutor executor = null;

        if (context instanceof SelectExecContext) {
            executor = new SelectEngineExecutor((SelectExecContext) context, connection, getTemplateEngine());
        } else if (context instanceof UpdateExecContext) {
            executor = new UpdateEngineExecutor((UpdateExecContext) context, connection, getTemplateEngine());
        } else if (context instanceof DeleteExecContext) {
            executor = new DeleteEngineExecutor((DeleteExecContext) context, connection, getTemplateEngine());
        } else if (context instanceof InsertExecContext) {
            executor = new InsertEngineExecutor((InsertExecContext) context, connection, getTemplateEngine());
        }

        if (executor == null) {
            throw new EngineExecException("无法找到该上下文对应的执行器");
        }

        return executor;
    }

}
