package cn.dyw.engine.core.configuration;

/**
 * @author dyw770
 * @since 2022-09-28
 */
public enum PageType {

    /**
     * 自动分页
     */
    simple("simple"),

    /**
     * 全自定义
     */
    fullCustom("fullCustom");
    

    public final String name;
    
    PageType(String name) {
        this.name = name;
    }
}
