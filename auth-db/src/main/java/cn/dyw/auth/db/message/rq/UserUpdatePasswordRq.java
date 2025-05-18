package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author dyw770
 * @since 2025-05-18
 */
@Data
public class UserUpdatePasswordRq {

    /**
     * 旧密码
     */
    @NotBlank
    @Length(min = 6, max = 16)
    private String password;

    /**
     * 新密码
     */
    @NotBlank
    @Length(min = 6, max = 16)
    private String newPassword;
}
