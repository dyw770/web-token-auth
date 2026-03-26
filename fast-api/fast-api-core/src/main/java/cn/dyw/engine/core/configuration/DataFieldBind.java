package cn.dyw.engine.core.configuration;

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
    private String headerField;

    /**
     * 映射字段名
     */
    private String headerName;
}
