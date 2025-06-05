package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.message.rq.UserSearchRq;
import cn.dyw.auth.db.message.rs.UserRs;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.model.UserDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.GrantedAuthority;

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
     * @param del true 删除角色
     */
    void addRoleForUser(String username, String roleCode, boolean del);

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
     * 查询用户授权的角色
     * @param username 用户名
     * @return 角色列表
     */
    List<String> userAuthRoles(String username);
    
     /**
     * 获取用户权限
     *
     * @param username 用户名
     * @return 权限列表
     */
    List<String> userPermission(String username);

    /**
     * 查询用户菜单
     * @param username 用户名
     * @return 菜单
     */
    List<MenuDto> userMenus(String username);
    
    /**
     * 获取用户权限
     *
     * @param username 用户名
     * @param rolePrefix 角色前缀
     * @return 权限列表
     */
    List<? extends GrantedAuthority> userAuthorities(String username, String rolePrefix);
}
