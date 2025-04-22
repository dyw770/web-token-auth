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
 * 系统操作日志表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("sys_system_operation_log")
public class SysSystemOperationLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 操作ip
     */
    private String operationIp;

    /**
     * 操作ua
     */
    private String operationUa;

    /**
     * 操作结果类型
     */
    private String operationResultType;

    /**
     * 操作结果代码
     */
    private Integer operationResultCode;

    /**
     * 操作内容
     */
    private String operationContent;

    /**
     * 操作事件
     */
    private String operationEvent;
}
