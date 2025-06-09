package cn.dyw.auth.sync;

import cn.dyw.auth.event.AuthChangedApplicationEvent;
import cn.dyw.auth.event.AuthChangedHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理redis中发布的消息
 *
 * @author dyw770
 * @since 2025-06-09
 */
@Slf4j
public class RedisAuthChangedMessageAdapter {

    private final AuthChangedHandler handler;

    public RedisAuthChangedMessageAdapter(AuthChangedHandler handler) {
        this.handler = handler;
    }

    public void handleMessage(AuthChangedApplicationEvent event) {
        handler.handle(event);
    }
}
