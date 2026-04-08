package cn.dyw.engine.server.service.impl;

import cn.dyw.auth.exception.ExtensionBusinessException;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.engine.core.context.*;
import cn.dyw.engine.core.exception.EngineExecException;
import cn.dyw.engine.core.exec.EngineConfiguration;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.exec.IExecEngine;
import cn.dyw.engine.core.model.DataFieldBind;
import cn.dyw.engine.core.model.DataPageOption;
import cn.dyw.engine.core.model.DataSortField;
import cn.dyw.engine.server.db.domain.SysFastDataSource;
import cn.dyw.engine.server.db.domain.SysFastSql;
import cn.dyw.engine.server.db.service.ISysFastApiService;
import cn.dyw.engine.server.db.service.ISysFastDataSourceService;
import cn.dyw.engine.server.db.service.ISysFastSqlService;
import cn.dyw.engine.server.message.rq.ExecParameterRq;
import cn.dyw.engine.server.message.rq.ExecRq;
import cn.dyw.engine.server.model.FastApi;
import cn.dyw.engine.server.service.IQueryService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author dyw770
 * @since 2026-03-27
 */
@Service
public class QueryServiceImpl implements IQueryService {

    private final IExecEngine engine;

    private final EngineConfiguration configuration;

    private final ISysFastSqlService sqlService;

    private final ISysFastApiService apiService;

    private final ISysFastDataSourceService dataSourceService;

    public QueryServiceImpl(IExecEngine engine,
                            EngineConfiguration configuration,
                            ISysFastSqlService sqlService,
                            ISysFastApiService apiService, ISysFastDataSourceService dataSourceService) {
        this.engine = engine;
        this.configuration = configuration;
        this.sqlService = sqlService;
        this.apiService = apiService;
        this.dataSourceService = dataSourceService;
    }


    @Override
    public ExecResult execQuery(ExecRq rq) {
        ExecContext context = contextFactory(rq);
        return execResult(context);
    }

    @Override
    public ExecContext contextFactory(ExecRq rq) {
        StatementType statementType = rq.getStatementType();
        String sqlTemplate = rq.getSql();
        List<DynamicFilterParameter> parameters = rq.getParameters();
        ExecContext context;

        switch (statementType) {
            case select:
                context = new SelectExecContext(sqlTemplate, new HashMap<>(16));
                List<DataSortField> sorts = rq.getSortFields();
                DataPageOption dataPageOption = rq.getDataPage();
                List<DataFieldBind> fieldNames = rq.getDataFieldBinds();
                if (sorts == null) {
                    sorts = new ArrayList<>();
                }
                if (fieldNames == null) {
                    fieldNames = new ArrayList<>();
                }
                ((SelectExecContext) context).addSorts(sorts);
                ((SelectExecContext) context).addFieldNames(fieldNames);
                ((SelectExecContext) context).setDataPageOption(dataPageOption);

                break;
            case update:
                context = new UpdateExecContext(sqlTemplate);
                break;
            case delete:
                context = new DeleteExecContext(sqlTemplate);
                break;
            case insert:
                context = new InsertExecContext(sqlTemplate);
                break;
            default:
                throw new EngineExecException("无法生成对应的上下文");
        }

        if (parameters == null) {
            parameters = new ArrayList<>();
        }

        if (ObjectUtils.isNotEmpty(rq.getExtend())) {
            context.addExtendParameter(rq.getExtend());
        }
        context.addParameter(parameters);
        context.setConfiguration(configuration);

        context.setDataSource(rq.getDataSource());
        context.setDbType(dataSourceType(rq.getDataSource()));

        return context;
    }

    @Override
    public ExecResult execResult(ExecContext context) {
        return engine.exec(context);
    }

    @Override
    public ExecResult execSql(ExecParameterRq rq, Integer sqlId) {
        SysFastSql fastSql = sqlService.getById(sqlId);
        if (ObjectUtils.isEmpty(fastSql)) {
            throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "未找到对应的SQL配置");
        }

        return execQuery(createExecRq(rq, fastSql));
    }

    @Override
    public ExecResult execApi(ExecParameterRq rq, String path) {
        FastApi api = apiService.queryApi(path);
        if (ObjectUtils.isEmpty(api)) {
            throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "未找到对应的API配置");
        }

        SysFastSql fastSql = api.getFastSql();
        return execQuery(createExecRq(rq, fastSql));
    }


    private ExecRq createExecRq(ExecParameterRq rq, SysFastSql fastSql) {
        ExecRq execRq = new ExecRq();
        execRq.setSql(fastSql.getSqlTemplate());
        execRq.setStatementType(fastSql.getStatementType());
        execRq.setDataFieldBinds(fastSql.getDataFieldBinds());
        execRq.setCustomCountSql(fastSql.getCustomCountSql());
        execRq.setExtend(fastSql.getExtend());

        execRq.setParameters(rq.getParameters());
        execRq.setDataPage(rq.getDataPage());
        execRq.setSortFields(rq.getSortFields());
        execRq.setDataSource(fastSql.getDataSource());
        return execRq;
    }

    private String dataSourceType(String dataSource) {
        SysFastDataSource source = dataSourceService.getById(dataSource);
        if (ObjectUtils.isEmpty(source)) {
            throw new ExtensionBusinessException(MessageCode.ERROR, "未找到对应的数据源");
        }
        return source.getDbType();
    }
}
