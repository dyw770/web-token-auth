package cn.dyw.auth.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-18
 */
public class MenuRoleDto extends MenuDto {

    @Getter
    @Setter
    private List<String> roles;
}
