package cn.dyw.engine.server.message.rq;

import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.StatementType;
import cn.dyw.engine.core.model.DataFieldBind;
import cn.dyw.engine.core.model.DataPageOption;
import cn.dyw.engine.core.model.DataSortField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Map;

/**
 * 执行sql配置
 *
 * @author dyw770
 * @since 2026-3-27
 */
@Data
public class ExecRq {

    /**
     * sql
     */
    @NotBlank
    @Length(max = 2000)
    private String sql;

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
     * 结果表头配置
     */
    @NotNull
    private List<DataFieldBind> dataFieldBinds;

    /**
     * 分页配置
     */
    @NotNull
    private DataPageOption dataPage;

    /**
     * 语句类型
     */
    @NotNull
    private StatementType statementType;

    /**
     * 自定义统计sql
     */
    @Length(max = 2000)
    private String customCountSql;

    /**
     * 扩展参数
     */
    @NotNull
    private Map<String, Object> extend;

    /**
     * 数据源
     */
    @NotBlank
    @Length(min = 1, max = 32)
    private String dataSource;
}
