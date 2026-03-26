package cn.dyw.engine.core.exec.plugin;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exec.EngineExecutor;
import cn.dyw.engine.core.exec.ExecResult;

/**
 * 插件
 *
 * @author dyw770
 * @since 2022-09-27
 */
public interface SqlExecPlugin {


    /**
     * 执行
     *
     * @param context  上下文
     * @param executor 执行器
     * @param chain    链
     * @return 结果
     */
    ExecResult doExec(ExecContext context, EngineExecutor executor, ExecPluginChain chain);

    /**
     * 能否处理
     *
     * @param context  上下文
     * @param executor 执行器
     * @param chain    链
     * @return 是否支持处理
     */
    boolean canSupport(ExecContext context, EngineExecutor executor, ExecPluginChain chain);
}
