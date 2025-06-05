package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysMenus;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.model.MenuRoleDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Mapper
public interface SysMenusMapper extends BaseMapper<SysMenus> {

    /**
     * 查询菜单列表
     *
     * @return 全部菜单
     */
    List<MenuDto> queryMenuList();

    /**
     * 查询菜单列表以及授权的角色
     *
     * @return 全部菜单
     */
    List<MenuRoleDto> queryMenuRoleList();

    /**
     * 查询角色菜单列表
     * 包括其子角色的菜单
     *
     * @param roleCode 角色编码
     * @return 菜单
     */
    List<MenuDto> queryRoleMenuList(@Param("roleCode") String roleCode);

    /**
     * 查询角色菜单列表
     *
     * @param roleCode 角色编码
     * @return 菜单
     */
    List<MenuDto> queryRoleAuthMenuList(@Param("roleCode") String roleCode);

    /**
     * 查询用户菜单列表
     * @param username 用户名
     * @return 菜单
     */
    List<MenuDto> queryUserMenuList(@Param("username") String username);

    /**
     * 删除角色菜单
     * @param menuIds 菜单ID
     * @param roleCode 角色
     * @return 删除条数
     */
    int deleteMenuForRole(@Param("menuIds") List<Integer> menuIds, @Param("roleCode") String roleCode);

    /**
     * 添加角色菜单授权
     * @param menuIds 菜单ID
     * @param roleCode 角色
     * @return 添加条数
     */
    int insertMenuForRole(@Param("menuIds") List<Integer> menuIds, @Param("roleCode") String roleCode);
}


