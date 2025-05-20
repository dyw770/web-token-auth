package cn.dyw.auth.db.message.rs;

import cn.dyw.auth.db.domain.SysMenuPermission;
import cn.dyw.auth.db.domain.SysRoleMenuPermission;
import lombok.Data;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-20
 */
@Data
public class RoleMenuPermission {

    /**
     * 菜单权限池
     */
    private List<SysMenuPermission> menuPermissions;

    /**
     * 已经授权的菜单权限
     */
    private List<SysRoleMenuPermission> rolePermissions;
}
