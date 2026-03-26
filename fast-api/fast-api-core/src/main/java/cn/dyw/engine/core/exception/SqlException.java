package cn.dyw.engine.core.exception;

/**
 * SQL 异常
 *
 * @author dyw770
 * @since 2022-09-19
 */
public class SqlException extends EngineException {

    public SqlException(String message) {
        super(message);
    }

    public SqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
