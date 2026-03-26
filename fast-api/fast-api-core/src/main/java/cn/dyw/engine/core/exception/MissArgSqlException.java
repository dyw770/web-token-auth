package cn.dyw.engine.core.exception;

/**
 * 缺少参数
 *
 * @author dyw770
 * @since 2022-09-26
 */
public class MissArgSqlException extends RenderSqlException {

    public MissArgSqlException(String message) {
        super(message);
    }
}
