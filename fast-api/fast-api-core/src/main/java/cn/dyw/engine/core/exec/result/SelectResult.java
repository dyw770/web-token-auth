package cn.dyw.engine.core.exec.result;

import cn.dyw.engine.core.configuration.DataFieldBind;
import cn.dyw.engine.core.configuration.DataSortField;
import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.SelectExecContext;
import cn.dyw.engine.core.exec.ExecResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询结果
 *
 * @param <T> 数据
 * @author dyw770
 */
public abstract class SelectResult<T> extends ExecResult {

    /**
     * 字段列表
     */
    @Getter
    private final List<String> schema;

    @Getter
    private final SelectExecContext context;

    public SelectResult(SelectExecContext context) {
        schema = new ArrayList<>(32);
        this.context = context;
    }

    /**
     * 返回数据
     *
     * @return 数据
     */
    public abstract T getData();

    /**
     * 返回字段映射
     *
     * @return 字段映射
     */
    public Map<String, DataFieldBind> getHeaderNames() {
        return context.getFieldNames();
    }

    /**
     * 返回排序方式
     *
     * @return 排序方式
     */
    public List<DataSortField> getSorts() {
        return context.getSorts();
    }

    /**
     * 返回一些额外的参数
     *
     * @return 参数
     */
    public Map<String, Object> getExtend() {
        return context.getExtend();
    }

    /**
     * 返回参数
     *
     * @return 字段映射
     */
    public List<DynamicFilterParameter> getParameters() {
        return context.getParameters();
    }

    /**
     * 初始化字段
     *
     * @param schema 字段列表
     */
    public void initSchema(List<String> schema) {
        this.schema.addAll(schema);
    }

}
