package cn.dyw.engine.core.exec;

import cn.dyw.engine.core.configuration.DataPageOption;
import cn.dyw.engine.core.exec.plugin.SqlExecPlugin;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局配置
 *
 * @author dyw770
 * @since 2022-09-27
 */
public class EngineConfiguration {

    @Getter
    private final List<SqlExecPlugin> plugins;

    /**
     * 在缺失分页参数时是否分页
     */
    @Getter
    @Setter
    private boolean missPage = false;

    @Setter
    @Getter
    private DataPageOption defaultPageArgs;

    public EngineConfiguration() {
        plugins = new ArrayList<>(16);
        defaultPageArgs = new DataPageOption();
        defaultPageArgs.setPage(1);
        defaultPageArgs.setSize(10);
        defaultPageArgs.setNeedPage(true);
    }

    public void addPlugins(SqlExecPlugin plugin) {
        plugins.add(plugin);
    }
}
