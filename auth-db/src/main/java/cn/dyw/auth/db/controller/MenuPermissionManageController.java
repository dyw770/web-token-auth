package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysMenuPermission;
import cn.dyw.auth.db.message.rq.MenuPermissionSaveRq;
import cn.dyw.auth.db.service.ISysMenuPermissionService;
import cn.dyw.auth.message.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单权限管理
 *
 * @author dyw770
 * @since 2025-05-19
 */
@Validated
@RestController
@RequestMapping("${app.auth.jdbc.api-context-path:/admin}/menu/permission")
public class MenuPermissionManageController {

    private final ISysMenuPermissionService menuPermissionService;

    public MenuPermissionManageController(ISysMenuPermissionService menuPermissionService) {
        this.menuPermissionService = menuPermissionService;
    }

    /**
     * 查询菜单列权限池
     *
     * @return 权限池
     */
    @GetMapping("list/{menuId}")
    public Result<List<SysMenuPermission>> menuPermissionList(@PathVariable("menuId") Integer menuId) {
        List<SysMenuPermission> list = menuPermissionService.lambdaQuery()
                .eq(SysMenuPermission::getMenuId, menuId)
                .list();
        return Result.createSuccess(list);
    }

    /**
     * 新增菜单的权限池
     * @param rq 请求参数
     * @return 结果
     */
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated MenuPermissionSaveRq rq) {
        SysMenuPermission sysMenuPermission = new SysMenuPermission();
        BeanUtils.copyProperties(rq, sysMenuPermission);
        sysMenuPermission.setUpdateTime(LocalDateTime.now());
        sysMenuPermission.setCreateTime(LocalDateTime.now());
        menuPermissionService.save(sysMenuPermission);
        return Result.createSuccess();
    }
    
    /**
     * 删除菜单的权限
     * @param menuId 菜单id
     * @param permissionId 权限id
     * @return 结果
     */
    @DeleteMapping("delete/{menuId}/{permissionId}")
    public Result<Void> delete(@PathVariable("menuId") Integer menuId, @PathVariable("permissionId") String permissionId) {
        menuPermissionService.lambdaUpdate()
                .eq(SysMenuPermission::getMenuId, menuId)
                .eq(SysMenuPermission::getPermissionId, permissionId)
                .remove();
        return Result.createSuccess();
    }
    
    /**
     * 更新菜单的权限
     * @param rq 请求参数
     * @return 结果
     */
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated MenuPermissionSaveRq rq) {
        menuPermissionService.lambdaUpdate()
                .eq(SysMenuPermission::getMenuId, rq.getMenuId())
                .eq(SysMenuPermission::getPermissionId, rq.getPermissionId())
                .set(SysMenuPermission::getPermissionDesc, rq.getPermissionDesc())
                .set(SysMenuPermission::getUpdateTime, LocalDateTime.now())
                .update();
        return Result.createSuccess();
    }

}
