package cn.dyw.auth.db.event;

import cn.dyw.auth.event.AuthChangedApplicationEvent;
import cn.dyw.auth.event.AuthChangedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.Nullable;

/**
 * 监听授权数据变更
 *
 * @author dyw770
 * @since 2025-04-27
 */
@Slf4j
public class AuthChangedApplicationListener implements ApplicationListener<AuthChangedApplicationEvent> {

    private final AuthChangedHandler handler;

    public AuthChangedApplicationListener(AuthChangedHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onApplicationEvent(@Nullable AuthChangedApplicationEvent event) {
        handler.handle(event);
    }
}
