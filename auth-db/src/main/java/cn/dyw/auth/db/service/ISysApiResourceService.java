package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysApiResource;
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
}
