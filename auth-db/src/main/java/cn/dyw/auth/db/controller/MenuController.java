package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysMenus;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.service.ISysMenusService;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.message.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-18
 */
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
    @PostMapping("/update/menu/info")
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
    @GetMapping("/update/menu/hierarchy")
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
    @GetMapping("/delete/{menuId}")
    public Result<Void> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menusService.deleteMenu(menuId);
        return Result.createSuccess();
    }
}
