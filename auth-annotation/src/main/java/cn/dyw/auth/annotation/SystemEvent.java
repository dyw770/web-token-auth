package cn.dyw.auth.annotation;

import java.lang.annotation.*;

/**
 * 记录日志接口
 *
 * @author dyw770
 * @since 2025-04-26
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface SystemEvent {

    String module() default "";

    /**
     * 日志模版，使用 {x} 表示可变参数
     * <p>
     * 使用 MessageFormat 填充模版
     *
     * @return 日志模版
     */
    String message() default "";

    /**
     * spel 表达式
     * 每个spel表达式返回一个参数，用来填充模版
     *
     * @return spel 表达式
     */
    String[] execute() default {};

    /**
     * 异常时执行的spel表达式
     *
     * @return spel 表示式
     */
    String throwable() default "";
}
