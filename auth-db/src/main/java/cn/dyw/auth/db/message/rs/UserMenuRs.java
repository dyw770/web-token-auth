package cn.dyw.auth.db.message.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-02
 */
@Data
public class UserMenuRs {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String path;

    private Integer menuId;
    
    private UserMenuMetaRs meta;

    private List<UserMenuRs> children = new ArrayList<>();
    
    public void addChildren(UserMenuRs rs) {
        this.children.add(rs);
    }

    /**
     * 返回一个name属性，用于前台创建路由匹配
     * @return 唯一name
     */
    public String getName() {
        return menuId.toString();
    }
}
