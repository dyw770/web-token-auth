package cn.dyw.engine.core.configuration;

import lombok.Data;

/**
 * 排序字段
 *
 * @author dyw770
 * @since 2021-08-09
 */
@Data
public class DataSortField {

    /**
     * 排序字段名
     */
    private String fieldName;

    /**
     * 排序类型
     * 默认为 desc
     */
    private boolean asc = true;

    public DataSortField(String fieldName, boolean asc) {
        this.fieldName = fieldName;
        this.asc = asc;
    }

    public DataSortField(String fieldName) {
        this.fieldName = fieldName;
    }
}
