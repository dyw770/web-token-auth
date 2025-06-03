package cn.dyw.auth.support.system;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author dyw770
 * @since 2025-06-03
 */
@Aspect
public class SystemEventAspect {

    private final SystemEventInterceptor interceptor;

    public SystemEventAspect(SystemEventInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Around("@annotation(cn.dyw.auth.annotation.SystemEvent)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        JoinPointMethodInvocation adapter = new JoinPointMethodInvocation(joinPoint);
        return interceptor.invoke(adapter);
    }
}
