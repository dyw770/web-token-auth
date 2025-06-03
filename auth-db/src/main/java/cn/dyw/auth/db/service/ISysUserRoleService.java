package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色授权表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 删除用户角色
     *
     * @param username 用户名
     * @param roleCode 角色代码
     */
    void deleteRoleForUser(String username, String roleCode);
}
