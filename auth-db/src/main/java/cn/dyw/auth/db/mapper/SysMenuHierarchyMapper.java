package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysMenuHierarchy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 菜单层级表 Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Mapper
public interface SysMenuHierarchyMapper extends BaseMapper<SysMenuHierarchy> {

    /**
     * 保存菜单的层级关系
     *
     * @param menuId       菜单ID
     * @param parentMenuId 父菜单ID
     * @return 更新条数
     */
    int savaMenuHierarchy(@Param("menuId") Integer menuId,
                          @Param("parentMenuId") Integer parentMenuId);

    /**
     * 将菜单层级提升到父菜单同级
     *
     * @param menuId 父菜单ID
     * @return 更新条数
     */
    int updateMenuHierarchyToParent(@Param("menuId") Integer menuId);


    /**
     * 删除指定菜单所有关系
     *
     * @param menuId 菜单ID
     * @return 删除条数
     */
    int deleteMenuHierarchy(@Param("menuId") Integer menuId);

    /**
     * 删除指定菜单的父级关系，使其和其子节点成为一个孤立节点
     *
     * @param menuId 菜单ID
     * @return 删除条数
     */
    int deleteMenuParentHierarchy(@Param("menuId") Integer menuId);

    /**
     * 更新菜单及其子菜单的父级， 注意调用之前必须先使该节点成为孤立节点
     *
     * @param menuId       菜单ID
     * @param parentMenuId 新父菜单ID
     * @return 插入条数
     */
    int updateMenuParentHierarchy(@Param("menuId") Integer menuId,
                                  @Param("parentMenuId") Integer parentMenuId);


}

