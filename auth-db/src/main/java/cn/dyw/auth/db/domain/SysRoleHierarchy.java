package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 角色层级表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("sys_role_hierarchy")
public class SysRoleHierarchy {

    /**
     * 祖先角色名
     */
    private String ancestorRoleCode;

    /**
     * 后代角色名
     */
    private String descendantRoleCode;

    /**
     * 深度
     */
    private Integer depth;
}
