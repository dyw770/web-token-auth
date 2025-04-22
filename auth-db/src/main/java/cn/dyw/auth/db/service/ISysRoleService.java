package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysRole;
import cn.dyw.auth.db.model.RoleDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 新增角色
     *
     * @param sysRole        角色信息
     * @param parentRoleCode 父角色编码
     */
    void savaRole(SysRole sysRole, String parentRoleCode);

    /**
     * 跟新角色信息
     *
     * @param sysRole 角色信息
     */
    void updateRole(SysRole sysRole);

    /**
     * 更新角色层级信息
     *
     * @param roleCode       角色编码
     * @param parentRoleCode 新的父角色
     */
    void updateRoleHierarchy(String roleCode, String parentRoleCode);

    /**
     * 删除角色信息
     *
     * @param roleCode 角色编码
     */
    void deleteRole(String roleCode);

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    List<RoleDto> roleList();

    /**
     * 查询用户角色列表，包括其拥有的子角色
     * @param username 用户名
     * @return 角色列表
     */
    List<String> userRoleList(String username);
}
