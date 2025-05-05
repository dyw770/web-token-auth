package cn.dyw.auth.db.message.rs;

import lombok.Data;

/**
 * 用户角色
 * 
 * @author dyw770
 * @since 2025-05-05
 */
@Data
public class UserRoleRs {
    /**
     * 角色ID
     */
    private String roleCode;

    /**
     * 角色名
     */
    private String roleName;
}
