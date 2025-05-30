package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysMenuPermission;
import cn.dyw.auth.db.message.rq.MenuPermissionSaveRq;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-19
 */
public interface ISysMenuPermissionService extends IService<SysMenuPermission> {

    /**
     * 删除菜单的权限
     * @param menuId 菜单ID
     * @param permissionId 权限ID
     */
    void removeMenuPermission(Integer menuId, String permissionId);

    /**
     * 删除菜单的权限权限
     * @param menuId 权限ID
     */
    void removeMenuPermissions(Integer menuId);

    /**
     * 删除角色的菜单子权限
     * @param menuIds 菜单ID
     * @param roleCode 角色ID
     */
    void removeRoleMenuPermissions(List<Integer> menuIds, String roleCode);

    /**
     * 新增菜单子权限
     * @param rq 请求
     */
    void addMenuPermission(MenuPermissionSaveRq rq);
}
