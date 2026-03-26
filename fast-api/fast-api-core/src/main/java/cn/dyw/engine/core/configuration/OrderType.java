package cn.dyw.engine.core.configuration;

import lombok.Getter;

/**
 * 排序方式
 *
 * @author dyw770
 * @since 2022-09-28
 */
public enum OrderType {

    /**
     * 升序
     */
    ASC("ASC"),

    /**
     * 降序
     */
    DESC("DESC");

    @Getter
    private final String name;
    
    OrderType(String name) {
        this.name = name;
    }
}
