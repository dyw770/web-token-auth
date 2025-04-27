package cn.dyw.auth.demo;

import cn.dyw.auth.db.configuration.AuthJdbcAutoConfiguration;
import cn.dyw.auth.security.configuration.SecurityAutoConfiguration;
import cn.dyw.auth.security.configuration.SecurityRedisAutoConfiguration;
import cn.dyw.auth.support.access.SystemAccessBeanConfiguration;
import cn.dyw.auth.support.system.SystemEventBeanConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AuthJdbcAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        SecurityRedisAutoConfiguration.class,
        SystemAccessBeanConfiguration.class,
        SystemEventBeanConfiguration.class
})
public class AuthDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthDemoApplication.class, args);
    }

}
