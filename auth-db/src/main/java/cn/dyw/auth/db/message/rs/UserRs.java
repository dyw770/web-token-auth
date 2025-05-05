package cn.dyw.auth.db.message.rs;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息
 * 
 * @author dyw770
 * @since 2025-05-03
 */
@Data
public class UserRs {
    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 账号是否过期
     */
    private Boolean accountNonExpired;

    /**
     * 密码是否过期
     */
    private Boolean credentialsNonExpired;

    /**
     * 账号是否锁定
     */
    private Boolean accountNonLocked;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 角色列表
     */
    private List<UserRoleRs> roles;
}
