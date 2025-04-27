package cn.dyw.auth.support.system;

import java.lang.reflect.Method;

/**
 * @author dyw770
 * @since 2025-04-26
 */
public record SystemEventExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass) {

}
