package cn.dyw.engine.core.exec.plugin;

import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.context.SelectExecContext;
import cn.dyw.engine.core.exception.EngineExecException;
import cn.dyw.engine.core.exec.EngineConfiguration;
import cn.dyw.engine.core.exec.EngineExecutor;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.exec.result.DataSetResult;
import cn.dyw.engine.core.exec.result.PageDataSetResult;
import cn.dyw.engine.core.model.DataPageOption;
import cn.dyw.engine.core.model.PageType;
import cn.dyw.engine.core.sql.template.SqlSources;
import cn.dyw.engine.core.utils.DatabaseResultSetUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 分页插件
 *
 * @author dyw770
 * @since 2022-10-16
 */
@Slf4j
public class PageSqlExecPlugin implements SqlExecPlugin {

    public static String TMP_COUNT_SQL_AlIAS = "tmp_count_table";

    public static String PAGE_PARAM_OFFSET_NAME = "offset";

    public static String PAGE_PARAM_SIZE_NAME = "size";

    @Override
    public ExecResult doExec(ExecContext context, EngineExecutor executor, ExecPluginChain chain) {

        // 指明不分页的情况 直接调用下个插件
        SelectExecContext selectExecContext = (SelectExecContext) context;
        if (!needPage(selectExecContext)) {
            return chain.doChain();
        }

        DataPageOption dataPage = selectExecContext.getDataPageOption();
        PageType pageType = dataPage.getPageType();

        if (pageType == PageType.fullCustom) {
            return fullCustom(selectExecContext, executor, chain, dataPage);
        } else {
            return simplePage(selectExecContext, executor, chain, dataPage);
        }
    }

    @Override
    public boolean canSupport(ExecContext context, EngineExecutor executor, ExecPluginChain chain) {
        if (context instanceof SelectExecContext) {
            return needPage((SelectExecContext) context);
        } else {
            return false;
        }

    }

    /**
     * 简单分页方式
     * 统计sql自动生成
     * 分页sql自动生成
     *
     * @param context  context
     * @param executor executor
     * @param chain    chain
     * @param dataPage dataPage
     * @return 结果
     */
    private ExecResult simplePage(SelectExecContext context,
                                  EngineExecutor executor,
                                  ExecPluginChain chain,
                                  DataPageOption dataPage) {
        Long count = execCount(context, executor);
        // 满足分页 调用下个插件
        if (count > 0 && totalPageSize(dataPage.getSize(), count) >= dataPage.getPage()) {
            createPageSql(context, executor);
            ExecResult execResult = chain.doChain();

            return buildPageResult(context, execResult, count);
        } else {

            // 不满足分页 放弃执行查询 这里后续需要增加配置 是否执行
            // TODO: 增加在总条数不足的情况下 是否再次执行查询语句
            log.info("简单分页执行结果，总数量 {} 小于等于 0， 或者总页数 {} 小于请求页数 {}, 取消执行", count, totalPageSize(dataPage.getSize(), count), dataPage.getPage());
            return new PageDataSetResult(context, 0);
        }
    }

    /**
     * 全自定义分页方式
     * 统计sql模版渲染生成
     * 分页sql模版渲染生成
     *
     * @param context  context
     * @param executor executor
     * @param chain    chain
     * @param dataPage dataPage
     * @return 结果
     */
    private ExecResult fullCustom(SelectExecContext context,
                                  EngineExecutor executor,
                                  ExecPluginChain chain,
                                  DataPageOption dataPage) {
        // 渲染自定义统计sql
        long count = execCustomCount(context, executor);

        if (count > 0 && totalPageSize(dataPage.getSize(), count) >= dataPage.getPage()) {
            // 将分页参数注入到变量里面
            addPageParams(context, dataPage);
            // 重新生成sql
            executor.applyContext();
            ExecResult execResult = chain.doChain();
            return buildPageResult(context, execResult, count);
        } else {
            // 不满足分页 放弃执行查询 这里后续需要增加配置 是否执行
            // TODO: 增加在总条数不足的情况下 是否再次执行查询语句
            log.info("自定义分页执行结果，总数量 {} 小于等于 0， 或者总页数 {} 小于请求页数 {}, 取消执行", count, totalPageSize(dataPage.getSize(), count), dataPage.getPage());
            return new PageDataSetResult(context, 0);
        }
    }

    private long execCustomCount(SelectExecContext context, EngineExecutor executor) {
        String countSql = context.getCustomCountSql();
        if (StringUtils.isBlank(countSql)) {
            throw new EngineExecException("自定义分页方式需要配置自定义统计sql");
        }
        SelectExecContext countContext = new SelectExecContext(countSql, null);
        countContext.addParameter(context.getParameters());
        SqlSources countSources = executor.templateEngine().process(countContext);
        log.debug("执行自定义统计SQL: {}", countSources.render());

        long count;
        try (PreparedStatement statement = countSources.prepareStatement(executor);
             ResultSet resultSet = statement.executeQuery()) {

            Object o = DatabaseResultSetUtils.handleOneResult(resultSet);
            count = o == null ? 0L : (Long) o;
        } catch (SQLException e) {
            throw new EngineExecException("执行自定义 count sql 失败", e);
        }
        return count;
    }

