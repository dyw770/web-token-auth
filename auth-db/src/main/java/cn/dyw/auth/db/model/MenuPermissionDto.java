package cn.dyw.auth.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dyw770
 * @since 2025-05-20
 */
public class MenuPermissionDto extends MenuDto {

    /**
     * 菜单权限
     */
    @Getter
    @Setter
    private List<String> permissions = new ArrayList<>();
}
