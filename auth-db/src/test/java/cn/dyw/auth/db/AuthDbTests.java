package cn.dyw.auth.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author dyw770
 * @since 2025-07-21
 */
@Configuration
@MapperScan(basePackages = "cn.dyw.auth.db.mapper")
@SpringBootApplication(scanBasePackageClasses = AuthDbTests.class)
public class AuthDbTests {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
