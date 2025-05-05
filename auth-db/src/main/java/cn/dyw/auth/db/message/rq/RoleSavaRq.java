package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 2, max = 12)
    private String roleCode;

    /**
     * 角色名
     */
    @NotBlank
    @Length(min = 2, max = 12)
    private String roleName;

    /**
     * 角色描述
     */
    @Length(max = 128)
    private String description;
}
