package cn.dyw.auth.db.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author dyw770
 * @since 2025-04-22
 */
@Configuration
@MapperScan(basePackages = "cn.dyw.auth.db.mapper")
@ComponentScan(basePackages = "cn.dyw.auth.db")
public class AuthJdbcAutoConfiguration {


}
