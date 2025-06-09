package cn.dyw.auth.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 授权资源变动事件
 *
 * @author dyw770
 * @since 2025-04-27
 */
public class AuthChangedApplicationEvent extends ApplicationEvent {

    public AuthChangedApplicationEvent(@JsonProperty("source") Object source) {
        super(source);
    }

    public AuthChangedApplicationEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
