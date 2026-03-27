package cn.dyw.engine.server.message.rq;

import cn.dyw.engine.core.configuration.DataFieldBind;
import cn.dyw.engine.core.configuration.DataPageOption;
import cn.dyw.engine.core.configuration.DataSortField;
import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.StatementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Map;

/**
 * 编辑SQL模板请求
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Data
public class SqlEditRq {

    /**
     * ID
     */
    @NotNull
    private Integer id;

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
    @NotEmpty
    private List<DataSortField> sortFields;

    /**
     * 参数
     */
    @NotEmpty
    private List<DynamicFilterParameter> parameters;

    /**
     * 分页参数
     */
    @NotNull
    private DataPageOption dataPage;

    /**
     * 扩展字段
     */
    @NotEmpty
    private Map<String, Object> extend;

    /**
     * 数据字段绑定
     */
    @NotEmpty
    private List<DataFieldBind> dataFieldBinds;
}