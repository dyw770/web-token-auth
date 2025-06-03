package cn.dyw.auth.support.access;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author dyw770
 * @since 2025-06-03
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SystemAccessBeanConfiguration.class)
public @interface EnableSystemAccess {
}
