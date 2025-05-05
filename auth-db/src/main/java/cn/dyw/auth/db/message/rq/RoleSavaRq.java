package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增角色
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Data
public class RoleSavaRq {

    /**
     * 角色名
     */
    @NotBlank
    private String roleCode;

    /**
     * 角色描述
     */
    @NotBlank
    private String description;
}
