package cn.dyw.engine.server.service;

import cn.dyw.engine.server.db.domain.SysFastDataSource;
import cn.dyw.engine.server.message.rq.DataSourceCreateRq;
import cn.dyw.engine.server.message.rq.DataSourceEditRq;

import java.util.List;

/**
 *
 * @author dyw770
 * @since 2026-04-07
 */
public interface IDataSourceService {

    /**
     * 添加数据源
     *
     * @param rq rq
     */
     void add(DataSourceCreateRq rq);
     
     /**
      * 删除数据源
      *
      * @param name 数据源名称
      */
      void remove(String name);
      
      /**
      * 修改数据源
      *
      * @param rq rq
      */
      void update(DataSourceEditRq rq);
      
      /**
      * 刷新数据源
      *
      * @param name 数据源名称
      */
      void refresh(String name);
      
      /**
      * 查询数据源列表
      *
      * @return 数据源列表
      */
      List<SysFastDataSource> list();
}
