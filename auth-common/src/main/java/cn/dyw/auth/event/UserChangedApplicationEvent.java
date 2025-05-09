package cn.dyw.auth.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 授权资源变动事件
 *
 * @author dyw770
 * @since 2025-04-27
 */
public class UserChangedApplicationEvent extends ApplicationEvent {
    
    @Getter
    private final String username;

    public UserChangedApplicationEvent(String source) {
        super(source);
        this.username = source;
    }

    public UserChangedApplicationEvent(String source, Clock clock) {
        super(source, clock);
        this.username = source;
    }
    
    
}
