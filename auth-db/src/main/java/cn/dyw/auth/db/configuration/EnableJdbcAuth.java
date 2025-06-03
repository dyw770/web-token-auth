package cn.dyw.auth.db.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 导入jdbc授权
 *
 * @author dyw770
 * @since 2025-06-03
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AuthJdbcAutoConfiguration.class)
public @interface EnableJdbcAuth {
}
