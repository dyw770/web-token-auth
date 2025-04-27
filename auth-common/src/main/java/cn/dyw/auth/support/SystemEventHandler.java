package cn.dyw.auth.support;

/**
 * @author dyw770
 * @since 2025-04-27
 */
public interface SystemEventHandler {

    /**
     * 处理 SystemEvent 注解
     *
     * @param model 注解信息
     * @param args  解析后的参数
     */
    void handle(SystemEventModel model, Object[] args);

    /**
     * 发生异常时调用
     *
     * @param model     注解信息
     * @param throwable 异常
     * @param args      解析后的参数
     */
    void handleThrowable(SystemEventModel model, Throwable throwable, Object args);
}
