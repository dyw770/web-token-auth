package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author dyw770
 * @since 2025-05-20
 */
@Data
public class RoleMenuPermissionSaveRq {
    /**
     * 角色ID
     */
    @NotBlank
    @Length(max = 32, min = 2)
    private String roleCode;

    /**
     * 菜单ID
     */
    @NotNull
    private Integer menuId;

    /**
     * 权限ID
     */
    @NotBlank
    @Length(max = 128, min = 2)
    private String permissionId;
}
