package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysRoleHierarchy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色层级表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
public interface ISysRoleHierarchyService extends IService<SysRoleHierarchy> {

    /**
     * 保存角色层级
     *
     * @param roleCode       角色编码
     * @param parentRoleCode 父角色
     */
    void savaRoleHierarchy(String roleCode, String parentRoleCode);

    /**
     * 删除角色层级
     *
     * @param roleCode 角色编码
     */
    void deleteRoleHierarchy(String roleCode);

    /**
     * 更新角色层级
     *
     * @param roleCode       角色编码
     * @param parentRoleCode 新父级角色编码
     */
    void updateRoleHierarchyToRole(String roleCode, String parentRoleCode);
}
