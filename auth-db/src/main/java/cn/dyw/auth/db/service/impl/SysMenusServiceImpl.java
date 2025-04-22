package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysMenus;
import cn.dyw.auth.db.domain.SysRoleMenu;
import cn.dyw.auth.db.mapper.SysMenusMapper;
import cn.dyw.auth.db.model.MenuDto;
import cn.dyw.auth.db.service.ISysMenuHierarchyService;
import cn.dyw.auth.db.service.ISysMenusService;
import cn.dyw.auth.db.service.ISysRoleMenuService;
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

    public SysMenusServiceImpl(ISysMenuHierarchyService menuHierarchyService, ISysRoleMenuService roleMenuService) {
        this.menuHierarchyService = menuHierarchyService;
        this.roleMenuService = roleMenuService;
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
    }

    @Override
    public List<MenuDto> menuList() {
        List<MenuDto> menuList = getBaseMapper().queryMenuList();

        return treeMenu(menuList);
    }

    @Override
    public List<MenuDto> roleMenuList(String roleCode) {
        List<MenuDto> menuDtoList = getBaseMapper().queryRoleMenuList(roleCode);
        return treeMenu(menuDtoList);
    }

    @Override
    public List<MenuDto> userMenuList(String username) {
        List<MenuDto> menuDtoList = getBaseMapper().queryUserMenuList(username);
        return treeMenu(menuDtoList);
    }

    @Override
    public void deleteMenuForRole(List<Integer> menuIds, String roleCode) {
        // 删除角色的菜单授权时同时要删除其子角色的授权
        getBaseMapper().deleteMenuForRole(menuIds, roleCode);
    }

    @Override
    public void addMenuForRole(List<Integer> menuIds, String roleCode) {
        // 增加角色菜单授权的同时会把该菜单的父级菜单都授权给该角色
        getBaseMapper().insertMenuForRole(menuIds, roleCode);
    }

    private List<MenuDto> treeMenu(List<MenuDto> menuDtoList) {

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
