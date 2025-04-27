package cn.dyw.auth.db.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 授权资源变动事件
 *
 * @author dyw770
 * @since 2025-04-27
 */
public class AuthChangedApplicationEvent extends ApplicationEvent {

    public AuthChangedApplicationEvent(Object source) {
        super(source);
    }

    public AuthChangedApplicationEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
