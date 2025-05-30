package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单权限池配置
 *
 * @author dyw770
 * @since 2025-05-19
 */
@Data
@TableName(value = "sys_menu_permission")
public class SysMenuPermission {
    /**
     * 菜单
     */
    private Integer menuId;

    /**
     * 权限ID
     */
    private String permissionId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}