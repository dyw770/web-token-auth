package cn.dyw.auth.support.access;

import cn.dyw.auth.support.SystemAccessHandler;
import cn.dyw.auth.support.SystemAccessModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dyw770
 * @since 2025-04-27
 */
@Slf4j
public class DefaultSystemAccessHandler implements SystemAccessHandler {

    @Override
    public void handle(SystemAccessModel model) {
        log.debug("请求访问日志: {}", model);
    }
}
