package cn.dyw.engine.server.db.domain;

import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.StatementType;
import cn.dyw.engine.core.model.DataFieldBind;
import cn.dyw.engine.core.model.DataPageOption;
import cn.dyw.engine.core.model.DataSortField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyw770
 * @since 2026-04-02
 */
@Getter
@Setter
@ToString
@TableName("sys_fast_sql")
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
    private List<DataSortField> sortFields;

    /**
     * 参数
     */
    private List<DynamicFilterParameter> parameters;

    /**
     * 分页参数
     */
    private DataPageOption dataPage;

    /**
     * 扩展字段
     */
    private Map<String, Object> extend;

    /**
     * 数据字段绑定
     */
    private List<DataFieldBind> dataFieldBinds;
    
    /**
     * 数据源
     */
    private String dataSource;
}
