package cn.dyw.auth.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.util.List;

/**
 * 授权资源变动事件
 *
 * @author dyw770
 * @since 2025-04-27
 */
public class AuthChangedApplicationEvent extends ApplicationEvent {

    public AuthChangedApplicationEvent(@JsonProperty("source") List<Integer> source) {
        super(source, Clock.systemDefaultZone());
    }
}
