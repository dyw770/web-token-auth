package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.cache.CacheNames;
import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.domain.SysRolePermission;
import cn.dyw.auth.db.mapper.SysRolePermissionMapper;
import cn.dyw.auth.db.service.ISysRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色权限授权表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Override
    @Cacheable(value = CacheNames.ROLE_PERMISSION, key = "#roleCode")
    public List<SysPermission> rolePermissions(String roleCode) {
        return getBaseMapper().queryRolePermissions(roleCode);
    }

    @Override
    public List<SysPermission> roleMenuPermissions(Integer menuId, String roleCode) {
        return getBaseMapper().queryRoleMenuPermissions(menuId, roleCode);
    }

    @Override
    public void removeRolePermissions(String roleCode, List<String> permissionIds) {
        getBaseMapper().deleteRolePermissions(roleCode, permissionIds);
    }
}
