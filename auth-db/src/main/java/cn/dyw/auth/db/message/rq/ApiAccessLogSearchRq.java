package cn.dyw.auth.db.message.rq;

import cn.dyw.auth.message.PageRq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author dyw770
 * @since 2025-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiAccessLogSearchRq extends PageRq {
    /**
     * 用户名
     */
    private String username;

    /**
     * api路径
     */
    private String apiPath;


    /**
     * api访问时间
     */
    private LocalDateTime startTime;
    
    /**
     * api访问时间
     */
    private LocalDateTime endTime;


    /**
     * api访问ip
     */
    private String apiAccessIp;

}
