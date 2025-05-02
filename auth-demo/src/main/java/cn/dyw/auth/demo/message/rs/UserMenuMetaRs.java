package cn.dyw.auth.demo.message.rs;

import lombok.Data;

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
    
    private int order;
}
