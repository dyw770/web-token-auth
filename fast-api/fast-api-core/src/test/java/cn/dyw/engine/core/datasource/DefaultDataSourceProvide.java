package cn.dyw.engine.core.datasource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 *
 * @author dyw770
 * @since 2026-04-07
 */
public class DefaultDataSourceProvide extends AbstractDataSourceProvide<DruidDataSource> {

    @Override
    public void add(String name, DruidDataSource dataSource) {
        this.getDataSources().put(name, dataSource);
    }

    @Override
    public void remove(String name) {
        DruidDataSource source = this.getDataSources().remove(name);
        source.close();
    }

    @Override
    public void init() {

    }

    @Override
    public void close() {
        for (DruidDataSource dataSource : this.getDataSources().values()) {
            dataSource.close();
        }
    }
}
