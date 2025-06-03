package cn.dyw.auth.support.system;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author dyw770
 * @since 2025-06-03
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SystemEventBeanConfiguration.class)
public @interface EnableSystemEvent {
}
