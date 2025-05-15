package cn.dyw.auth.db.message.rq;

import cn.dyw.auth.db.domain.SysApiResource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author dyw770
 * @since 2025-05-15
 */
@Data
public class ResourceSaveRq {

    /**
     * api路径
     */
    @NotBlank
    @Length(min = 1, max = 128)
    private String apiPath;

    /**
     * 匹配类型
     */
    @NotNull
    private SysApiResource.MatchType matchType;

    /**
     * api方法
     */
    @NotBlank
    @Length(min = 1, max = 20)
    private String apiMethod;

    /**
     * 描述
     */
    @NotBlank
    @Length(min = 1, max = 128)
    private String description;

}
