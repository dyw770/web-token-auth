package cn.dyw.engine.core.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字段映射
 *
 * @author dyw770
 * @since 2021-08-05
 */
@Data
public class DataFieldBind {

    /**
     * 真实字段名
     */
    @NotBlank
    private String headerField;

    /**
     * 映射字段名
     */
    @NotBlank
    private String headerName;
}
