package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.db.mapper.SysApiResourceMapper;
import cn.dyw.auth.db.model.ApiResourceDto;
import cn.dyw.auth.db.service.ISysApiResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * api资源表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Service
public class SysApiResourceServiceImpl extends ServiceImpl<SysApiResourceMapper, SysApiResource> implements ISysApiResourceService {

    @Override
    public List<ApiResourceDto> listAll() {
        return getBaseMapper().queryAll();
    }
}
