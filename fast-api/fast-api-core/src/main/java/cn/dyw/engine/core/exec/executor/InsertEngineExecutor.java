package cn.dyw.engine.core.exec.executor;

import cn.dyw.engine.core.context.InsertExecContext;
import cn.dyw.engine.core.sql.template.ITemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

/**
 * @author dyw770
 * @since 2022-09-22
 */
@Slf4j
public class InsertEngineExecutor extends DefaultEngineExecutor {

    public InsertEngineExecutor(InsertExecContext context, Connection connection, ITemplateEngine templateEngine) {
        super(context, connection, templateEngine);
    }
}
