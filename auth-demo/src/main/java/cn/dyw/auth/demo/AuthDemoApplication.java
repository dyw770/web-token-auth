package cn.dyw.auth.demo;

import cn.dyw.auth.cache.configuration.EnableAuthCache;
import cn.dyw.auth.cache.configuration.RedisCacheAutoConfiguration;
import cn.dyw.auth.db.configuration.EnableJdbcAuth;
import cn.dyw.auth.security.configuration.EnableAuthCore;
import cn.dyw.auth.support.system.EnableSystemEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@EnableJdbcAuth
@EnableAuthCore
@EnableAuthCache
@EnableSystemEvent
@SpringBootApplication
@Import(RedisCacheAutoConfiguration.class)
public class AuthDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthDemoApplication.class, args);
    }

}
