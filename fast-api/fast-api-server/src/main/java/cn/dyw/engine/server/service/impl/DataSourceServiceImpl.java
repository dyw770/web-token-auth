package cn.dyw.engine.server.service.impl;

import cn.dyw.auth.exception.ExtensionBusinessException;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.engine.server.configuration.DruidDataSourceProvide;
import cn.dyw.engine.server.db.domain.SysFastDataSource;
import cn.dyw.engine.server.db.service.ISysFastDataSourceService;
import cn.dyw.engine.server.message.rq.DataSourceCreateRq;
import cn.dyw.engine.server.message.rq.DataSourceEditRq;
import cn.dyw.engine.server.service.IDataSourceService;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author dyw770
 * @since 2026-04-07
 */
@Service
public class DataSourceServiceImpl implements IDataSourceService {

    private final DruidDataSourceProvide dataSourceProvide;

    private final ISysFastDataSourceService dataSourceService;

    public DataSourceServiceImpl(DruidDataSourceProvide dataSourceProvide,
                                 ISysFastDataSourceService dataSourceService) {
        this.dataSourceProvide = dataSourceProvide;
        this.dataSourceService = dataSourceService;
    }

    @Override
    public void add(DataSourceCreateRq rq) {
        SysFastDataSource dataSource = new SysFastDataSource();
        BeanUtils.copyProperties(rq, dataSource);
        dataSource.setCreateTime(LocalDateTime.now());
        dataSource.setUpdateTime(LocalDateTime.now());
        dataSourceService.save(dataSource);
        refresh(rq.getSourceName());
    }

    @Override
    public void remove(String name) {
        SysFastDataSource dataSource = dataSourceService.getById(name);
        if (ObjectUtils.isEmpty(dataSource)) {
            throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "数据源不存在");
        }
        dataSourceService.removeById(name);
        dataSourceProvide.remove(name);
    }

    @Override
    public void update(DataSourceEditRq rq) {
        SysFastDataSource dataSource = dataSourceService.getById(rq.getSourceName());
        if (ObjectUtils.isEmpty(dataSource)) {
            throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "数据源不存在");
        }
        BeanUtils.copyProperties(rq, dataSource);
        dataSource.setUpdateTime(LocalDateTime.now());
        dataSourceService.updateById(dataSource);
        refresh(rq.getSourceName());
    }

    @Override
    public void refresh(String name) {
        SysFastDataSource dataSource = dataSourceService.getById(name);
        if (ObjectUtils.isEmpty(dataSource)) {
            return;
        }
        DruidDataSource source = dataSourceProvide.create(dataSource);
        try {
            source.init();
        } catch (SQLException e) {
            throw new ExtensionBusinessException(MessageCode.ERROR, "数据源初始化失败: " + e.getMessage());
        }
        dataSourceProvide.remove(dataSource.getSourceName());
        dataSourceProvide.add(dataSource.getSourceName(), source);

    }

    @Override
    public List<SysFastDataSource> list() {
        return dataSourceService.list();
    }
}
