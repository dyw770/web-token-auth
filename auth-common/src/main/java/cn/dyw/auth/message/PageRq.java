package cn.dyw.auth.message;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 分页参数
 *
 * @author dyw770
 * @since 2025-04-29
 */
@Data
public class PageRq {

    /**
     * 当前页码
     */
    @Min(1)
    private long page;

    /**
     * 每页数量
     */
    @Min(10)
    private long size;

    /**
     * 排序
     */
    private OrderRq[] orders;

    /**
     * 构建 mybatis plus 分页对象
     *
     * @param <T> T
     * @return 分页对象
     */
    public <T> Page<T> toPage() {
        Page<T> page = new Page<>();
        page.setCurrent(this.page);
        page.setSize(this.size);
        if (ArrayUtils.isNotEmpty(this.orders)) {
            page.addOrder(Arrays.stream(this.orders).map(OrderRq::toOrderItem).toList());
        }
        return page;
    }

    /**
     * 构建 mybatis plus 分页对象
     *
     * @param orders 排序
     * @param <T>    T
     * @return 分页对象
     */
    public <T> Page<T> toPage(OrderRq... orders) {
        Page<T> page = new Page<>();
        page.setCurrent(this.page);
        page.setSize(this.size);
        page.setOrders(Arrays.stream(orders)
                .map(OrderRq::toOrderItem)
                .filter(item -> StringUtils.isNotBlank(item.getColumn()))
                .toList());
        return page;
    }

}
