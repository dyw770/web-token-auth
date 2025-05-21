package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysMenuPermission;
import cn.dyw.auth.db.domain.SysMenus;
import cn.dyw.auth.db.domain.SysRoleMenu;
import cn.dyw.auth.db.domain.SysRoleMenuPermission;
import cn.dyw.auth.db.mapper.SysMenusMapper;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.model.MenuPermissionDto;
import cn.dyw.auth.db.model.MenuRoleDto;
import cn.dyw.auth.db.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Service
@Transactional
public class SysMenusServiceImpl extends ServiceImpl<SysMenusMapper, SysMenus> implements ISysMenusService {

    private final ISysMenuHierarchyService menuHierarchyService;

    private final ISysRoleMenuService roleMenuService;

    private final ISysMenuPermissionService menuPermissionService;

    private final ISysRoleMenuPermissionService roleMenuPermissionService;

    public SysMenusServiceImpl(ISysMenuHierarchyService menuHierarchyService,
                               ISysRoleMenuService roleMenuService,
                               ISysMenuPermissionService menuPermissionService,
                               ISysRoleMenuPermissionService roleMenuPermissionService) {
        this.menuHierarchyService = menuHierarchyService;
        this.roleMenuService = roleMenuService;
        this.menuPermissionService = menuPermissionService;
        this.roleMenuPermissionService = roleMenuPermissionService;
    }


    @Override
    public void savaMenu(SysMenus menu, Integer parentMenuId) {
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        save(menu);

        menuHierarchyService.savaMenuHierarchy(menu.getId(), parentMenuId);
    }

    @Override
    public void updateMenu(SysMenus menu) {
        menu.setCreateTime(null);
        menu.setUpdateTime(LocalDateTime.now());
        updateById(menu);
    }

    @Override
    public void updateMenuHierarchy(Integer menuId, Integer parentMenuId) {
        lambdaUpdate()
                .eq(SysMenus::getId, menuId)
                .set(SysMenus::getUpdateTime, LocalDateTime.now())
                .update();

        menuHierarchyService.updateMenuHierarchyToMenu(menuId, parentMenuId);
    }

    @Override
    public void deleteMenu(Integer menuId) {
        // 删除菜单
        lambdaUpdate().eq(SysMenus::getId, menuId)
                .remove();
        // 删除菜单的层级关系
        menuHierarchyService.deleteMenuHierarchy(menuId);
        // 删除菜单的授权
        roleMenuService.lambdaUpdate()
                .eq(SysRoleMenu::getMenuId, menuId)
                .remove();
        // 删除菜单的权限池和已经授权的权限
        menuPermissionService.lambdaUpdate()
                .eq(SysMenuPermission::getMenuId, menuId)
                .remove();
        roleMenuPermissionService.lambdaUpdate()
                .eq(SysRoleMenuPermission::getMenuId, menuId)
                .remove();
    }

    @Override
    public List<MenuDto> menuList() {
        List<MenuDto> menuList = getBaseMapper().queryMenuList();

        return treeMenu(menuList);
    }

    @Override
    public List<MenuRoleDto> menuRoleList() {
        List<MenuRoleDto> menuRoles = getBaseMapper().queryMenuRoleList();
        return treeMenu(menuRoles);
    }

    @Override
    public List<MenuDto> roleMenuList(String roleCode) {
        List<MenuDto> menuDtoList = getBaseMapper().queryRoleMenuList(roleCode);
        return treeMenu(menuDtoList);
    }

    @Override
    public List<MenuPermissionDto> userMenuTreeList(String username) {
        List<MenuPermissionDto> menuDtoList = getBaseMapper().queryUserMenuList(username);
        return treeMenu(menuDtoList);
    }

    @Override
    public List<MenuPermissionDto> userMenuList(String username) {
        return getBaseMapper().queryUserMenuList(username);
    }

    @Override
    public void deleteMenuForRole(List<Integer> menuIds, String roleCode) {
        // 删除角色的菜单授权时同时要删除其子角色的授权
        getBaseMapper().deleteMenuForRole(menuIds, roleCode);
        // 删除角色菜单子权限授权
        roleMenuPermissionService.lambdaUpdate()
                .eq(SysRoleMenuPermission::getRoleCode, roleCode)
                .in(SysRoleMenuPermission::getMenuId, menuIds)
                .remove();
    }

    @Override
    public void addMenuForRole(List<Integer> menuIds, String roleCode) {
        // 增加角色菜单授权的同时会把该菜单的父级菜单和子菜单都授权给该角色
        getBaseMapper().insertMenuForRole(menuIds, roleCode);
    }

    private <T extends MenuDto> List<T> treeMenu(List<T> menuDtoList) {
        Map<Integer, MenuDto> dtoMap = menuDtoList
                .stream()
                .collect(
                        Collectors
                                .toMap(MenuDto::getId, dto -> dto)
                );

        return menuDtoList
                .stream()
                .peek(dto -> {
                    if (ObjectUtils.isNotEmpty(dto.getParentMenuId())) {
                        MenuDto menuDto = dtoMap.get(dto.getParentMenuId());
                        if (ObjectUtils.isNotEmpty(menuDto)) {
                            menuDto.addChildren(dto);
                        }
                    }
                }).filter(
                        dto -> ObjectUtils.isEmpty(dto.getParentMenuId())
                )
                .sorted(Comparator.comparing(SysMenus::getMenuOrder))
                .toList();
    }
}
