package cn.dyw.auth.db.model;

import cn.dyw.auth.db.domain.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDto extends SysRole {
    
    private String parentRoleCode;
    
    private List<RoleDto> children = new ArrayList<>();
    
    public void addChildren(RoleDto roleDto) {
        children.add(roleDto);
    }
}
