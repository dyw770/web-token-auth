package cn.dyw.auth.db.event;

import cn.dyw.auth.db.security.JdbcAuthorizationManager;
import cn.dyw.auth.event.AuthChangedApplicationEvent;
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
    
    private final JdbcAuthorizationManager authorizationManager;

    public AuthChangedApplicationListener(JdbcAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public void onApplicationEvent(@Nullable AuthChangedApplicationEvent event) {
        log.info("授权数据发生改变，将刷新全部数据");
        authorizationManager.refresh();
    }
}
