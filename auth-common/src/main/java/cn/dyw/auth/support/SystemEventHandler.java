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
     */
    void handle(SystemEventModel model);
}
