package cn.dyw.auth.db.message.rs;

import lombok.Data;

import java.util.List;

/**
 * 用户菜单
 * 
 * @author dyw770
 * @since 2025-05-01
 */
@Data
public class UserMenuMetaRs {
    
    private String title;
    
    private String icon;
    
    private boolean menu;
    
    private int order;
    
    private List<String> permissions;
}
