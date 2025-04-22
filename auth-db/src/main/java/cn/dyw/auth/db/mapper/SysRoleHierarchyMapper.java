package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysRoleHierarchy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@Mapper
public interface SysRoleHierarchyMapper extends BaseMapper<SysRoleHierarchy> {

    /**
     * 保存角色层级
     *
     * @param roleCode       角色编码
     * @param parentRoleCode 父角色编码
     * @return 更新条数
     */
    int savaRoleHierarchy(@Param("roleCode") String roleCode,
                          @Param("parentRoleCode") String parentRoleCode);

    /**
     * 将角色层级提升到父角色同级
     *
     * @param roleCode 父角色编码
     * @return 更新条数
     */
    int updateRoleHierarchyToParent(@Param("roleCode") String roleCode);

    /**
     * 删除指定角色所有关系
     *
     * @param roleCode 角色编码
     * @return 删除条数
     */
    int deleteRoleHierarchy(@Param("roleCode") String roleCode);

    /**
     * 删除指定角色的父级关系，使其和其子节点成为一个孤立节点
     *
     * @param roleCode 角色编码
     * @return 删除条数
     */
    int deleteRoleParentHierarchy(@Param("roleCode") String roleCode);

    /**
     * 更新角色及其子角色的父级， 注意调用之前必须先使该节点成为孤立节点
     *
     * @param roleCode       角色编码
     * @param parentRoleCode 新父角色
     * @return 插入条数
     */
    int updateRoleParentHierarchy(@Param("roleCode") String roleCode,
                                  @Param("parentRoleCode") String parentRoleCode);
}




