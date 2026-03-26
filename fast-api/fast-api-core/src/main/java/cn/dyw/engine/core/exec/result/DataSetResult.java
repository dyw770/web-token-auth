package cn.dyw.engine.core.exec.result;

import cn.dyw.engine.core.context.SelectExecContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 查询结果
 *
 * @author dyw770
 * @since 2021-08-03
 */
public class DataSetResult extends SelectResult<List<Map<String, Object>>> {

    private final List<Map<String, Object>> data;

    public DataSetResult(SelectExecContext context) {
        super(context);
        data = new ArrayList<>(128);
    }

    @Override
    public List<Map<String, Object>> getData() {
        return data;
    }

    /**
     * 添加数据
     *
     * @param data 数据
     */
    public void addData(Map<String, Object> data) {
        this.data.add(data);
    }


    /**
     * 添加数据
     *
     * @param datas 数据
     */
    public void addData(List<Map<String, Object>> datas) {
        this.data.addAll(datas);
    }

    public void cloneData(Consumer<List<Map<String, Object>>> consumer) {
        consumer.accept(data);
    }

    @Override
    public String toString() {
        return "data \n" + data.toString();
    }
}
