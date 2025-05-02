package cn.dyw.auth.demo.message.rs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-02
 */
@Data
public class UserMenuRs {
    
    private String path;

    private Integer menuId;
    
    private UserMenuMetaRs meta;

    private List<UserMenuRs> children = new ArrayList<>();
    
    public void addChildren(UserMenuRs rs) {
        this.children.add(rs);
    }
}
