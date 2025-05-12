package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    @NotNull
    private Integer id;

    /**
     * 菜单名
     */
    @NotBlank
    @Length(min = 2, max = 12)
    private String menuName;

    /**
     * 菜单路由
     */
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

    /**
     * 是否显示在导航栏
     */
    @NotNull
    private Boolean navShow;
}
