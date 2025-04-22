package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.model.UserDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 查询用户信息
     * @param username 用户名
     * @return 结果
     */
    UserDto getUserByUsername(String username);

    /**
     * 为用户添加角色
     * @param username 用户名
     * @param roleCode 角色代码
     */
    void addRoleForUser(String username, String roleCode);

    /**
     * 删除用户角色
     * @param username 用户名
     * @param roleCode 角色代码
     */
    void deleteRoleForUser(String username, String roleCode);
}
