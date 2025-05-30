package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.domain.SysRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色权限授权表 Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 查询角色已经授权的权限
     *
     * @param roleCode 角色ID
     * @return 结果
     */
    List<SysPermission> queryRolePermissions(String roleCode);

    /**
     * 查询已经授权的菜单子权限
     *
     * @param menuId   菜单ID
     * @param roleCode 角色ID
     * @return 结果
     */
    List<SysPermission> queryRoleMenuPermissions(@Param("menuId") Integer menuId,
                                                 @Param("roleCode") String roleCode);

    /**
     * 删除角色权限
     *
     * @param roleCode      角色ID
     * @param permissionIds 权限ID集合
     * @return 删除的行数
     */
    int deleteRolePermissions(@Param("roleCode") String roleCode, @Param("permissionIds") List<String> permissionIds);
}

