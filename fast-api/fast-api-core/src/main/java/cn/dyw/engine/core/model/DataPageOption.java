package cn.dyw.engine.core.model;

import lombok.Data;

/**
 * @author dyw770
 * @since 2022-09-29
 */
@Data
public class DataPageOption {

    private PageType pageType = PageType.simple;

    private int page = 0;

    private int size = 20;

    private boolean needPage = false;
}
