package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.cache.CacheNames;
import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.domain.SysRole;
import cn.dyw.auth.db.domain.SysUserRole;
import cn.dyw.auth.db.mapper.SysRoleMapper;
import cn.dyw.auth.db.model.RoleDto;
import cn.dyw.auth.db.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@Service
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements ISysRoleService {

    private final ISysRoleHierarchyService roleHierarchyService;

    private final ISysUserRoleService userRoleService;

    private final ISysApiResourceAuthService resourceAuthService;
    
    private final ISysRolePermissionService rolePermissionService;

    public SysRoleServiceImpl(ISysRoleHierarchyService roleHierarchyService,
                              ISysUserRoleService userRoleService,
                              ISysApiResourceAuthService resourceAuthService,
                              ISysRolePermissionService rolePermissionService
    ) {
        this.roleHierarchyService = roleHierarchyService;
        this.userRoleService = userRoleService;
        this.resourceAuthService = resourceAuthService;
        this.rolePermissionService = rolePermissionService;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheNames.ROLE_LIST, allEntries = true),
                    @CacheEvict(value = CacheNames.ROLE_MENU, allEntries = true),
                    @CacheEvict(value = CacheNames.USER_AUTH_ROLE, allEntries = true)
            }
    )
    public void savaRole(SysRole sysRole, String parentRoleCode) {

        sysRole.setDel(false);
        sysRole.setCreateTime(LocalDateTime.now());
        sysRole.setUpdateTime(LocalDateTime.now());
        save(sysRole);

        roleHierarchyService.savaRoleHierarchy(sysRole.getRoleCode(), parentRoleCode);
    }

    @Override
    public void updateRole(SysRole sysRole) {
        sysRole.setCreateTime(null);
        sysRole.setUpdateTime(LocalDateTime.now());
        updateById(sysRole);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheNames.ROLE_LIST, allEntries = true),
                    @CacheEvict(value = CacheNames.ROLE_MENU, allEntries = true),
                    @CacheEvict(value = CacheNames.USER_AUTH_ROLE, allEntries = true)
            }
    )
    public void updateRoleHierarchy(String roleCode, String parentRoleCode) {
        lambdaUpdate().eq(SysRole::getRoleCode, roleCode)
                .set(SysRole::getUpdateTime, LocalDateTime.now())
                .update();

        roleHierarchyService.updateRoleHierarchyToRole(roleCode, parentRoleCode);
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = CacheNames.ROLE_LIST, allEntries = true),
                    @CacheEvict(value = CacheNames.ROLE_MENU, allEntries = true),
                    @CacheEvict(value = CacheNames.USER_AUTH_ROLE, allEntries = true)
            }
    )
    public void deleteRole(String roleCode) {
        // 将角色状态设置为删除
        lambdaUpdate().eq(SysRole::getRoleCode, roleCode)
                .set(SysRole::getDel, true)
                .set(SysRole::getUpdateTime, LocalDateTime.now())
                .update();
        // 删除角色的层级关系
        roleHierarchyService.deleteRoleHierarchy(roleCode);
        // 删除角色授权
        userRoleService.lambdaUpdate()
                .eq(SysUserRole::getRoleCode, roleCode)
                .remove();
        // 删除权限
        rolePermissionService.removeRolePermissions(roleCode, List.of());
        // 删除资源授权
        resourceAuthService.removeResourceAuth(SysApiResourceAuth.AuthType.ROLE, roleCode);
    }

    @Override
    @Cacheable(value = CacheNames.ROLE_LIST, key = "'list'")
    public List<RoleDto> roleList() {
       return getBaseMapper().queryRoleList();
    }

    @Override
    @Cacheable(value = CacheNames.USER_AUTH_ROLE, key = "#username")
    public List<String> userAuthRole(String username) {
        return getBaseMapper().queryUserRoles(username);
    }


    @Override
    public List<String> userRoleList(String username) {
        return getBaseMapper().queryRoleCodeByUser(username);
    }

}




