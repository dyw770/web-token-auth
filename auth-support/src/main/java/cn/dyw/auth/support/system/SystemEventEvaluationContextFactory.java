package cn.dyw.auth.support.system;

import lombok.Setter;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.function.SingletonSupplier;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * EvaluationContext 工厂
 * <p>
 * 该代码来自 spring-context
 *
 * @author dyw770
 * @since 2025-04-26
 */
public class SystemEventEvaluationContextFactory {

    private final StandardEvaluationContext originalContext;

    @Setter
    @Nullable
    private Supplier<ParameterNameDiscoverer> parameterNameDiscoverer;

    SystemEventEvaluationContextFactory(StandardEvaluationContext originalContext) {
        this.originalContext = originalContext;
    }

    public ParameterNameDiscoverer getParameterNameDiscoverer() {
        if (this.parameterNameDiscoverer == null) {
            this.parameterNameDiscoverer = SingletonSupplier.of(new DefaultParameterNameDiscoverer());
        }
        return this.parameterNameDiscoverer.get();
    }

    /**
     * Creates a {@link SystemEventEvaluationContext} for the specified operation.
     *
     * @param rootObject   the {@code root} object to use for the context
     * @param targetMethod the target cache {@link Method}
     * @param args         the arguments of the method invocation
     * @return a context suitable for this cache operation
     */
    public SystemEventEvaluationContext forOperation(SystemEventExpressionRootObject rootObject,
                                               Method targetMethod, Object[] args) {

        SystemEventEvaluationContext evaluationContext = new SystemEventEvaluationContext(
                rootObject, targetMethod, args, getParameterNameDiscoverer());
        this.originalContext.applyDelegatesTo(evaluationContext);
        return evaluationContext;
    }
}
