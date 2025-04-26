package cn.dyw.auth.support.expression;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 表达式key, 用来唯一标识一个注解中的表达式
 * <p>
 * 该代码来自 spring-context
 *
 * @author dyw770
 * @since 2025-04-26
 */
public class ExpressionKey implements Comparable<ExpressionKey> {

    private final AnnotatedElementKey element;

    private final String expression;

    public ExpressionKey(AnnotatedElementKey element, String expression) {
        Assert.notNull(element, "AnnotatedElementKey must not be null");
        Assert.notNull(expression, "Expression must not be null");
        this.element = element;
        this.expression = expression;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof ExpressionKey that &&
                this.element.equals(that.element) && this.expression.equals(that.expression)));
    }

    @Override
    public int hashCode() {
        return this.element.hashCode() * 29 + this.expression.hashCode();
    }

    @Override
    public String toString() {
        return this.element + " with expression \"" + this.expression + "\"";
    }

    @Override
    public int compareTo(ExpressionKey other) {
        int result = this.element.toString().compareTo(other.element.toString());
        if (result == 0) {
            result = this.expression.compareTo(other.expression);
        }
        return result;
    }
}
