package cn.dyw.auth.support.expression;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.AnnotatedElement;

/**
 * 注解key的, 唯一标识一个注解
 * <p>
 * 该代码来自 spring-context
 *
 * @author dyw770
 * @since 2025-04-26
 */
public class AnnotatedElementKey implements Comparable<AnnotatedElementKey> {

    private final AnnotatedElement element;

    @Nullable
    private final Class<?> targetClass;


    /**
     * Create a new instance with the specified {@link AnnotatedElement} and
     * optional target {@link Class}.
     */
    public AnnotatedElementKey(AnnotatedElement element, @Nullable Class<?> targetClass) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        this.element = element;
        this.targetClass = targetClass;
    }


    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof AnnotatedElementKey that &&
                this.element.equals(that.element) &&
                ObjectUtils.nullSafeEquals(this.targetClass, that.targetClass)));
    }

    @Override
    public int hashCode() {
        return this.element.hashCode() + (this.targetClass != null ? this.targetClass.hashCode() * 29 : 0);
    }

    @Override
    public String toString() {
        return this.element + (this.targetClass != null ? " on " + this.targetClass : "");
    }

    @Override
    public int compareTo(AnnotatedElementKey other) {
        int result = this.element.toString().compareTo(other.element.toString());
        if (result == 0 && this.targetClass != null) {
            if (other.targetClass == null) {
                return 1;
            }
            result = this.targetClass.getName().compareTo(other.targetClass.getName());
        }
        return result;
    }
}
