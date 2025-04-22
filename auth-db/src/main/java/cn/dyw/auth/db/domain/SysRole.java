package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * 角色表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("sys_role")
public class SysRole {

    /**
     * 角色名
     */
    @TableId("role_code")
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Boolean del;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
