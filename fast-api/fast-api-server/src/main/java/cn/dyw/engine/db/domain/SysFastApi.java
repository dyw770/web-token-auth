package cn.dyw.engine.db.domain;

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
 * @since 2026-03-25
 */
@Getter
@Setter
@ToString
@TableName("sys_fast_api")
public class SysFastApi {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String apiName;

    /**
     * 描述
     */
    private String apiDescribe;

    /**
     * 路径
     */
    private String apiPath;

    /**
     * sql
     */
    private Integer sysSql;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
