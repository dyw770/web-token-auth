package cn.dyw.auth.db.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * <p>
 * 权限表
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
@Getter
@Setter
@ToString
@TableName("sys_permission")
public class SysPermission {

    /**
     * 权限ID
     */
    @TableId("permission_id")
    private String permissionId;

    /**
     * 权限说明
     */
    private String permissionDesc;

    /**
     * 权限分类
     */
    private String permissionType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
