package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * 用户角色授权表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("sys_user_role")
public class SysUserRole {

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色名
     */
    private String roleCode;

    /**
     * 授权时间
     */
    private LocalDateTime authTime;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;
}
