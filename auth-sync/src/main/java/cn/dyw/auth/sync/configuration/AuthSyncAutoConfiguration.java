package cn.dyw.auth.sync.configuration;

import cn.dyw.auth.event.AuthChangedApplicationEvent;
import cn.dyw.auth.event.AuthChangedHandler;
import cn.dyw.auth.sync.Constants;
import cn.dyw.auth.sync.RedisAuthChangedApplicationListener;
import cn.dyw.auth.sync.RedisAuthChangedMessageAdapter;
import cn.dyw.auth.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author dyw770
 * @since 2025-06-09
 */
@Configuration
@ConditionalOnProperty(prefix = "app.auth.jdbc", name = "enable-jdbc-api-auth", havingValue = "true")
public class AuthSyncAutoConfiguration {
   
    @Bean
    public RedisAuthChangedApplicationListener redisAuthChangedApplicationListener(RedisTemplate<String, ?> redisTemplate) {
        return new RedisAuthChangedApplicationListener(redisTemplate);
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(AuthChangedHandler handler) {
   
        ObjectMapper objectMapper = ObjectMapperUtils.createRedisObjectMapper();

        Jackson2JsonRedisSerializer<AuthChangedApplicationEvent> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, AuthChangedApplicationEvent.class);
        
        MessageListenerAdapter handleMessage = new MessageListenerAdapter(new RedisAuthChangedMessageAdapter(handler), "handleMessage");
        handleMessage.setSerializer(jackson2JsonRedisSerializer);
        return handleMessage;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, MessageListenerAdapter listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, ChannelTopic.of(Constants.REDIS_PUBLISH));
        return container;
    }
}
