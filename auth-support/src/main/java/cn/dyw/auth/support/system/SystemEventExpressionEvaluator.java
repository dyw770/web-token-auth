package cn.dyw.auth.support.system;

import cn.dyw.auth.support.expression.AnnotatedElementKey;
import cn.dyw.auth.support.expression.ExpressionKey;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dyw770
 * @since 2025-04-26
 */
public class SystemEventExpressionEvaluator {

    /**
     * Indicate that there is no result variable.
     */
    public static final Object NO_RESULT = new Object();

    /**
     * Indicate that the result variable cannot be used at all.
     */
    public static final Object RESULT_UNAVAILABLE = new Object();

    /**
     * The name of the variable holding the result object.
     */
    public static final String RESULT_VARIABLE = "result";
    
    private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);

    private final SystemEventEvaluationContextFactory evaluationContextFactory;

    private final SpelExpressionParser parser;

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public SystemEventExpressionEvaluator(SystemEventEvaluationContextFactory evaluationContextFactory) {
        this.parser = new SpelExpressionParser();
        this.evaluationContextFactory = evaluationContextFactory;
        this.evaluationContextFactory.setParameterNameDiscoverer(() -> this.parameterNameDiscoverer);
    }

    /**
     * Create an {@link EvaluationContext}.
     *
     * @param method      the method
     * @param args        the method arguments
     * @param target      the target object
     * @param targetClass the target class
     * @param result      the return value (can be {@code null}) or
     *                    {@link #NO_RESULT} if there is no return at this time
     * @return the evaluation context
     */
    public EvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Class<?> targetClass, Method targetMethod,
                                                     @Nullable Object result) {

        SystemEventExpressionRootObject rootObject = new SystemEventExpressionRootObject(
                method, args, target, targetClass);
        SystemEventEvaluationContext evaluationContext = this.evaluationContextFactory
                .forOperation(rootObject, targetMethod, args);
        if (result == RESULT_UNAVAILABLE) {
            evaluationContext.addUnavailableVariable(RESULT_VARIABLE);
        } else if (result != NO_RESULT) {
            evaluationContext.setVariable(RESULT_VARIABLE, result);
        }
        return evaluationContext;
    }

    @Nullable
    public Object execute(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
    }


    /**
     * Return the parsed {@link Expression} for the specified SpEL expression.
     * <p>{@linkplain #parseExpression(String) Parses} the expression if it hasn't
     * already been parsed and cached.
     *
     * @param cache      the cache to use
     * @param elementKey the {@code AnnotatedElementKey} containing the element
     *                   on which the expression is defined
     * @param expression the expression to parse
     */
    protected Expression getExpression(Map<ExpressionKey, Expression> cache,
                                       AnnotatedElementKey elementKey, String expression) {

        ExpressionKey expressionKey = createKey(elementKey, expression);
        return cache.computeIfAbsent(expressionKey, key -> parseExpression(expression));
    }

    /**
     * Parse the specified {@code expression}.
     *
     * @param expression the expression to parse
     * @since 5.3.13
     */
    protected Expression parseExpression(String expression) {
        return this.parser.parseExpression(expression);
    }

    private ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
        return new ExpressionKey(elementKey, expression);
    }

    /**
     * Clear all caches.
     */
    void clear() {
        this.keyCache.clear();
    }
}
