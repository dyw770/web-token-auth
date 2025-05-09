package cn.dyw.auth.security.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Clock;

/**
 * 授权资源变动事件
 *
 * @author dyw770
 * @since 2025-04-27
 */
public class UserChangedApplicationEvent extends ApplicationEvent {
    
    @Getter
    private final UserDetails userDetails;

    public UserChangedApplicationEvent(UserDetails source) {
        super(source);
        this.userDetails = source;
    }

    public UserChangedApplicationEvent(UserDetails source, Clock clock) {
        super(source, clock);
        this.userDetails = source;
    }
    
    
}
