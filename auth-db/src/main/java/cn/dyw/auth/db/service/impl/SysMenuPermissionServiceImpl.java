package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysMenuPermission;
import cn.dyw.auth.db.mapper.SysMenuPermissionMapper;
import cn.dyw.auth.db.service.ISysMenuPermissionService;
import cn.dyw.auth.db.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dyw770
 * @since 2025-05-19
 */
@Service
public class SysMenuPermissionServiceImpl extends ServiceImpl<SysMenuPermissionMapper, SysMenuPermission>
        implements ISysMenuPermissionService {

    private final ISysPermissionService permissionService;

    public SysMenuPermissionServiceImpl(ISysPermissionService permissionService) {

        this.permissionService = permissionService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMenuPermission(Integer menuId, String permissionId) {

        permissionService.deletePermission(permissionId);

        this.lambdaUpdate()
                .eq(SysMenuPermission::getMenuId, menuId)
                .eq(SysMenuPermission::getPermissionId, permissionId)
                .remove();
    }
}




