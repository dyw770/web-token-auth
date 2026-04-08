package cn.dyw.engine.server.message.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author dyw770
 * @since 2026-04-07
 */
@Data
public class DataSourceEditRq {

    /**
     * 数据源名称
     */
    @NotBlank
    @Size(min = 1, max = 32)
    private String sourceName;

    /**
     * jdbc url
     */
    @NotBlank
    @Size(min = 1, max = 256)
    private String jdbcUrl;

    /**
     * 用户名
     */
    @Size(min = 1, max = 64)
    private String username;

    /**
     * 密码
     */
    @Size(min = 1, max = 64)
    private String password;

    /**
     * 驱动名称
     */
    @NotBlank
    @Size(min = 1, max = 256)
    private String driverName;

    /**
     * 属性
     */
    @Size(min = 1, max = 4096)
    private String properties;

    /**
     * 数据库类型
     */
    @NotBlank
    @Size(min = 1, max = 256)
    private String dbType;
}
