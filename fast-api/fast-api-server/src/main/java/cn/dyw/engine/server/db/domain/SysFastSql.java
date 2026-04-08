package cn.dyw.engine.server.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
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
    private String statementType;

    /**
     * 排序字段
     */
    private String sortFields;

    /**
     * 参数
     */
    private String parameters;

    /**
     * 分页参数
     */
    private String dataPage;

    /**
     * 扩展字段
     */
    private String extend;

    /**
     * 数据字段绑定
     */
    private String dataFieldBinds;
}
