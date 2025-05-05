package cn.dyw.auth.message;

import cn.dyw.auth.exception.BusinessException;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author dyw770
 * @since 2025-04-29
 */
@Data
public class OrderRq {

    /**
     * 需要进行排序的字段
     */
    @NotBlank
    private String column;

    /**
     * 是否正序排列，默认 true
     */
    private boolean asc = true;

    /**
     * 数据库中真实的列名
     *
     * @return 列名
     */
    public String getSqlColumn() {
        if (!SqlInjectionUtils.check(this.column)) {
            return StringUtils.camelToUnderline(column);
        } else {
            throw new BusinessException(MessageCode.SQL_CHECK_ERROR);
        }
    }

    public OrderItem toOrderItem() {
        return asc ? OrderItem.asc(getSqlColumn()) : OrderItem.desc(StringUtils.camelToUnderline(getSqlColumn()));
    }
}
