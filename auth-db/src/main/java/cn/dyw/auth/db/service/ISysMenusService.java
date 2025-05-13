package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysMenus;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.model.MenuRoleDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
public interface ISysMenusService extends IService<SysMenus> {

    /**
     * 新增菜单
     *
     * @param menu         菜单信息
     * @param parentMenuId 父菜单ID
     */
    void savaMenu(SysMenus menu, Integer parentMenuId);

    /**
     * 更新菜单信息
     *
     * @param menu 菜单信息
     */
    void updateMenu(SysMenus menu);

    /**
     * 更新菜单层级信息
     *
     * @param menuId       菜单ID
     * @param parentMenuId 新父菜单ID
     */
    void updateMenuHierarchy(Integer menuId, Integer parentMenuId);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    void deleteMenu(Integer menuId);

    /**
     * 获取菜单列表，返回的树状结构数据
     *
     * @return 菜单列表
     */
    List<MenuDto> menuList();

    /**
     * 获取菜单列表以及授权的角色，返回的树状结构数据
     *
     * @return 菜单列表
     */
    List<MenuRoleDto> menuRoleList();

    /**
     * 查询角色的菜单列表
     *
     * @param roleCode 角色名
     * @return 角色菜单
     */
    List<MenuDto> roleMenuList(String roleCode);

    /**
     * 查询用户的菜单列表
     *
     * @param username 用户名
     * @return 菜单
     */
    List<MenuDto> userMenuTreeList(String username);

    /**
     * 查询用户的菜单列表
     *
     * @param username 用户名
     * @return 菜单
     */
    List<MenuDto> userMenuList(String username);

    /**
     * 删除角色的菜单授权
     *
     * @param menuIds  菜单ID
     * @param roleCode 角色
     */
    void deleteMenuForRole(List<Integer> menuIds, String roleCode);

    /**
     * 添加角色的菜单授权
     * @param menuIds 菜单ID
     * @param roleCode 角色
     */
    void addMenuForRole(List<Integer> menuIds, String roleCode);
}
