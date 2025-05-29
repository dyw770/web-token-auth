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
     * 查询角色以及子角色的权限
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
}
