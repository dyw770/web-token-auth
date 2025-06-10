package cn.dyw.auth.db.event;

import cn.dyw.auth.db.security.JdbcAuthorizationManager;
import cn.dyw.auth.event.AuthChangedApplicationEvent;
import cn.dyw.auth.event.AuthChangedHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认授权改变事件处理
 *
 * @author dyw770
 * @since 2025-06-09
 */
@Slf4j
public class DefaultAuthChangedHandler implements AuthChangedHandler {

    private final JdbcAuthorizationManager authorizationManager;

    public DefaultAuthChangedHandler(JdbcAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public void handle(AuthChangedApplicationEvent event) {
        log.info("授权数据发生改变，将刷新数据: {}", event.getSource());
        authorizationManager.refresh();
    }
}
