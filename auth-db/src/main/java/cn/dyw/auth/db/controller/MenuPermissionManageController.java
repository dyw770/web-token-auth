package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysMenuPermission;
import cn.dyw.auth.db.domain.SysRoleMenuPermission;
import cn.dyw.auth.db.message.rq.MenuPermissionSaveRq;
import cn.dyw.auth.db.message.rq.RoleMenuPermissionSaveRq;
import cn.dyw.auth.db.message.rs.RoleMenuPermission;
import cn.dyw.auth.db.service.ISysMenuPermissionService;
import cn.dyw.auth.db.service.ISysRoleMenuPermissionService;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private final ISysRoleMenuPermissionService roleMenuPermissionService;

    public MenuPermissionManageController(ISysMenuPermissionService menuPermissionService,
                                          ISysRoleMenuPermissionService roleMenuPermissionService) {
        this.menuPermissionService = menuPermissionService;
        this.roleMenuPermissionService = roleMenuPermissionService;
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
     *
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
     *
     * @param menuId       菜单id
     * @param permissionId 权限id
     * @return 结果
     */
    @DeleteMapping("delete/{menuId}/{permissionId}")
    public Result<Void> delete(@PathVariable("menuId") Integer menuId, @PathVariable("permissionId") String permissionId) {
        menuPermissionService.removeMenuPermission(menuId, permissionId);
        return Result.createSuccess();
    }

    /**
     * 更新菜单的权限
     *
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

    /**
     * 查询角色的菜单权限
     *
     * @param roleCode 角色ID
     * @param menuId   菜单ID
     * @return 结果
     */
    @GetMapping("role")
    public Result<RoleMenuPermission> roleMenuPermission(@RequestParam("roleCode") @NotBlank String roleCode,
                                                         @RequestParam("menuId") @NotNull @Min(1) Integer menuId) {

        RoleMenuPermission roleMenuPermission = new RoleMenuPermission();
        List<SysMenuPermission> menuPermissions = menuPermissionService.lambdaQuery()
                .eq(SysMenuPermission::getMenuId, menuId)
                .list();
        List<SysRoleMenuPermission> roleMenuPermissions = roleMenuPermissionService.lambdaQuery()
                .eq(SysRoleMenuPermission::getRoleCode, roleCode)
                .eq(SysRoleMenuPermission::getMenuId, menuId)
                .list();

        roleMenuPermission.setMenuPermissions(menuPermissions);
        roleMenuPermission.setRolePermissions(roleMenuPermissions);
        return Result.createSuccess(roleMenuPermission);
    }

    /**
     * 保存角色的菜单权限
     *
     * @param rq 请求参数
     * @return 结果
     */
    @PostMapping("role/save")
    public Result<Void> roleMenuPermissionSave(@RequestBody @Validated RoleMenuPermissionSaveRq rq) {

        Long count = menuPermissionService.lambdaQuery()
                .eq(SysMenuPermission::getMenuId, rq.getMenuId())
                .eq(SysMenuPermission::getPermissionId, rq.getPermissionId())
                .count();
        if (count <= 0) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "菜单权限" + rq.getPermissionId() + "不存在");
        }

        Long rolePermissionCount = roleMenuPermissionService.lambdaQuery()
                .eq(SysRoleMenuPermission::getRoleCode, rq.getRoleCode())
                .eq(SysRoleMenuPermission::getMenuId, rq.getMenuId())
                .eq(SysRoleMenuPermission::getPermissionId, rq.getPermissionId())
                .count();
        if (rolePermissionCount > 0) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "角色菜单权限" + rq.getPermissionId() + "已存在");
        }

        SysRoleMenuPermission sysRoleMenuPermission = new SysRoleMenuPermission();
        sysRoleMenuPermission.setRoleCode(rq.getRoleCode());
        sysRoleMenuPermission.setMenuId(rq.getMenuId());
        sysRoleMenuPermission.setPermissionId(rq.getPermissionId());
        sysRoleMenuPermission.setAuthTime(LocalDateTime.now());
        roleMenuPermissionService.save(sysRoleMenuPermission);
        return Result.createSuccess();
    }

    /**
     * 删除角色的菜单权限
     *
     * @param authId 授权ID
     * @return 结果
     */
    @DeleteMapping("role/delete")
    public Result<Void> roleMenuPermissionDelete(@RequestParam("authId") @NotNull @Min(1) Integer authId) {
        roleMenuPermissionService.removeById(authId);
        return Result.createSuccess();
    }
}
