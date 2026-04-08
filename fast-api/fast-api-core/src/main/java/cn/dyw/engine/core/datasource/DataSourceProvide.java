package cn.dyw.engine.core.datasource;

import cn.dyw.engine.core.context.ExecContext;

import javax.sql.DataSource;

/**
 *
 * 数据源提供者
 *
 * @author dyw770
 * @since 2026-04-03
 */
public interface DataSourceProvide<T extends DataSource> {

    /**
     * 提供数据源
     *
     * @param context 执行上下文
     * @return 数据源
     */
    DataSource provide(ExecContext context);

    /**
     * 添加数据源
     *
     * @param name       数据源名称
     * @param dataSource 数据源
     */
    void add(String name, T dataSource);
    
    /**
     * 移除数据源
     *
     * @param name 数据源名称
     */
    void remove(String name);

    /**
     * 初始化数据源
     */
    void init();

    /**
     * 关闭数据源
     */
    void close();
}
