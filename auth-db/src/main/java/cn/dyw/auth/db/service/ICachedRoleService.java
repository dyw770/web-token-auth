package cn.dyw.auth.db.service;

import cn.dyw.auth.db.model.ParentRoleDto;
import cn.dyw.auth.db.model.RoleDto;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
public interface ICachedRoleService {

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    List<RoleDto> roleTree();

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    List<ParentRoleDto> parentRoleList();
}
