package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author dyw770
 * @since 2025-05-27
 */
@Data
public class PermissionUpdateRq {

    /**
     * 权限ID
     */
    @NotBlank
    @Length(min = 2, max = 32)
    private String permissionId;
    
    /**
     * 权限说明
     */
    @NotBlank
    @Length(max = 128)
    private String permissionDesc;
}
