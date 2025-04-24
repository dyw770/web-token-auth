package cn.dyw.auth.db.model;

import cn.dyw.auth.db.domain.SysApiResourceAuth.AuthType;
import lombok.Data;

/**
 * 授权
 * 
 * @author dyw770
 * @since 2025-04-24
 */
@Data
public class AuthDto {

    /**
     * 授权类型
     */
    private AuthType authType;
 
    /**
     * 授权对象
     */
    private String authObject;
}
