package cn.dyw.auth.db.message.rq;

import cn.dyw.auth.db.domain.SysApiResource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author dyw770
 * @since 2025-05-15
 */
@Data
public class ResourceUpdateRq {

    /**
     * 资源id
     */
    @Min(1)
    @NotNull
    private Integer id;

    /**
     * api路径
     */
    @Length(min = 1, max = 128)
    private String apiPath;

    /**
     * 匹配类型
     */
    private SysApiResource.MatchType matchType;

    /**
     * api方法
     */
    @Length(min = 1, max = 20)
    private String apiMethod;

    /**
     * 描述
     */
    @Length(min = 1, max = 128)
    private String description;
}
