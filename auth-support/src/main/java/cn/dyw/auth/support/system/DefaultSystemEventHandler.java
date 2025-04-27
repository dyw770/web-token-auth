package cn.dyw.auth.support.system;

import cn.dyw.auth.support.SystemEventHandler;
import cn.dyw.auth.support.SystemEventModel;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * @author dyw770
 * @since 2025-04-27
 */
@Slf4j
public class DefaultSystemEventHandler implements SystemEventHandler {

    @Override
    public void handle(SystemEventModel model) {
        String format = MessageFormat.format(model.message(), model.args());
        log.debug("执行事件记录：\n{}", format);
    }
}
