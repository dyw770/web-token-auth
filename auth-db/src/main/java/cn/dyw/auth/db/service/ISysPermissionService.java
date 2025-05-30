package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.message.rq.PermissionSaveRq;
import cn.dyw.auth.db.model.SysMenuPermissionDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 新增权限
     * @param rq 请求参数
     * @param permissionType 权限类型
     */
    void savePermission(PermissionSaveRq rq, SysPermission.PermissionType permissionType);
    
    /**
     * 删除权限
     * @param permissions 权限ID集合
     */
    void removeMenuPermission(List<String>  permissions);

    /**
     * 菜单子权限
     * @param menuId 菜单ID
     */
    List<SysMenuPermissionDto> menuPermission(Integer menuId);
}
