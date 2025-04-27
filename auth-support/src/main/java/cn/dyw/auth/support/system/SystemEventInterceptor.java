package cn.dyw.auth.support.system;


import cn.dyw.auth.annotation.SystemEvent;
import cn.dyw.auth.support.SystemEventHandler;
import cn.dyw.auth.support.SystemEventModel;
import cn.dyw.auth.support.expression.AnnotatedElementKey;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
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

    @Getter
    @Setter
    private SystemEventHandler systemEventHandler;

    public SystemEventInterceptor() {
        systemEventHandler = new DefaultSystemEventHandler();
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
            try {
                execute(target, method, invocation.getArguments(), ex);
            } catch (Exception e) {
                log.error("执行system event aop 拦截器逻辑时发生异常", e);
            }
            throw ex;
        }
        try {
            execute(result, target, method, invocation.getArguments());
        } catch (Exception e) {
            log.error("执行system event aop 拦截器逻辑时发生异常", e);
        }

        return result;
    }

    /**
     * 处理结果
     *
     * @param result 目标方法返回结果
     * @param target 目标对象
     * @param method 目标方法
     * @param args   目标方法参数
     */
    protected void execute(Object result, Object target, Method method, Object[] args) {

        AnnotationInfo annotationInfo = getAnnotationInfo(target, method);
        if (annotationInfo == null) return;


        EvaluationContext context;
        if (ObjectUtils.isEmpty(result) || method.getReturnType().isAssignableFrom(void.class)) {
            context = expressionEvaluator.createEvaluationContext(
                    method, 
                    args, 
                    target,
                    annotationInfo.targetClass, 
                    annotationInfo.targetMethod, 
                    SystemEventExpressionEvaluator.NO_RESULT
            );
        } else {
            context = expressionEvaluator.createEvaluationContext(
                    method, 
                    args, 
                    target,
                    annotationInfo.targetClass,
                    annotationInfo.targetMethod, 
                    result
            );
        }

        String[] execute = annotationInfo.systemEventModel.args();
        if (ArrayUtils.isEmpty(execute)) {
            systemEventHandler.handle(annotationInfo.systemEventModel, new Object[0]);
            return;
        }

        Object[] objects = Stream.of(execute)
                .filter(StringUtils::isNotBlank)
                .map(expression ->
                        expressionEvaluator.execute(
                                expression, new AnnotatedElementKey(annotationInfo.targetMethod, annotationInfo.targetClass), context)
                )
                .toArray(Object[]::new);

        systemEventHandler.handle(annotationInfo.systemEventModel, objects);
    }

    /**
     * 处理异常
     *
     * @param target    目标对象
     * @param method    目标方法
     * @param args      目标方法参数
     * @param throwable 异常
     */
    protected void execute(Object target, Method method, Object[] args, Throwable throwable) {

        AnnotationInfo result = getAnnotationInfo(target, method);
        if (result == null) return;

        EvaluationContext context = expressionEvaluator.createEvaluationContext(method, args, target, result.targetClass(), result.targetMethod(), throwable);

        String exception = result.annotation().throwable();
        if (StringUtils.isEmpty(exception)) {
            systemEventHandler.handleThrowable(result.systemEventModel(), throwable, exception);
            return;
        }

        Object execute = expressionEvaluator.execute(exception, new AnnotatedElementKey(result.targetMethod(), result.targetClass()), context);
        systemEventHandler.handleThrowable(result.systemEventModel(), throwable, execute);
    }

    private AnnotationInfo getAnnotationInfo(Object target, Method method) {
        // Check whether aspect is enabled (to cope with cases where the AJ is pulled in automatically)
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        Method targetMethod = (!Proxy.isProxyClass(targetClass) ?
                AopUtils.getMostSpecificMethod(method, targetClass) : method);

        SystemEvent annotation = AnnotatedElementUtils.getMergedAnnotation(method, SystemEvent.class);

        if (ObjectUtils.isEmpty(annotation)) {
            return null;
        }

        SystemEventModel systemEventModel = new SystemEventModel(
                annotation.message(),
                annotation.module(),
                annotation.execute(),
                annotation.throwable()
        );
        return new AnnotationInfo(targetClass, targetMethod, annotation, systemEventModel);
    }

    private record AnnotationInfo(Class<?> targetClass, Method targetMethod, SystemEvent annotation,
                                  SystemEventModel systemEventModel) {
    }

    @Override
    public void setBeanFactory(@Nullable BeanFactory beanFactory) throws BeansException {
        if (beanFactory != null) {
            this.originalEvaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
    }
}
