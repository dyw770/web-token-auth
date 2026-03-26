package cn.dyw.engine.core.exec.result;

import cn.dyw.engine.core.context.SelectExecContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dyw770
 * @since 2022-12-12
 */
public class SingleLineExecResult extends SelectResult<Map<String, Object>> {


    private final Map<String, Object> data;

    public SingleLineExecResult(SelectExecContext context) {
        super(context);
        data = new HashMap<>(128);
    }

    @Override
    public Map<String, Object> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "data \n" + data.toString();
    }

    /**
     * 添加数据
     *
     * @param data 数据
     */
    public void addData(Map<String, Object> data) {
        this.data.putAll(data);
    }

    /**
     * 添加数据
     *
     * @param key   key
     * @param value value
     */
    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

}
