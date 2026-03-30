package cn.dyw.engine.core.context;

import cn.dyw.engine.core.model.DataFieldBind;
import cn.dyw.engine.core.model.DataPageOption;
import cn.dyw.engine.core.model.DataSortField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 执行器上下文
 *
 * @author dyw770
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SelectExecContext extends ExecContext {

    /**
     * 排序字段
     */
    private final List<DataSortField> sorts = new ArrayList<>(16);

    /**
     * 映射字段
     */
    private Map<String, DataFieldBind> fieldNames;
    
    /**
     * 自定义countSql
     */
    private String customCountSql;

    /**
     * 分页参数
     */
    private DataPageOption dataPageOption;

    public SelectExecContext(String sqlTemplate, Map<String, DataFieldBind> fieldNames) {
        setSqlTemplate(sqlTemplate);
        this.fieldNames = fieldNames;
        setStatementType(StatementType.select);
    }


    public void addSort(DataSortField sort) {
        this.sorts.add(sort);
    }

    public void addSorts(List<DataSortField> sorts) {
        this.sorts.addAll(sorts);
    }

    public void addFieldName(DataFieldBind dataField) {
        this.fieldNames.put(dataField.getHeaderField(), dataField);
    }

    public void addFieldNames(List<DataFieldBind> dataFields) {
        for (DataFieldBind dataField : dataFields) {
            this.fieldNames.put(dataField.getHeaderField(), dataField);
        }
    }

    public void removeSort(DataSortField sort) {
        this.sorts.remove(sort);
    }

    public void clearSort() {
        this.sorts.clear();
    }
}
