package cn.dyw.auth.annotation;

/**
 * 记录日志接口
 *
 * @author dyw770
 * @since 2025-04-26
 */
public @interface SystemEvent {

    String module() default "";
    
    /**
     * 日志模版，使用 {} 表示可变参数
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
}
