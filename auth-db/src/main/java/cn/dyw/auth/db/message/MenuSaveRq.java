package cn.dyw.auth.db.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增菜单
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Data
public class MenuSaveRq {

    /**
     * 菜单名
     */
    @NotBlank
    private String menuName;

    /**
     * 菜单路由
     */
    @NotBlank
    private String menuRouter;

    /**
     * 菜单图标
     */
    @NotBlank
    private String menuIcon;

    /**
     * 菜单顺序
     */
    @NotNull
    private Integer menuOrder;
}
