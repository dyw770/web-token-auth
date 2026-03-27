package cn.dyw.engine.server.message.rq;

import cn.dyw.auth.message.PageRq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SqlSearchRq extends PageRq {

    /**
     * 名称
     */
    @Length(max = 16)
    private String sqlName;

    /**
     * 描述
     */
    @Length(max = 16)
    private String sqlDescribe;
}
