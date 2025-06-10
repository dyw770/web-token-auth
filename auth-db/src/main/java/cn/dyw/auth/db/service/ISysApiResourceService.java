package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.db.message.rq.ResourceSaveRq;
import cn.dyw.auth.db.message.rq.ResourceUpdateRq;
import cn.dyw.auth.db.model.ApiResourceDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * api资源表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
public interface ISysApiResourceService extends IService<SysApiResource> {

    /**
     * 查询全部api资源
     *
     * @return api资源
     */
    List<ApiResourceDto> listAll();

    /**
     * 删除api资源
     *
     * @param resourceId api资源id
     */
    void removeResource(Integer resourceId);

    /**
     * 更新api资源
     *
     * @param rq 更新参数
     */
    void updateResource(ResourceUpdateRq rq);

    /**
     * 保存api资源
     *
     * @param rq 保存参数
     */
    void saveResource(ResourceSaveRq rq);

    /**
     * 启用或禁用api资源
     *
     * @param resourceId api资源id
     * @param enable     是否启用
     */
    void enableResource(Integer resourceId, Boolean enable);
}
