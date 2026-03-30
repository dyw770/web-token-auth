package cn.dyw.engine.server.service.impl;

import cn.dyw.engine.core.context.*;
import cn.dyw.engine.core.exception.EngineExecException;
import cn.dyw.engine.core.exec.EngineConfiguration;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.exec.IExecEngine;
import cn.dyw.engine.core.model.DataFieldBind;
import cn.dyw.engine.core.model.DataPageOption;
import cn.dyw.engine.core.model.DataSortField;
import cn.dyw.engine.server.message.rq.ExecSqlRq;
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

    public QueryServiceImpl(IExecEngine engine, EngineConfiguration configuration) {
        this.engine = engine;
        this.configuration = configuration;
    }


    @Override
    public ExecResult execQuery(ExecSqlRq rq) {
        ExecContext context = contextFactory(rq);
        return execResult(context);
    }

    @Override
    public ExecContext contextFactory(ExecSqlRq rq) {
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
        // TODO 先固定写
        context.setDbType("mysql");
        return context;
    }

    @Override
    public ExecResult execResult(ExecContext context) {
        return engine.exec(context);
    }
}
