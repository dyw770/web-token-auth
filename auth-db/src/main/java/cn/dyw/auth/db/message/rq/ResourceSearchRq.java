package cn.dyw.auth.db.message.rq;

import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.message.PageRq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @author dyw770
 * @since 2025-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceSearchRq extends PageRq {

    /**
     * 资源路径
     */
    @Length(max = 128)
    private String apiPath;

    /**
     * 请求方法
     */
    @Length(max = 20)
    private String apiMethod;
    
    /**
     * 描述
     */
    @Length(max = 128)
    private String description;

    /**
     * 匹配方式
     */
    private SysApiResource.MatchType matchType;
}
