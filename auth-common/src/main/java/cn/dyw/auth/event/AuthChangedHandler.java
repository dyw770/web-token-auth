package cn.dyw.auth.event;

/**
 * 授权改变处理器
 * 
 * @author dyw770
 * @since 2025-06-09
 */
@FunctionalInterface
public interface AuthChangedHandler {
    
    void handle(AuthChangedApplicationEvent event);
}
