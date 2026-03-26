package cn.dyw.engine.core.exception;

/**
 * 引擎执行异常
 *
 * @author dyw770
 * @since 2021-08-03
 */
public class EngineExecException extends EngineException {

    public EngineExecException(String message) {
        super(message);
    }

    public EngineExecException(String message, Throwable cause) {
        super(message, cause);
    }
}
