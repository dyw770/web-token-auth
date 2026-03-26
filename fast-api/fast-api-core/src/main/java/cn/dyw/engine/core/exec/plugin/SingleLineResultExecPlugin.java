package cn.dyw.engine.core.exec.plugin;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.context.SelectExecContext;
import cn.dyw.engine.core.exec.EngineExecutor;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.exec.result.DataSetResult;
import cn.dyw.engine.core.exec.result.SingleLineExecResult;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理单行数据插件
 *
 * @author dyw770
 * @since 2022-09-27
 */
@Slf4j
public class SingleLineResultExecPlugin implements SqlExecPlugin {

    public static final String SINGLE_LINE_RESULT = "single";

    @Override
    public ExecResult doExec(ExecContext context, EngineExecutor executor, ExecPluginChain chain) {
        ExecResult execResult = chain.doChain();
        if (execResult instanceof DataSetResult dataSetResult) {
            SingleLineExecResult result = new SingleLineExecResult(dataSetResult.getContext());
            result.initSchema(dataSetResult.getSchema());
            if (!dataSetResult.getData().isEmpty()) {
                result.addData(dataSetResult.getData().get(0));
            }
            return result;
        }
        return execResult;
    }

    @Override
    public boolean canSupport(ExecContext context, EngineExecutor executor, ExecPluginChain chain) {
        if (context instanceof SelectExecContext
                && context.getExtend().getOrDefault(SINGLE_LINE_RESULT, Boolean.FALSE).equals(Boolean.TRUE)) {
            // 未启用分页则返回单行数据结构
            return !((SelectExecContext) context).getDataPageOption().isNeedPage();
        } else {
            return false;
        }
    }
}
