package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysMenuPermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dyw770
 * @since 2025-05-19
 */
public interface ISysMenuPermissionService extends IService<SysMenuPermission> {

    void removeMenuPermission(Integer menuId, String permissionId);
}
