package cn.dyw.auth.db.model;

import cn.dyw.auth.db.domain.SysPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dyw770
 * @since 2025-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuPermissionDto extends SysPermission {
    /**
     * 菜单ID
     */
    private Integer menuId;
}
