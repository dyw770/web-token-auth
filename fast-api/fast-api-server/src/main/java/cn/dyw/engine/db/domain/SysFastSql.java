package cn.dyw.engine.db.domain;

import cn.dyw.engine.core.configuration.DataFieldBind;
import cn.dyw.engine.core.configuration.DataPageOption;
import cn.dyw.engine.core.configuration.DataSortField;
import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.StatementType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author dyw770
 * @since 2026-03-25
 */
@Getter
@Setter
@ToString
@TableName(value = "sys_fast_sql", autoResultMap = true)
public class SysFastSql {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String sqlName;

    /**
     * 描述
     */
    private String sqlDescribe;

    /**
     * sql模板
     */
    private String sqlTemplate;

    /**
     * 自定义计数sql
     */
    private String customCountSql;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 语句类型
     */
    private StatementType statementType;

    /**
     * 排序字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private List<DataSortField> sortFields;

    /**
     * 参数
     */
    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private List<DynamicFilterParameter> parameters;

    /**
     * 分页参数
     */
    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private DataPageOption dataPage;

    /**
     * 扩展字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private Map<String, Object> extend;

    /**
     * 数据字段绑定
     */
    @TableField(typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private List<DataFieldBind> dataFieldBinds;
}
