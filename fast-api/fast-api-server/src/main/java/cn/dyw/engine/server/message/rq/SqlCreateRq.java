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
 * 创建SQL模板请求
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Data
public class SqlCreateRq {

    /**
     * 名称
     */
    @NotBlank
    @Length(min = 1, max = 32)
    private String sqlName;

    /**
     * 描述
     */
    @NotBlank
    @Length(min = 1, max = 128)
    private String sqlDescribe;

    /**
     * SQL模板
     */
    @NotBlank
    @Length(min = 1, max = 2000)
    private String sqlTemplate;

    /**
     * 自定义计数SQL
     */
    @Length(max = 2000)
    private String customCountSql;

    /**
     * 语句类型
     */
    @NotNull
    private StatementType statementType;

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
     * 分页参数
     */
    @NotNull
    private DataPageOption dataPage;

    /**
     * 扩展字段
     */
    @NotNull
    private Map<String, Object> extend;

    /**
     * 数据字段绑定
     */
    @NotNull
    private List<DataFieldBind> dataFieldBinds;

    /**
     * 数据源名称
     */
    @Length(max = 32)
    private String dataSource;
}