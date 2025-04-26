package cn.dyw.auth.support.system;

import cn.dyw.auth.support.expression.MethodBasedEvaluationContext;
import cn.dyw.auth.support.expression.VariableNotAvailableException;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dyw770
 * @since 2025-04-26
 */
public class SystemEventEvaluationContext extends MethodBasedEvaluationContext {

    private final Set<String> unavailableVariables = new HashSet<>(1);
    

    public SystemEventEvaluationContext(Object rootObject,
                                        Method method,
                                        Object[] arguments,
                                        ParameterNameDiscoverer parameterNameDiscoverer) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
    }

    public void addUnavailableVariable(String name) {
        this.unavailableVariables.add(name);
    }

    @Override
    @Nullable
    public Object lookupVariable(String name) {
        if (this.unavailableVariables.contains(name)) {
            throw new VariableNotAvailableException(name);
        }
        return super.lookupVariable(name);
    }
}
