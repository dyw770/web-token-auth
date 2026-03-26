package cn.dyw.engine.core.exception;

/**
 *
 * @author dyw770
 * @since 2026-03-17
 */
public class EngineException extends RuntimeException {

    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
