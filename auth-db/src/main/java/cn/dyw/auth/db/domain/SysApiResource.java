package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * api资源表
 * </p>
 *
 * @author dyw770
 * @since 2025-04-24
 */
@Getter
@Setter
@ToString
@TableName("sys_api_resource")
public class SysApiResource {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * api路径
     */
    private String apiPath;

    /**
     * 匹配类型
     */
    private MatchType matchType;

    /**
     * api方法
     */
    private String apiMethod;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 资源匹配类型
     */
    public enum MatchType {
        /**
         * 正则
         */
        REGEX,
        /**
         * ant
         */
        ANT
    }
}
