package cn.dyw.auth.security.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author dyw770
 * @since 2025-06-03
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        SecurityAutoConfiguration.class,
        SecurityRedisAutoConfiguration.class,
})
public @interface EnableAuthCore {
}
