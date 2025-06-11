package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.db.mapper.SysApiResourceMapper;
import cn.dyw.auth.db.message.rq.ResourceSaveRq;
import cn.dyw.auth.db.message.rq.ResourceUpdateRq;
import cn.dyw.auth.db.model.ApiResourceDto;
import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.event.AuthChangedApplicationEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private final ApplicationContext context;

    private final SysApiResourceAuthServiceImpl resourceAuthService;

    public SysApiResourceServiceImpl(ApplicationContext context,
                                     SysApiResourceAuthServiceImpl resourceAuthService) {
        this.context = context;
        this.resourceAuthService = resourceAuthService;
    }

    @Override
    public List<ApiResourceDto> listAll() {
        return getBaseMapper().queryAll();
    }

    @Override
    public ApiResourceDto getResourceById(Integer resourceId) {
        return getBaseMapper().queryById(resourceId);
    }

    @Override
    public List<ApiResourceDto> getResourceByIds(List<Integer> resourceIds) {
        return getBaseMapper().queryByIds(resourceIds);
    }

    @Override
    public void removeResource(Integer resourceId) {
        this.removeById(resourceId);
        resourceAuthService.removeAllAuth(resourceId);
    }

    @Override
    public void updateResource(ResourceUpdateRq rq) {
        SysApiResource apiResource = new SysApiResource();
        BeanUtils.copyProperties(rq, apiResource);
        apiResource.setUpdateTime(LocalDateTime.now());
        apiResource.setEnable(null);
        boolean update = this.updateById(apiResource);

        if (update) {
            pushEvent(rq.getId());
        }
    }

    @Override
    public void saveResource(ResourceSaveRq rq) {
        SysApiResource apiResource = new SysApiResource();
        BeanUtils.copyProperties(rq, apiResource);
        apiResource.setUpdateTime(LocalDateTime.now());
        apiResource.setCreateTime(LocalDateTime.now());
        apiResource.setEnable(false);
        this.save(apiResource);
    }

    @Override
    public void enableResource(Integer resourceId, Boolean enable) {
        boolean update = this.lambdaUpdate()
                .eq(SysApiResource::getId, resourceId)
                .set(SysApiResource::getEnable, enable)
                .update();
        if (update) {
            pushEvent(resourceId);
        }
    }

    private void pushEvent(Integer resourceId) {
        List<Integer> resourceIds = new ArrayList<>();
        resourceIds.add(resourceId);
        context.publishEvent(new AuthChangedApplicationEvent(resourceIds));
    }
}
