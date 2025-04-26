package cn.dyw.auth.support.expression;

import org.springframework.expression.EvaluationException;

/**
 * spel 使用未定义变量
 * <p>
 * 该代码来自 spring-cache
 *
 * @author dyw770
 * @since 2025-04-26
 */
public class VariableNotAvailableException extends EvaluationException {

    public VariableNotAvailableException(String name) {
        super("Variable '" + name + "' not available");
    }
}
