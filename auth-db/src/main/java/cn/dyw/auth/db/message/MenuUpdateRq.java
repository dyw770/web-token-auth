package cn.dyw.auth.db.message;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 更新菜单
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Data
public class MenuUpdateRq {

    /**
     * 菜单ID
     */
    @Min(1)
    private Integer menuId;

    /**
     * 菜单名
     */
    private String menuName;

    /**
     * 菜单路由
     */
    private String menuRouter;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 菜单顺序
     */
    private Integer menuOrder;
}
