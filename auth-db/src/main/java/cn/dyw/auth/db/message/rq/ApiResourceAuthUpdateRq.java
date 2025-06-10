package cn.dyw.auth.db.message.rq;

import cn.dyw.auth.db.domain.SysApiResourceAuth;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author dyw770
 * @since 2025-05-17
 */

@Data
public class ApiResourceAuthUpdateRq {

    /**
     * 授权ID
     */
    @Min(1)
    @NotNull
    private Integer authId;

    /**
     * 授权类型
     */
    @NotNull
    private SysApiResourceAuth.AuthType authType;

    /**
     * 授权对象
     */
    @NotBlank
    @Length(min = 2, max = 24)
    private String authObject;
}
