package cn.dyw.auth.utils;

import cn.dyw.auth.message.PageRq;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author dyw770
 * @since 2025-04-29
 */
public final class PageUtils {


    /**
     * 分页处理
     *
     * @param page page
     * @param size size
     * @param <T>  分页对象
     * @return 结果
     */
    public static <T> Page<T> createPage(Integer page, Integer size) {
        return new Page<>(page, size);
    }

    /**
     * 自定义分页执行
     *
     * @param page          分页结果返回对象
     * @param querySupplier 查询
     * @param countSupplier 统计
     * @param <T>           查询对象
     * @return 结果
     */
    public static <T> Page<T> queryPage(PageRq page, Supplier<List<T>> querySupplier, Supplier<Long> countSupplier) {
        Page<T> mybatisPage = page.toPage();
        long count = countSupplier.get();
        mybatisPage.setTotal(count);

        if (mybatisPage.offset() >= mybatisPage.getTotal()) {
            return mybatisPage.setRecords(new ArrayList<>());
        }

        List<T> data = querySupplier.get();
        
        mybatisPage.setRecords(data);
        return mybatisPage;
    }
}
