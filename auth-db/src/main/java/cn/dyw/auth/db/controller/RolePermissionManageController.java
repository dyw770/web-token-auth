package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.message.rq.PermissionSaveRq;
import cn.dyw.auth.db.message.rq.PermissionSearchRq;
import cn.dyw.auth.db.message.rq.PermissionUpdateRq;
import cn.dyw.auth.db.service.ISysPermissionService;
import cn.dyw.auth.db.service.ISysRolePermissionService;
import cn.dyw.auth.message.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-27
 */
@Validated
@RestController
@RequestMapping("${app.auth.api-context-path:/admin}/permission")
public class RolePermissionManageController {

    private final ISysPermissionService permissionService;

    private final ISysRolePermissionService rolePermissionService;


    public RolePermissionManageController(ISysPermissionService permissionService, ISysRolePermissionService rolePermissionService) {
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
    }


    /**
     * 获取权限列表
     *
     * @return 权限列表
     */
    @PostMapping("list")
    public Result<Page<SysPermission>> list(@RequestBody PermissionSearchRq rq) {
        Page<SysPermission> page = permissionService.lambdaQuery()
                .like(StringUtils.isNotBlank(rq.getPermissionId()), SysPermission::getPermissionId, rq.getPermissionId())
                .eq(SysPermission::getPermissionType, SysPermission.PermissionType.GLOBAL)
                .eq(ObjectUtils.isNotEmpty(rq.getPermissionType()), SysPermission::getPermissionType, rq.getPermissionType())
                .page(rq.toPage());

        return Result.createSuccess(page);
    }

    /**
     * 获取权限列表
     *
     * @return 权限列表
     */
    @GetMapping("list/all")
    public Result<List<SysPermission>> list() {
        List<SysPermission> list = permissionService.list();
        return Result.createSuccess(list);
    }

    /**
     * 新增权限
     *
     * @param rq 权限信息
     * @return 结果
     */
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated PermissionSaveRq rq) {
        permissionService.savePermission(rq, SysPermission.PermissionType.GLOBAL);
        return Result.createSuccess();
    }

    /**
     * 删除权限
     *
     * @param permissionId 权限ID
     * @return 结果
     */
    @DeleteMapping("delete/{permissionId}")
    public Result<Void> delete(@PathVariable("permissionId") String permissionId) {
        permissionService.removeMenuPermission(List.of(permissionId));
        return Result.createSuccess();
    }

    /**
     * 删除角色授权
     *
     * @param roleCode     角色ID
     * @param permissionId 权限ID
     * @return 结果
     */
    @DeleteMapping("/auth/delete")
    public Result<Void> delete(@RequestParam("roleCode") String roleCode, @RequestParam("permissionId") String permissionId) {
        rolePermissionService.removeRolePermissions(roleCode, List.of(permissionId));
        return Result.createSuccess();
    }

    /**
     * 删除角色授权
     *
     * @param roleCode     角色ID
     * @param permissionId 权限ID
     * @return 结果
     */
    @GetMapping("/auth/add")
    public Result<Void> authAdd(@RequestParam("roleCode") String roleCode, @RequestParam("permissionId") String permissionId) {
        rolePermissionService.addPermissionRoleAuth(roleCode, permissionId);
        return Result.createSuccess();
    }

    /**
     * 更新权限
     *
     * @param rq 权限信息
     * @return 结果
     */
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated PermissionUpdateRq rq) {
        permissionService.lambdaUpdate()
                .eq(SysPermission::getPermissionId, rq.getPermissionId())
                .set(SysPermission::getPermissionDesc, rq.getPermissionDesc())
                .update();
        return Result.createSuccess();
    }

    /**
     * 获取权限授权角色列表
     *
     * @param permission 权限ID
     * @return 授权角色列表
     */
    @GetMapping("/auth/{permission}")
    public Result<List<String>> authRoles(@PathVariable("permission") @NotBlank String permission) {
        return Result.createSuccess(rolePermissionService.authRoles(permission));
    }
}
