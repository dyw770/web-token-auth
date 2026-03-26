package cn.dyw.engine.core.exception;

/**
 * sql渲染异常
 *
 * @author dyw770
 * @since 2022-09-19
 */
public class RenderSqlException extends SqlException {

    public RenderSqlException(String message) {
        super(message);
    }

    public RenderSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
