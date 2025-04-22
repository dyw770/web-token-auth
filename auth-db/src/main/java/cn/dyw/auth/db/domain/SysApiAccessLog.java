package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * api访问日志表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("sys_api_access_log")
public class SysApiAccessLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * api路径
     */
    private String apiPath;

    /**
     * api方法
     */
    private String apiMethod;

    /**
     * api访问时间
     */
    private LocalDateTime apiAccessTime;

    /**
     * api访问时长
     */
    private Integer apiAccessDuration;

    /**
     * api访问ip
     */
    private String apiAccessIp;

    /**
     * api访问ua
     */
    private String apiAccessUa;

    /**
     * api访问结果类型
     */
    private String apiAccessResultType;

    /**
     * api访问结果代码
     */
    private Integer apiAccessResultCode;

    /**
     * api访问响应代码
     */
    private Integer apiAccessResponseCode;
}
