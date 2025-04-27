package cn.dyw.auth.support.system;


import cn.dyw.auth.annotation.SystemEvent;
import cn.dyw.auth.support.expression.AnnotatedElementKey;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.stream.Stream;

/**
 * SystemEvent注解 aop 拦截器
 *
 * @author dyw770
 * @since 2025-04-27
 */
@Slf4j
public class SystemEventInterceptor implements MethodInterceptor, BeanFactoryAware {

    private final SystemEventExpressionEvaluator expressionEvaluator;

    private final StandardEvaluationContext originalEvaluationContext = new StandardEvaluationContext();

    private final SystemEventHandler systemEventHandler;
    
    private final StopWatch stopWatch = new StopWatch();

    public SystemEventInterceptor(SystemEventHandler systemEventHandler) {
        this.systemEventHandler = systemEventHandler;
        expressionEvaluator = new SystemEventExpressionEvaluator(
                new SystemEventEvaluationContextFactory(this.originalEvaluationContext)
        );
    }

    @Override
    @Nullable
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();

        Object target = invocation.getThis();
        Assert.state(target != null, "Target must not be null");

        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable ex) {
            log.debug("system event aop 拦截器执行方法时异常", ex);
            throw ex;
        }
        try {
            stopWatch.start("System Event Log");
            execute(result, target, method, invocation.getArguments());
            stopWatch.stop();
            if (log.isDebugEnabled()) {
                log.debug("SystemEvent 注解处理耗时\n{}", stopWatch.prettyPrint());
            }
        } catch (Exception e) {
            log.debug("执行system event aop 拦截器逻辑时发生异常", e);
        }

        return result;
    }


    protected void execute(Object result, Object target, Method method, Object[] args) {

        // Check whether aspect is enabled (to cope with cases where the AJ is pulled in automatically)
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        Method targetMethod = (!Proxy.isProxyClass(targetClass) ?
                AopUtils.getMostSpecificMethod(method, targetClass) : method);

        SystemEvent annotation = AnnotatedElementUtils.getMergedAnnotation(method, SystemEvent.class);

        if (ObjectUtils.isEmpty(annotation)) {
            return;
        }
        EvaluationContext context;
        if (ObjectUtils.isEmpty(result) || method.getReturnType().isAssignableFrom(void.class)) {
            context = expressionEvaluator.createEvaluationContext(method, args, target, targetClass, targetMethod, SystemEventExpressionEvaluator.NO_RESULT);
        } else {
            context = expressionEvaluator.createEvaluationContext(method, args, target, targetClass, targetMethod, result);
        }

        String[] execute = annotation.execute();

        Object[] objects = Stream.of(execute)
                .filter(StringUtils::isNotBlank)
                .map(expression ->
                        expressionEvaluator.execute(
                                expression, new AnnotatedElementKey(targetMethod, targetClass), context)
                )
                .toArray(Object[]::new);

        systemEventHandler.handle(new SystemEventModel(annotation.message(), annotation.module(), objects));
    }

    @Override
    public void setBeanFactory(@Nullable BeanFactory beanFactory) throws BeansException {
        if (beanFactory != null) {
            this.originalEvaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
    }
}
