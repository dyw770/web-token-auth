package cn.dyw.auth.db.message.rq;

import cn.dyw.auth.message.PageRq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @author dyw770
 * @since 2025-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemOperationSearchRq extends PageRq {
    /**
     * 用户名
     */
    @Length(max = 12)
    private String username;

    /**
     * 操作时间
     */
    private LocalDateTime startTime;
    
    /**
     * 操作时间
     */
    private LocalDateTime endTime;

    /**
     * 操作事件
     */
    @Length(max = 12)
    private String operationEvent;
}
