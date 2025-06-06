package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysRole;
import cn.dyw.auth.db.message.rq.RoleSavaRq;
import cn.dyw.auth.db.message.rq.RoleUpdateRq;
import cn.dyw.auth.db.model.RoleDto;
import cn.dyw.auth.db.service.ICachedRoleService;
import cn.dyw.auth.db.service.ISysRoleService;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-17
 */
@RestController
@RequestMapping("${app.auth.api-context-path:/admin}/role")
public class RoleManageController {

    private final ISysRoleService sysRoleService;
    
    private final ICachedRoleService cachedRoleService;

    public RoleManageController(ISysRoleService sysRoleService, ICachedRoleService cachedRoleService) {
        this.sysRoleService = sysRoleService;
        this.cachedRoleService = cachedRoleService;
    }

    /**
     * 查询角色列表，返回树状结构数据
     *
     * @return 角色列表
     */
    @GetMapping("list")
    public Result<List<RoleDto>> roleList() {
        return Result.createSuccess(cachedRoleService.roleTree());
    }

    /**
     * 保存角色信息
     *
     * @param rq             角色信息
     * @param parentRoleCode 父级角色
     * @return 保存成功
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Validated RoleSavaRq rq,
                             @RequestParam(value = "parentRoleCode", required = false) String parentRoleCode) {
        if (StringUtils.isNotBlank(parentRoleCode)) {
            SysRole role = sysRoleService.getById(parentRoleCode);
            if (ObjectUtils.isEmpty(role)) {
                return Result.createFail(MessageCode.PARAM_ERROR, "父级角色不存在", null);
            }
        }

        SysRole role = sysRoleService.getById(rq.getRoleCode());
        if (ObjectUtils.isNotEmpty(role)) {
            return Result.createFail(MessageCode.PARAM_ERROR, "角色已存在", null);
        }

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(rq, sysRole);
        sysRole.setDel(false);
        sysRoleService.savaRole(sysRole, parentRoleCode);
        return Result.createSuccess();
    }

    /**
     * 更新角色信息
     *
     * @param rq 角色信息
     * @return 更新成功
     */
    @PostMapping("/update/info")
    public Result<Void> updateRole(@RequestBody @Validated RoleUpdateRq rq) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(rq, sysRole);
        sysRoleService.updateRole(sysRole);
        return Result.createSuccess();
    }

    /**
     * 更新角色的层级关系
     *
     * @param roleCode       角色
     * @param parentRoleCode 新父级角色
     * @return 更新成功
     */
    @GetMapping("/update/hierarchy")
    public Result<Void> updateRoleHierarchy(@RequestParam("roleCode") String roleCode,
                                            @RequestParam(value = "parentRoleCode", required = false, defaultValue = "") String parentRoleCode) {
        if (StringUtils.isNotBlank(parentRoleCode)) {
            SysRole role = sysRoleService.getById(parentRoleCode);
            if (ObjectUtils.isEmpty(role)) {
                return Result.createFail(MessageCode.PARAM_ERROR, "父级角色不存在", null);
            }
        }
        sysRoleService.updateRoleHierarchy(roleCode, parentRoleCode);
        return Result.createSuccess();
    }

    /**
     * 删除角色
     *
     * @param roleCode 角色
     * @return 删除成功
     */
    @GetMapping("/delete/{roleCode}")
    public Result<Void> deleteRole(@PathVariable("roleCode") @NotBlank String roleCode) {
        sysRoleService.deleteRole(roleCode);
        return Result.createSuccess();
    }
}
