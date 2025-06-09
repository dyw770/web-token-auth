package cn.dyw.auth.sync;

import cn.dyw.auth.event.AuthChangedApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author dyw770
 * @since 2025-06-09
 */
@Slf4j
public class RedisAuthChangedApplicationListener implements ApplicationListener<AuthChangedApplicationEvent> {
    
    private final RedisTemplate<String, ?> redisTemplate;

    public RedisAuthChangedApplicationListener(RedisTemplate<String, ?> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onApplicationEvent(AuthChangedApplicationEvent event) {
        redisTemplate.convertAndSend(Constants.REDIS_PUBLISH, event);
    }
}
