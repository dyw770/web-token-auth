package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 创建用户
 *
 * @author dyw770
 * @since 2025-05-03
 */
@Data
public class UserCreateRq {

    /**
     * 用户名
     */
    @NotBlank
    @Length(min = 3, max = 12)
    private String username;

    /**
     * 昵称
     */
    @NotBlank
    @Length(min = 3, max = 12)
    private String nickname;

    /**
     * 密码
     */
    @NotBlank
    @Length(min = 6, max = 16)
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否启用
     */
    @NotNull
    private Boolean enabled;

}
