package cn.dyw.engine.core.exec;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exception.EngineExecException;

/**
 * 报表执行引擎
 *
 * @author dyw770
 * @since 2021-08-02
 */
public interface IExecEngine {

    /**
     * 执行报表
     *
     * @param context 上下文
     * @return 结果
     * @throws EngineExecException 执行异常
     */
    ExecResult exec(ExecContext context)
            throws EngineExecException;

}
