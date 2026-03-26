package cn.dyw.engine.core.context;

/**
 * 执行器上下文
 *
 * @author dyw770
 * @since 2021-08-02
 */
public class UpdateExecContext extends ExecContext {

    public UpdateExecContext(String sqlTemplate) {
        setSqlTemplate(sqlTemplate);
        setStatementType(StatementType.update);
    }

}
