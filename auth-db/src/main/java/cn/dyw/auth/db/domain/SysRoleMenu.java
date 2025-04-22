package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * 菜单授权表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("sys_role_menu")
public class SysRoleMenu {

    /**
     * 角色名
     */
    private String roleCode;

    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 授权时间
     */
    private LocalDateTime authTime;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;
}
