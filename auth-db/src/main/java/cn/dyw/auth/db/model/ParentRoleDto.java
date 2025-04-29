package cn.dyw.auth.db.model;

import lombok.Data;

import java.util.List;

/**
 * 角色及其所有父角色
 * 
 * @author dyw770
 * @since 2025-04-29
 */
@Data
public class ParentRoleDto {

    /**
     * 角色
     */
    private String roleCode;

    /**
     * 父角色
     */
    private List<String> parentRoleCode;
}
