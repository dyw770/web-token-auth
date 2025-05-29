package cn.dyw.auth.db.message.rs;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.model.SysMenuPermissionDto;
import lombok.Data;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-20
 */
@Data
public class RoleMenuPermissionRs {

    /**
     * 菜单权限池
     */
    private List<SysMenuPermissionDto> menuPermissions;

    /**
     * 已经授权的菜单权限
     */
    private List<SysPermission> rolePermissions;
}
