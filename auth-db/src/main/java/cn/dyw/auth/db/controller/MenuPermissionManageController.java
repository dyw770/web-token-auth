package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysMenuPermission;
import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.domain.SysRolePermission;
import cn.dyw.auth.db.message.rq.MenuPermissionSaveRq;
import cn.dyw.auth.db.message.rq.RoleMenuPermissionSaveRq;
import cn.dyw.auth.db.message.rs.RoleMenuPermissionRs;
import cn.dyw.auth.db.model.SysMenuPermissionDto;
import cn.dyw.auth.db.service.ISysMenuPermissionService;
import cn.dyw.auth.db.service.ISysPermissionService;
import cn.dyw.auth.db.service.ISysRolePermissionService;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单权限管理
 *
 * @author dyw770
 * @since 2025-05-19
 */
@Validated
@RestController
@RequestMapping("${app.auth.api-context-path:/admin}/menu/permission")
public class MenuPermissionManageController {

    private final ISysMenuPermissionService menuPermissionService;


    private final ISysPermissionService permissionService;

    private final ISysRolePermissionService rolePermissionService;

    public MenuPermissionManageController(ISysMenuPermissionService menuPermissionService,
                                          ISysPermissionService permissionService,
                                          ISysRolePermissionService rolePermissionService) {
        this.menuPermissionService = menuPermissionService;
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
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
    @Transactional
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated MenuPermissionSaveRq rq) {
        SysPermission id = permissionService.getById(rq.getPermissionId());
        if (ObjectUtils.isNotEmpty(id)) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "权限" + rq.getPermissionId() + "已存在");
        }

        menuPermissionService.addMenuPermission(rq);
        return Result.createSuccess();
    }

    /**
     * 删除菜单的权限
     *
     * @param menuId       菜单id
     * @param permissionId 权限id
     * @return 结果
     */
    @Transactional
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
        permissionService.lambdaUpdate()
                .eq(SysPermission::getPermissionId, rq.getPermissionId())
                .set(SysPermission::getPermissionDesc, rq.getPermissionDesc())
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
    public Result<RoleMenuPermissionRs> roleMenuPermission(@RequestParam("roleCode") @NotBlank String roleCode,
                                                           @RequestParam("menuId") @NotNull @Min(1) Integer menuId) {
        //TODO: 优化
        RoleMenuPermissionRs roleMenuPermission = new RoleMenuPermissionRs();
        List<SysMenuPermissionDto> menuPermissions = permissionService.menuPermission(menuId);
        List<SysPermission> roleMenuPermissions =
                rolePermissionService.roleMenuPermissions(menuId, roleCode);

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

        //TODO: 优化
        Long count = permissionService.lambdaQuery()
                .eq(SysPermission::getPermissionId, rq.getPermissionId())
                .count();
        if (count <= 0) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "菜单权限" + rq.getPermissionId() + "不存在");
        }

        Long rolePermissionCount = rolePermissionService.lambdaQuery()
                .eq(SysRolePermission::getRoleCode, rq.getRoleCode())
                .eq(SysRolePermission::getPermissionId, rq.getPermissionId())
                .count();
        if (rolePermissionCount > 0) {
            return Result.createFailWithMsg(MessageCode.PARAM_ERROR, "角色菜单权限" + rq.getPermissionId() + "已存在");
        }

        rolePermissionService.addPermissionRoleAuth(rq.getRoleCode(), rq.getPermissionId());
        return Result.createSuccess();
    }
}
