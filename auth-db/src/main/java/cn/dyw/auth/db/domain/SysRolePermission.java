package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * 角色权限授权表
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
@Getter
@Setter
@ToString
@TableName("sys_role_permission")
public class SysRolePermission {

    /**
     * 权限id
     */
    @TableId("permission_id")
    private String permissionId;

    /**
     * 角色id
     */
    @TableId("role_code")
    private String roleCode;

    /**
     * 授权时间
     */
    private LocalDateTime authTime;
}
