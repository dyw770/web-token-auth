package cn.dyw.engine.core.exception;

/**
 * sql执行异常
 *
 * @author dyw770
 * @since 2022-09-19
 */
public class ExecSqlException extends SqlException {


    public ExecSqlException(String message) {
        super(message);
    }

    public ExecSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
