package cn.dyw.engine.core.exec;

import cn.dyw.engine.core.context.ExecContext;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 报表结果
 *
 * @author dyw770
 * @since 2021-08-03
 */
public abstract class ExecResult {

    /**
     * 上下文
     *
     * @return 结果
     */
    @JsonIgnore
    public abstract ExecContext getContext();
}
