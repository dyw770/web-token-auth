package cn.dyw.engine.core.exec.plugin;

import cn.dyw.engine.core.configuration.DataSortField;
import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.context.SelectExecContext;
import cn.dyw.engine.core.exec.EngineExecutor;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.sql.template.SqlSources;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 排序插件
 *
 * @author dyw770
 * @since 2022-09-27
 */
@Slf4j
public class SortSqlExecPlugin implements SqlExecPlugin {

    private String buildSort(SelectExecContext context, String sql) {
        List<DataSortField> sorts = context.getSorts();
        if (sorts.isEmpty()) {
            return sql;
        }

        DbType dbType = DbType.valueOf(context.getDbType());

        SQLSelectStatement sqb = (SQLSelectStatement) SQLUtils.parseSingleStatement(sql, dbType);

        SQLSelectQueryBlock queryBlock = sqb.getSelect().getQueryBlock();

        for (DataSortField execSort : sorts) {
            queryBlock.addOrderBy(SQLUtils.toOrderByItem(execSort.getFieldName() + " "
                    + (execSort.isAsc() ? SQLOrderingSpecification.ASC : SQLOrderingSpecification.DESC), dbType));
        }

        String orderSql = SQLUtils.toSQLString(sqb, dbType);
        log.debug("排序插件 渲染排序： {}", orderSql);

        return orderSql;
    }

    @Override
    public ExecResult doExec(ExecContext context, EngineExecutor executor, ExecPluginChain chain) {
        SqlSources sqlSources = executor.sqlSources();
        String s = buildSort((SelectExecContext) context, sqlSources.render());
        sqlSources.apply(s);

        return chain.doChain();
    }

    @Override
    public boolean canSupport(ExecContext context, EngineExecutor executor, ExecPluginChain chain) {
        return context instanceof SelectExecContext sc && !sc.getSorts().isEmpty();
    }
}
