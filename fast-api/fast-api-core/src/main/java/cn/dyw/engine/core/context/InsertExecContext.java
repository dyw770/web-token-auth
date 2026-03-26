package cn.dyw.engine.core.context;

/**
 * 执行器上下文
 *
 * @author dyw770
 * @since 2021-08-02
 */
public class InsertExecContext extends ExecContext {

    public InsertExecContext(String sqlTemplate) {
        setSqlTemplate(sqlTemplate);
        setStatementType(StatementType.delete);
    }

}
