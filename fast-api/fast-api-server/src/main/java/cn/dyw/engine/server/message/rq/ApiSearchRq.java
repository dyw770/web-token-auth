package cn.dyw.engine.server.message.rq;

import cn.dyw.auth.message.PageRq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * API搜索请求
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiSearchRq extends PageRq {

    /**
     * 名称
     */
    @Length(max = 32)
    private String apiName;

    /**
     * 描述
     */
    @Length(max = 128)
    private String apiDescribe;

    /**
     * 路径
     */
    @Length(max = 128)
    private String apiPath;
}