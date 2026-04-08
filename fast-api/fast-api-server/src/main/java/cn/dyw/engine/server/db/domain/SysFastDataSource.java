package cn.dyw.engine.server.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * 数据源配置
 * </p>
 *
 * @author dyw770
 * @since 2026-04-02
 */
@Getter
@Setter
@ToString
@TableName("sys_fast_data_source")
public class SysFastDataSource {

    /**
     * 数据源名称
     */
    @TableId(value = "source_name", type = IdType.INPUT)
    private String sourceName;

    /**
     * jdbc url
     */
    private String jdbcUrl;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 驱动名称
     */
    private String driverName;

    /**
     * 属性
     */
    private String properties;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
