package cn.dyw.auth.security.event;

import cn.dyw.auth.event.UserChangedApplicationEvent;
import cn.dyw.auth.security.repository.SecurityTokenRepository;
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
public class UserChangedApplicationListener implements ApplicationListener<UserChangedApplicationEvent> {

    private final SecurityTokenRepository tokenRepository;

    public UserChangedApplicationListener(SecurityTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void onApplicationEvent(@Nullable UserChangedApplicationEvent event) {
        log.info("用户[{}]信息发生改变，将刷新用户缓存", event.getUsername());
        tokenRepository.refreshUser(event.getUsername());
    }
}
