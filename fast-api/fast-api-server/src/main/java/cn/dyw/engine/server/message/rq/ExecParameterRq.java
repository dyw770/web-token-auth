package cn.dyw.engine.server.message.rq;

import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.model.DataPageOption;
import cn.dyw.engine.core.model.DataSortField;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 *
 * 执行参数
 *
 * @author dyw770
 * @since 2026-03-30
 */
@Data
public class ExecParameterRq {

    /**
     * 排序字段
     */
    @NotNull
    private List<DataSortField> sortFields;

    /**
     * 参数
     */
    @NotNull
    private List<DynamicFilterParameter> parameters;

    /**
     * 分页配置
     */
    @NotNull
    private DataPageOption dataPage;
}
