package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysMenuHierarchy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜单层级表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
public interface ISysMenuHierarchyService extends IService<SysMenuHierarchy> {

    /**
     * 保存菜单的层级关系
     *
     * @param menuId       菜单ID
     * @param parentMenuId 父菜单ID
     */
    void savaMenuHierarchy(Integer menuId, Integer parentMenuId);

    /**
     * 删除菜单层级
     *
     * @param menuId 菜单ID
     */
    void deleteMenuHierarchy(Integer menuId);

    /**
     * 更新菜单层级
     *
     * @param menuId       菜单ID
     * @param parentMenuId 新父菜单ID
     */
    void updateMenuHierarchyToMenu(Integer menuId, Integer parentMenuId);
}
