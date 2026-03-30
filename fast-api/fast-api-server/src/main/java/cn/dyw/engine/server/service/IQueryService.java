package cn.dyw.engine.server.service;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.server.message.rq.ExecParameterRq;
import cn.dyw.engine.server.message.rq.ExecRq;

/**
 *
 * @author dyw770
 * @since 2026-03-27
 */
public interface IQueryService {

    /**
     * 执行查询
     *
     * @param rq 请求参数
     * @return 结果
     */
    ExecResult execQuery(ExecRq rq);

    /**
     * 创建执行上下文
     *
     * @param rq 请求参数
     * @return 执行上下文
     */
    ExecContext contextFactory(ExecRq rq);

    /**
     * 执行查询
     *
     * @param context 待执行SQL
     * @return 执行结果
     */
    ExecResult execResult(ExecContext context);

    /**
     * 执行
     *
     * @param rq 执行参数
     * @param sqlId sqlId
     * @return 执行结果
     */
    ExecResult execSql(ExecParameterRq rq, Integer sqlId);
    
    /**
     * 执行API
     *
     * @param rq 执行参数
     * @param path api路径
     * @return 执行结果
     */
    ExecResult execApi(ExecParameterRq rq, String path);
}
