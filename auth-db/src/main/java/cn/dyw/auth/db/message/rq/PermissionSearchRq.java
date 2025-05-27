package cn.dyw.auth.db.message.rq;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.message.PageRq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dyw770
 * @since 2025-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionSearchRq extends PageRq {


    /**
     * 权限ID
     */
    private String permissionId;

    /**
     * 权限类型
     */
    private SysPermission.PermissionType permissionType;
    
}
