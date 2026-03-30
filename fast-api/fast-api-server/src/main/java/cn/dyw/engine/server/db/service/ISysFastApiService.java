package cn.dyw.engine.server.db.service;

import cn.dyw.engine.server.db.domain.SysFastApi;
import cn.dyw.engine.server.message.rq.ApiCreateRq;
import cn.dyw.engine.server.message.rq.ApiEditRq;
import cn.dyw.engine.server.message.rq.ApiSearchRq;
import cn.dyw.engine.server.model.FastApi;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyw770
 * @since 2026-03-25
 */
public interface ISysFastApiService extends IService<SysFastApi> {

    /**
     * 查询API列表
     *
     * @param rq 查询参数
     * @return 分页结果
     */
    Page<SysFastApi> queryList(ApiSearchRq rq);

    /**
     * 新增API
     *
     * @param rq 新增参数
     */
    void addApi(ApiCreateRq rq);

    /**
     * 修改API
     *
     * @param rq 修改参数
     */
    void updateApi(ApiEditRq rq);

    /**
     * 删除API
     *
     * @param id 删除id
     */
    void deleteApi(Integer id);
    
    /**
     * 根据SQL ID删除API
     *
     * @param sqlId SQL ID
     */
    void deleteApiBySqlId(Integer sqlId);
    
    /**
     * 根据API路径查询API
     *
     * @param path API路径
     * @return API
     */
    FastApi queryApi(String path);
}
