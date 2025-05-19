package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 新增菜单权限池
 * 
 * @author dyw770
 * @since 2025-05-19
 */
@Data
public class MenuPermissionSaveRq {
    
    /**
     * 菜单
     */
    @Min(1)
    @NotNull
    private Integer menuId;

    /**
     * 权限ID
     */
    @NotBlank
    @Length(max = 32, min = 2)
    private String permissionId;

    /**
     * 权限说明
     */
    @Length(max = 128, min = 2)
    private String permissionDesc;
    
}
