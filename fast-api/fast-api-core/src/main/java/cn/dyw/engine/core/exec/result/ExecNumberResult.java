package cn.dyw.engine.core.exec.result;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exec.ExecResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 执行影响行数结果
 *
 * @author dyw770
 * @since 2022-09-29
 */
public class ExecNumberResult extends ExecResult {

    private final ExecContext context;

    @Getter
    @Setter
    private long number;

    public ExecNumberResult(ExecContext context, long number) {
        this.context = context;
        this.number = number;
    }

    @Override
    public ExecContext getContext() {
        return this.context;
    }

    @Override
    public String toString() {
        return "ExecNumberResult{" +
                " number=" + number +
                " }";
    }
}
