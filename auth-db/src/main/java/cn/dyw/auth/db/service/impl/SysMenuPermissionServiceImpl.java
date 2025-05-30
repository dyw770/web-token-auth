package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysMenuPermission;
import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.mapper.SysMenuPermissionMapper;
import cn.dyw.auth.db.message.rq.MenuPermissionSaveRq;
import cn.dyw.auth.db.service.ISysMenuPermissionService;
import cn.dyw.auth.db.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

        permissionService.removeMenuPermission(List.of(permissionId));

        this.lambdaUpdate()
                .eq(SysMenuPermission::getMenuId, menuId)
                .eq(SysMenuPermission::getPermissionId, permissionId)
                .remove();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMenuPermissions(Integer menuId) {
        List<SysMenuPermission> list = this.lambdaQuery()
                .eq(SysMenuPermission::getMenuId, menuId)
                .list();
        List<String> permissions = list.stream()
                .map(SysMenuPermission::getPermissionId)
                .distinct()
                .toList();
        permissionService.removeMenuPermission(permissions);
        this.lambdaUpdate()
                .eq(SysMenuPermission::getMenuId, menuId)
                .remove();
    }

    @Override
    public void removeRoleMenuPermissions(List<Integer> menuIds, String roleCode) {
        getBaseMapper().deleteRoleMenuPermissions(menuIds, roleCode);
    }

    @Override
    public void addMenuPermission(MenuPermissionSaveRq rq) {
        SysPermission permission = new SysPermission();
        permission.setPermissionType(SysPermission.PermissionType.MENU);
        permission.setPermissionId(rq.getPermissionId());
        permission.setPermissionDesc(rq.getPermissionDesc());
        permission.setCreateTime(LocalDateTime.now());
        permissionService.save(permission);

        SysMenuPermission sysMenuPermission = new SysMenuPermission();
        BeanUtils.copyProperties(rq, sysMenuPermission);
        sysMenuPermission.setCreateTime(LocalDateTime.now());
        this.save(sysMenuPermission);
    }
}




