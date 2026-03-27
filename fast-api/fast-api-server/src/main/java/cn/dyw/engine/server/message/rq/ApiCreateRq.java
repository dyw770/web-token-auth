package cn.dyw.engine.server.message.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 创建API请求
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Data
public class ApiCreateRq {

    /**
     * 名称
     */
    @NotBlank
    @Length(min = 1, max = 32)
    private String apiName;

    /**
     * 描述
     */
    @NotBlank
    @Length(min = 1, max = 128)
    private String apiDescribe;

    /**
     * 路径
     */
    @NotBlank
    @Length(min = 1, max = 128)
    private String apiPath;

    /**
     * SQL ID
     */
    @NotNull
    private Integer sysSql;
}