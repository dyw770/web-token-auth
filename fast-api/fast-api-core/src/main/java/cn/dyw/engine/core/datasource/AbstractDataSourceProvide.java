package cn.dyw.engine.core.datasource;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exception.EngineExecException;
import lombok.Getter;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 数据源提供者
 *
 * @author dyw770
 * @since 2026-04-03
 */
public abstract class AbstractDataSourceProvide<T extends DataSource> implements DataSourceProvide<T> {


    @Getter
    private final Map<String, T> dataSources;

    public AbstractDataSourceProvide() {
        this.dataSources = new ConcurrentHashMap<>();
    }

    @Override
    public T provide(ExecContext context) {
        String dataSource = context.getDataSource();
        if (!dataSources.containsKey(dataSource)) {
            throw new EngineExecException("未找到对应的数据源");
        }
        return dataSources.get(dataSource);
    }
}
