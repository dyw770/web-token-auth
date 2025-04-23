package cn.dyw.auth.db.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改角色
 * 
 * @author dyw770
 * @since 2025-04-23
 */
@Data
public class RoleUpdateRq {

    /**
     * 角色名
     */
    @NotBlank
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Boolean del;
}
