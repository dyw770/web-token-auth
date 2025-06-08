package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.cache.CacheNames;
import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.domain.SysRolePermission;
import cn.dyw.auth.db.mapper.SysRolePermissionMapper;
import cn.dyw.auth.db.service.ISysRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色权限授权表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
@Slf4j
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
    @CacheEvict(value = CacheNames.ROLE_PERMISSION, allEntries = true)
    public void removeRolePermissions(String roleCode, List<String> permissionIds) {
        getBaseMapper().deleteRolePermissions(roleCode, permissionIds);
    }

    @Override
    @CacheEvict(value = CacheNames.ROLE_PERMISSION, allEntries = true)
    public void addPermissionRoleAuth(String roleCode, String permission) {
        Long count = this.lambdaQuery()
                .eq(SysRolePermission::getRoleCode, roleCode)
                .eq(SysRolePermission::getPermissionId, permission)
                .count();
        if (count > 0) {
            log.info("角色{}已经授权过{}权限", roleCode, permission);
            return;
        }
        SysRolePermission sysRolePermission = new SysRolePermission();
        sysRolePermission.setRoleCode(roleCode);
        sysRolePermission.setPermissionId(permission);
        sysRolePermission.setAuthTime(LocalDateTime.now());
        save(sysRolePermission);
    }

    @Override
    public List<String> authRoles(String permission) {
        List<SysRolePermission> list = this.lambdaQuery()
                .eq(SysRolePermission::getPermissionId, permission)
                .list();
        return list.stream().map(SysRolePermission::getRoleCode).toList();
    }
}
