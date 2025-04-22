package cn.dyw.auth.db.model;

import cn.dyw.auth.db.domain.SysMenus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-18
 */
public class MenuDto extends SysMenus {

    @Getter
    @Setter
    private Integer parentMenuId;

    @Getter
    private final List<MenuDto> children = new ArrayList<>();

    public void addChildren(MenuDto menuDto) {
        children.add(menuDto);
    }
}
