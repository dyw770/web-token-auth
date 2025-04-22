package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 菜单层级表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("sys_menu_hierarchy")
public class SysMenuHierarchy {

    /**
     * 祖先菜单id
     */
    private Integer ancestorMenuId;

    /**
     * 后代菜单id
     */
    private Integer descendantMenuId;

    /**
     * 深度
     */
    private Integer depth;
}
