package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysMenus;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.service.ISysMenusService;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@Validated
@RestController
@RequestMapping("menu")
public class MenuController {

    private final ISysMenusService menusService;

    public MenuController(ISysMenusService menusService) {
        this.menusService = menusService;
    }

    /**
     * 查询菜单列表，返回树状结构数据
     *
     * @return 菜单列表
     */
    @GetMapping("list")
    public Result<List<MenuDto>> menuList() {
        return Result.createSuccess(menusService.menuList());
    }

    /**
     * 保存菜单信息
     *
     * @param menu         菜单信息
     * @param parentMenuId 父级菜单ID
     * @return 保存成功
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody SysMenus menu, @RequestParam(value = "parentMenuId", required = false) Integer parentMenuId) {
        if (ObjectUtils.isNotEmpty(parentMenuId)) {
            SysMenus menus = menusService.getById(parentMenuId);
            if (ObjectUtils.isEmpty(menus)) {
                return Result.createFail(MessageCode.PARAM_ERROR, "父级菜单不存在", null);
            }
        }
        menusService.savaMenu(menu, parentMenuId);
        return Result.createSuccess();
    }

    /**
     * 更新菜单信息
     *
     * @param menus 菜单信息
     * @return 更新成功
     */
    @PostMapping("/update/info")
    public Result<Void> updateMenu(@RequestBody SysMenus menus) {
        menusService.updateMenu(menus);
        return Result.createSuccess();
    }

    /**
     * 更新菜单的层级关系
     *
     * @param menuId       菜单ID
     * @param parentMenuId 新菜单ID
     * @return 更新成功
     */
    @GetMapping("/update/hierarchy")
    public Result<Void> updateMenuHierarchy(@RequestParam("menuId") Integer menuId,
                                            @RequestParam(value = "parentMenuId", required = false) Integer parentMenuId) {
        if (ObjectUtils.isNotEmpty(parentMenuId)) {
            SysMenus menus = menusService.getById(parentMenuId);
            if (ObjectUtils.isEmpty(menus)) {
                return Result.createFail(MessageCode.PARAM_ERROR, "父级菜单不存在", null);
            }
        }
        menusService.updateMenuHierarchy(menuId, parentMenuId);
        return Result.createSuccess();
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 删除成功
     */
    @DeleteMapping("/delete/{menuId}")
    public Result<Void> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menusService.deleteMenu(menuId);
        return Result.createSuccess();
    }

    /**
     * 查询角色菜单列表
     *
     * @param roleCode 角色
     * @return 菜单列表
     */
    @GetMapping("/list/role/{roleCode}")
    public Result<List<MenuDto>> roleMenuList(@PathVariable("roleCode") @NotBlank String roleCode) {
        return Result.createSuccess(menusService.roleMenuList(roleCode));
    }

    /**
     * 查询用户菜单列表
     *
     * @param username 用户名
     * @return 菜单列表
     */
    @GetMapping("/list/user/{username}")
    public Result<List<MenuDto>> userMenuList(@PathVariable("username") @NotBlank String username) {
        return Result.createSuccess(menusService.userMenuList(username));
    }

    /**
     * 为角色添加菜单
     *
     * @param menuIds  菜单ID集合
     * @param roleCode 角色
     * @return 添加成功
     */
    @PostMapping("/add/role")
    public Result<Void> addMenuForRole(@RequestBody @NotEmpty List<Integer> menuIds,
                                       @RequestParam("roleCode") @NotBlank String roleCode) {

        menusService.addMenuForRole(menuIds, roleCode);
        return Result.createSuccess();
    }

    /**
     * 删除角色菜单
     *
     * @param menuIds  菜单ID集合， 允许传空集合， 表示删除所有菜单
     * @param roleCode 角色
     * @return 删除成功
     */
    @DeleteMapping("/delete/role")
    public Result<Void> deleteMenuForRole(@RequestBody List<Integer> menuIds,
                                          @RequestParam("roleCode") @NotBlank String roleCode) {
        menusService.deleteMenuForRole(menuIds, roleCode);
        return Result.createSuccess();
    }
}
