package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * api资源授权表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-24
 */
@Getter
@Setter
@ToString
@TableName("sys_api_resource_auth")
public class SysApiResourceAuth {

    /**
     * api资源id
     */
    private Integer apiResourceId;

    /**
     * 授权类型
     */
    private AuthType authType;

    /**
     * 授权对象
     */
    private String authObject;

    /**
     * 授权时间
     */
    private LocalDateTime authTime;

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime;

    /**
     * 授权类型
     */
    public enum AuthType {
        /**
         * 角色
         */
        ROLE,

        /**
         * ip
         */
        IP
    }
}
