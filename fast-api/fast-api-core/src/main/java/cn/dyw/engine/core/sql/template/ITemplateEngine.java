package cn.dyw.engine.core.sql.template;


import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exception.RenderSqlException;

import java.util.List;

/**
 * sql渲染引擎
 *
 * @author dyw770
 * @since 2021-08-02
 */
public interface ITemplateEngine {

    /**
     * 渲染SQL
     *
     * @param context 上下文
     * @return SQL渲染结果
     * @throws RenderSqlException 渲染异常
     */
    SqlSources process(ExecContext context) throws RenderSqlException;

    /**
     * 解析模板SQL中的参数
     *
     * @param templateSql 模板SQL
     * @return 参数
     */
    List<String> analysisTemplate(String templateSql);
}

