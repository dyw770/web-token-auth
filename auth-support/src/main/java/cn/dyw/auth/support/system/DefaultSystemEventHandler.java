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
    public void handle(SystemEventModel model, Object[] args) {
        String format = MessageFormat.format(model.message(), args);
        log.debug("System Event 注解拦截：{}", format);
    }

    @Override
    public void handleThrowable(SystemEventModel model, Throwable throwable, Object args) {
        log.debug("System Event 注解异常时拦截：{}", args, throwable);
    }
}
