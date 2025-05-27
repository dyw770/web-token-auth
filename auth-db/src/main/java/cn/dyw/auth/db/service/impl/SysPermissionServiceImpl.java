package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.mapper.SysPermissionMapper;
import cn.dyw.auth.db.message.rq.PermissionSaveRq;
import cn.dyw.auth.db.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Override
    public void savePermission(PermissionSaveRq rq, SysPermission.PermissionType permissionType) {
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(rq, permission);
        permission.setCreateTime(LocalDateTime.now());
        permission.setPermissionType(permissionType);
        this.save(permission);
    }
}
