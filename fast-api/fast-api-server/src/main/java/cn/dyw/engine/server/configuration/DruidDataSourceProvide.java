package cn.dyw.engine.server.configuration;

import cn.dyw.engine.core.datasource.AbstractDataSourceProvide;
import cn.dyw.engine.server.db.domain.SysFastDataSource;
import cn.dyw.engine.server.db.service.ISysFastDataSourceService;
import com.alibaba.druid.pool.DruidDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author dyw770
 * @since 2026-04-07
 */
@Slf4j
@Component
public class DruidDataSourceProvide extends AbstractDataSourceProvide<DruidDataSource> {

    private final ISysFastDataSourceService dataSourceService;

    public DruidDataSourceProvide(ISysFastDataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @Override
    public void add(String name, DruidDataSource dataSource) {
        this.getDataSources().put(name, dataSource);
    }

    @Override
    public void remove(String name) {
        DruidDataSource source = this.getDataSources().remove(name);
        source.close();
    }
    
    public DruidDataSource create(SysFastDataSource dataSource) {
        DruidDataSource source = new DruidDataSource();
        source.setUrl(dataSource.getJdbcUrl());
        source.setUsername(dataSource.getUsername());
        source.setPassword(dataSource.getPassword());
        source.setDriverClassName(dataSource.getDriverName());
        source.setDbType(dataSource.getDbType());

        Properties properties = new Properties();
        try {
            if (StringUtils.isNotBlank(dataSource.getProperties())) {
                properties.load(new StringReader(dataSource.getProperties()));
            }
        } catch (IOException e) {
            throw new RuntimeException("数据源：" + dataSource.getSourceName() + " 加载数据源其他配置失败", e);
        }
        source.configFromPropeties(properties);
        
        return source;
    }

    @Override
    @PostConstruct
    public void init() {
        List<SysFastDataSource> list = dataSourceService.list();
        for (SysFastDataSource dataSource : list) {
            DruidDataSource source = this.create(dataSource);
            try {
                source.init();
                this.add(dataSource.getSourceName(), source);
            } catch (SQLException e) {
                log.error("数据源：{} 初始化失败", dataSource.getSourceName(), e);
            }
        }
    }

    @Override
    @PreDestroy
    public void close() {
        for (DruidDataSource dataSource : this.getDataSources().values()) {
            dataSource.close();
        }
    }
}
