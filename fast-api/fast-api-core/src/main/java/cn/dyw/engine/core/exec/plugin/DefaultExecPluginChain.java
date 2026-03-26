package cn.dyw.engine.core.exec.plugin;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exec.EngineExecutor;
import cn.dyw.engine.core.exec.ExecResult;
import lombok.Getter;

import java.util.List;

/**
 * 默认实现
 *
 * @author dyw770
 * @since 2022-10-16
 */
public class DefaultExecPluginChain implements ExecPluginChain {

    @Getter
    private final List<SqlExecPlugin> execPlugins;

    private int nowExecPluginIndex = 0;

    @Getter
    private final ExecContext context;

    @Getter
    private final EngineExecutor executor;

    public DefaultExecPluginChain(List<SqlExecPlugin> execPlugins, ExecContext context, EngineExecutor executor) {
        this.execPlugins = execPlugins;
        this.context = context;
        this.executor = executor;
    }

    @Override
    public ExecResult doChain() {
        // 没有插件， 直接返回执行器结果
        if (this.execPlugins.isEmpty()) {
            return this.executor.exec();
        }
        // 插件执行链完成 没有插件阻止了最后得执行
        if (this.nowExecPluginIndex == this.execPlugins.size()) {
            return this.executor.exec();
        }
        // 继续执行插件
        this.nowExecPluginIndex++;
        SqlExecPlugin plugin = this.execPlugins.get(this.nowExecPluginIndex - 1);
        if (plugin.canSupport(getContext(), getExecutor(), this)) {
            return plugin.doExec(getContext(), getExecutor(), this);
        } else {
            return this.doChain();
        }
    }
}
