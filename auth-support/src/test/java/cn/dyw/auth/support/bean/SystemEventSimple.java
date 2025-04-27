package cn.dyw.auth.support.bean;

import cn.dyw.auth.annotation.SystemEvent;
import org.springframework.stereotype.Component;

/**
 * @author dyw770
 * @since 2025-04-27
 */
@Component
public class SystemEventSimple {
    
    @SystemEvent(message = "用户 {0} 操作了 {1}, Bean测试：{2}", module = "info", execute = {"#p1", "#p0", "@systemEventSimple"})
    public void logEvent(String message, String user) {
        System.out.println("logEvent: " + message + " by " + user);
    }
}