    private ExecResult buildPageResult(SelectExecContext selectExecContext, ExecResult execResult, Long count) {
        // 如果是 DataSetResult 就直接修改内容
        if (execResult instanceof DataSetResult dataSetResult) {
            return new PageDataSetResult(dataSetResult, count);
        } else {
            DataSetResult dataSetResult = new DataSetResult(selectExecContext);
            try {
                BeanUtils.copyProperties(dataSetResult, execResult);
                return dataSetResult;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new EngineExecException("转换分页结果失败", e);
            }
        }
    }

    private void addPageParams(SelectExecContext context, DataPageOption dataPage) {
        DynamicFilterParameter pageParam = new DynamicFilterParameter(PAGE_PARAM_OFFSET_NAME,
                buildOffset(dataPage, context.getConfiguration()));
        DynamicFilterParameter sizeParam = new DynamicFilterParameter(PAGE_PARAM_SIZE_NAME,
                dataPage.getSize() > 0 ? dataPage.getSize() : context.getConfiguration().getDefaultPageArgs().getSize());
        context.addParameter(pageParam);
        context.addParameter(sizeParam);
    }

    private Long execCount(SelectExecContext context, EngineExecutor executor) {

        SqlSources sqlSources = executor.sqlSources();
        String sql = executor.sqlSources().original();

        String countSql = buildCountSql(sql, context.getDbType());

        try (PreparedStatement statement = sqlSources.prepareStatement(countSql, executor);
             ResultSet resultSet = statement.executeQuery()) {

            Object o = DatabaseResultSetUtils.handleOneResult(resultSet);
            return (Long) o;
        } catch (SQLException e) {
            throw new EngineExecException("执行 count sql 失败", e);
        }
    }


    private void createPageSql(SelectExecContext context, EngineExecutor executor) {

        DataPageOption pageArgs = context.getDataPageOption();
        SqlSources sqlSources = executor.sqlSources();

        if (pageArgs == null && context.getConfiguration().isMissPage()) {
            pageArgs = context.getConfiguration().getDefaultPageArgs();
        }

        if (pageArgs != null && pageArgs.isNeedPage()) {
            String renderSql = sqlSources.render();

            SQLSelectStatement stmt = (SQLSelectStatement) SQLUtils.parseSingleStatement(renderSql, context.getDbType());
            SQLSelectQuery query = stmt.getSelect().getQuery();

            if (query instanceof SQLSelectQueryBlock sqb) {
                SQLLimit limit = sqb.getLimit();
                if (limit == null) {
                    SQLLimit sqlLimit = buildSqlLimit(pageArgs, context);
                    sqb.setLimit(sqlLimit);
                    String pageSql = SQLUtils.toSQLString(sqb, context.getDbType());
                    log.debug("分页插件, 渲染分页: {}", pageSql);

                    sqlSources.apply(pageSql);
                }
            } else if (query instanceof SQLUnionQuery suq) {
                SQLLimit limit = suq.getLimit();
                if (limit == null) {
                    SQLLimit sqlLimit = buildSqlLimit(pageArgs, context);
                    suq.setLimit(sqlLimit);
                    String pageSql = SQLUtils.toSQLString(suq, context.getDbType());
                    log.debug("分页插件, unino sql 渲染分页: {}", pageSql);

                    sqlSources.apply(pageSql);
                }
            } else {
                throw new EngineExecException("未支持的查询SQL类型: " + query.getClass().getName());
            }
        }
    }

    private SQLLimit buildSqlLimit(DataPageOption pageArgs, SelectExecContext context) {
        SQLLimit sqlLimit = new SQLLimit();
        sqlLimit.setOffset(buildOffset(pageArgs, context.getConfiguration()));
        sqlLimit.setRowCount(pageArgs.getSize() > 0 ? pageArgs.getSize() : context.getConfiguration().getDefaultPageArgs().getSize());
        return sqlLimit;
    }

    private int buildOffset(DataPageOption dataPage, EngineConfiguration configuration) {
        int page = dataPage.getPage() > 0 ? dataPage.getPage() : 1;
        int size = dataPage.getSize() > 0 ? dataPage.getSize() : configuration.getDefaultPageArgs().getSize();

        return size * (page - 1);
    }

    private String buildCountSql(String sql, String dbType) {
        DbType db = DbType.valueOf(dbType);
        SQLSelectStatement stmt = (SQLSelectStatement) SQLUtils.parseSingleStatement(sql, db);
        SQLSelectQueryBlock sqb = new SQLSelectQueryBlock(db);

        sqb.setFrom(stmt.getSelect(), TMP_COUNT_SQL_AlIAS);
        sqb.addSelectItem(SQLUtils.toSelectItem("count(*)", db));
        String countSql = SQLUtils.toSQLString(sqb, db);
        log.info("渲染统计SQL：[{}]", countSql);
        return countSql;
    }

    private boolean needPage(SelectExecContext context) {
        DataPageOption pageArgs = context.getDataPageOption();
        // 是否加载默认参数
        if (pageArgs == null && context.getConfiguration().isMissPage()) {
            pageArgs = context.getConfiguration().getDefaultPageArgs();
        }

        return pageArgs != null && pageArgs.isNeedPage();
    }

    private long totalPageSize(int size, Long total) {
        if (total % size == 0) {
            return total / size;
        } else {
            return (total / size) + 1;
        }
    }
}
