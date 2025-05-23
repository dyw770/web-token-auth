package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.SpringSecurityCoreApplicationTests;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author dyw770
 * @since 2025-05-23
 */
@SpringBootTest(classes = SpringSecurityCoreApplicationTests.class)
public class RedisMapSecurityTokenRepositoryTest {


    /**
     * redis
     */
    @Autowired
    private RedisTemplate<String, UserLoginDetails> redisTemplate;
    

    @Test
    public void expireToken() {
        Boolean expire = redisTemplate.expire("test-key", 1000, TimeUnit.SECONDS);
        System.out.println(expire);
    }
}