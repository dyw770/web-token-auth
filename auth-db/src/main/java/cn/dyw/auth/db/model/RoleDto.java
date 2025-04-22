package cn.dyw.auth.db.model;

import cn.dyw.auth.db.domain.SysRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-17
 */
@ToString
public class RoleDto extends SysRole {

    @Getter
    @Setter
    private String parentRoleCode;
    
    @Getter
    private final List<RoleDto> children = new ArrayList<>();
    
    public void addChildren(RoleDto roleDto) {
        children.add(roleDto);
    }
}
