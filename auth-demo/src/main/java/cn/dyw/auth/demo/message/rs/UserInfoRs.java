package cn.dyw.auth.demo.message.rs;

import lombok.Data;

/**
 * 用户信息
 * 
 * @author dyw770
 * @since 2025-05-01
 */
@Data
public class UserInfoRs {
    
    private String username;
    
    private String nickname;
    
    private String avatar;
}
