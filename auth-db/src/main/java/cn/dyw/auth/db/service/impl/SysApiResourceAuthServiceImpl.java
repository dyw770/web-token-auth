package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.mapper.SysApiResourceAuthMapper;
import cn.dyw.auth.db.message.rq.ApiResourceAuthAddRq;
import cn.dyw.auth.db.message.rq.ApiResourceAuthUpdateRq;
import cn.dyw.auth.db.service.ISysApiResourceAuthService;
import cn.dyw.auth.event.AuthChangedApplicationEvent;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * api资源授权表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-24
 */
@Service
public class SysApiResourceAuthServiceImpl extends ServiceImpl<SysApiResourceAuthMapper, SysApiResourceAuth> implements ISysApiResourceAuthService {

    private final ApplicationContext applicationContext;

    public SysApiResourceAuthServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void removeResourceAuth(SysApiResourceAuth.AuthType authType, String authObject) {
        List<SysApiResourceAuth> list = this.lambdaQuery()
                .eq(SysApiResourceAuth::getAuthType, authType)
                .eq(SysApiResourceAuth::getAuthObject, authObject)
                .list();

        if (list.isEmpty()) {
            return;
        }

        this.lambdaUpdate()
                .eq(SysApiResourceAuth::getAuthType, authType)
                .eq(SysApiResourceAuth::getAuthObject, authObject)
                .remove();

        List<Integer> resourceId = list.stream()
                .map(SysApiResourceAuth::getApiResourceId)
                .distinct()
                .toList();

        applicationContext.publishEvent(new AuthChangedApplicationEvent(resourceId));
    }

    @Override
    public void removeResourceAuth(SysApiResourceAuth.AuthType authType, List<String> authObjects) {
        List<SysApiResourceAuth> list = this.lambdaQuery()
                .eq(SysApiResourceAuth::getAuthType, authType)
                .in(SysApiResourceAuth::getAuthObject, authObjects)
                .list();

        if (list.isEmpty()) {
            return;
        }

        this.lambdaUpdate()
                .eq(SysApiResourceAuth::getAuthType, authType)
                .in(SysApiResourceAuth::getAuthObject, authObjects)
                .remove();

        List<Integer> resourceId = list.stream()
                .map(SysApiResourceAuth::getApiResourceId)
                .distinct()
                .toList();

        applicationContext.publishEvent(new AuthChangedApplicationEvent(resourceId));
    }

    @Override
    public void removeAuth(Integer authId) {
        SysApiResourceAuth auth = getById(authId);
        if (ObjectUtils.isEmpty(auth)) {
            return;
        }

        removeById(authId);

        pushEvent(auth.getApiResourceId());
    }

    @Override
    public void updateAuth(ApiResourceAuthUpdateRq rq) {
        SysApiResourceAuth auth = getById(rq.getAuthId());
        if (ObjectUtils.isEmpty(auth)) {
            return;
        }

        this.lambdaUpdate()
                .eq(SysApiResourceAuth::getAuthId, rq.getAuthId())
                .set(SysApiResourceAuth::getAuthType, rq.getAuthType())
                .set(SysApiResourceAuth::getAuthObject, rq.getAuthObject())
                .update();

        pushEvent(auth.getApiResourceId());
    }

    @Override
    public void saveAuth(ApiResourceAuthAddRq rq) {
        SysApiResourceAuth resourceAuth = new SysApiResourceAuth();
        BeanUtils.copyProperties(rq, resourceAuth);
        resourceAuth.setAuthTime(LocalDateTime.now());
        resourceAuth.setExpiredTime(LocalDateTime.now());
        this.save(resourceAuth);

        pushEvent(resourceAuth.getApiResourceId());
    }

    @Override
    public void removeAllAuth(Integer resourceId) {
        List<SysApiResourceAuth> list = this.lambdaQuery()
                .eq(SysApiResourceAuth::getApiResourceId, resourceId)
                .list();
        if (list.isEmpty()) {
            return;
        }
        this.lambdaUpdate()
                .eq(SysApiResourceAuth::getApiResourceId, resourceId)
                .remove();
        
        pushEvent(resourceId);
    }

    private void pushEvent(Integer resourceId) {
        List<Integer> resourceIds = new ArrayList<>();
        resourceIds.add(resourceId);
        applicationContext.publishEvent(new AuthChangedApplicationEvent(resourceIds));
    }
}
