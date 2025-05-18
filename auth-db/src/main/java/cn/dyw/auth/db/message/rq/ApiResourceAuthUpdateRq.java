package cn.dyw.auth.db.message.rq;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dyw770
 * @since 2025-05-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiResourceAuthUpdateRq extends ApiResourceAuthAddRq {

    /**
     * 授权ID
     */
    @Min(1)
    @NotNull
    private Integer authId;
}
