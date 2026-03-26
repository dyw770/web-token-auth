package cn.dyw.engine.core.exec.plugin;

import cn.dyw.engine.core.exec.ExecResult;

/**
 * @author dyw770
 * @since 2022-10-16
 */
public interface ExecPluginChain {

    /**
     * 执行插件链
     *
     * @return 结果
     */
    ExecResult doChain();
}
