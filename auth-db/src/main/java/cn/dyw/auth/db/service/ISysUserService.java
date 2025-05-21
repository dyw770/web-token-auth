package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.message.rq.UserSearchRq;
import cn.dyw.auth.db.message.rs.UserRs;
import cn.dyw.auth.db.model.UserDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

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
     *
     * @param username 用户名
     * @return 结果
     */
    UserDto getUserByUsername(String username);

    /**
     * 为用户添加角色
     *
     * @param username 用户名
     * @param roleCode 角色代码
     */
    void addRoleForUser(String username, String roleCode);

    /**
     * 为用户添加角色
     *
     * @param username  用户名
     * @param roleCodes 角色代码
     */
    void addRoleForUser(String username, List<String> roleCodes);

    /**
     * 删除用户角色
     *
     * @param username 用户名
     * @param roleCode 角色代码
     */
    void deleteRoleForUser(String username, String roleCode);

    /**
     * 查询用户列表
     *
     * @param rq 查询参数
     * @return 用户列表
     */
    Page<UserRs> userList(UserSearchRq rq);

    /**
     * 返回用户权限
     *
     * @param username 用户名
     * @return 权限列表
     */
    Collection<GrantedAuthority> userAuthority(String username);
}
