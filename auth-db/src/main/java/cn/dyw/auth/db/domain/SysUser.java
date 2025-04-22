package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * 用户表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("sys_user")
public class SysUser {

    /**
     * 用户名
     */
    @TableId("username")
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

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
     * 更新时间
     */
    private LocalDateTime updateTime;
}
