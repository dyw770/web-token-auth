package cn.dyw.engine.server.db.mapper;

import cn.dyw.engine.server.db.domain.SysFastApi;
import cn.dyw.engine.server.message.rq.ApiSearchRq;
import cn.dyw.engine.server.model.FastApi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2026-03-25
 */
@Mapper
public interface SysFastApiMapper extends BaseMapper<SysFastApi> {

    /**
     * 根据路径查询API
     *
     * @param path 路径
     * @return API
     */
    FastApi queryApiByPath(@Param("path") String path);

    /**
     * 查询API列表详细
     *
     * @param rq   参数
     * @param page 分页参数
     * @return 结果
     */
    Page<FastApi> queryDetailsApi(@Param("rq") ApiSearchRq rq, Page<FastApi> page);
}

