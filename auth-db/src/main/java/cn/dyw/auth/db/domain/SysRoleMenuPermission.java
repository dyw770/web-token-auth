package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色菜单授权
 *
 * @author dyw770
 * @since 2025-05-19
 */

@Data
@TableName(value = "sys_role_menu_permission")
public class SysRoleMenuPermission {
    /**
     * 授权ID
     */
    @TableId(type = IdType.AUTO)
    private Integer authId;

    /**
     * 角色ID
     */
    private String roleCode;

    /**
     * 菜单ID
     */
    private Integer menuId;

    /**
     * 权限ID
     */
    private String permissionId;

    /**
     * 授权时间
     */
    private LocalDateTime authTime;
}