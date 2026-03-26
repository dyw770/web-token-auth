package cn.dyw.engine.core.exception;

/**
 * @author dyw770
 * @since 2022-09-21
 */
public class DataSourceException extends SqlException {

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
