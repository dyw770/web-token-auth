package cn.dyw.auth.cache.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用缓存配置
 *
 * @author dyw770
 * @since 2025-06-04
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisCacheAutoConfiguration.class)
public @interface EnableAuthCache {
}
