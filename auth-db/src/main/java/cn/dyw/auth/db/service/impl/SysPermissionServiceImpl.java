package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.domain.SysRolePermission;
import cn.dyw.auth.db.mapper.SysPermissionMapper;
import cn.dyw.auth.db.message.rq.PermissionSaveRq;
import cn.dyw.auth.db.model.SysMenuPermissionDto;
import cn.dyw.auth.db.service.ISysApiResourceAuthService;
import cn.dyw.auth.db.service.ISysPermissionService;
import cn.dyw.auth.db.service.ISysRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    private final ISysRolePermissionService rolePermissionService;
    
    private final ISysApiResourceAuthService resourceAuthService;

    public SysPermissionServiceImpl(ISysRolePermissionService rolePermissionService, ISysApiResourceAuthService resourceAuthService) {
        this.rolePermissionService = rolePermissionService;
        this.resourceAuthService = resourceAuthService;
    }

    @Override
    public void savePermission(PermissionSaveRq rq, SysPermission.PermissionType permissionType) {
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(rq, permission);
        permission.setCreateTime(LocalDateTime.now());
        permission.setPermissionType(permissionType);
        this.save(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(String permissionId) {
        this.removeById(permissionId);
        rolePermissionService.lambdaUpdate()
                .eq(SysRolePermission::getPermissionId, permissionId)
                .remove();
        resourceAuthService.lambdaUpdate()
                .eq(SysApiResourceAuth::getAuthObject, permissionId)
                .eq(SysApiResourceAuth::getAuthType, SysApiResourceAuth.AuthType.PERMISSION)
                .remove();
    }

    @Override
    public List<SysMenuPermissionDto> menuPermission(Integer menuId) {
        return getBaseMapper().queryMenuPermissionList(menuId);
    }
}
