package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.domain.SysRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色权限授权表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 查询角色授权的权限
     * @param roleCode 角色ID
     * @return 结果
     */
    List<SysPermission> rolePermissions(String roleCode);

    /**
     * 已经授权的菜单权限
     * @param menuId 菜单ID
     * @param roleCode 角色ID
     * @return 结果
     */
    List<SysPermission> roleMenuPermissions(Integer menuId, String roleCode);
    
    /**
     * 移除角色权限
     * @param roleCode 角色ID
     * @param permissionIds 权限ID
     */
    void removeRolePermissions(String roleCode, List<String> permissionIds);
    
    /**
     * 添加权限授权
     * @param roleCode 角色ID
     * @param permission 权限ID
     */
    void addPermissionRoleAuth(String roleCode, String permission);

    /**
     * 查询权限授权过的角色
     * @param permission 权限ID
     * @return 角色列表
     */
    List<String> authRoles(String permission);
}
