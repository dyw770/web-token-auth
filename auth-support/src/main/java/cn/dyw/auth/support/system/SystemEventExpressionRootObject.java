package cn.dyw.auth.support.system;

import lombok.Getter;

import java.lang.reflect.Method;

/**
 * @author dyw770
 * @since 2025-04-26
 */
@Getter
public class SystemEventExpressionRootObject {

    private final Method method;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;

    public SystemEventExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass) {
        this.method = method;
        this.args = args;
        this.target = target;
        this.targetClass = targetClass;
    }
}
