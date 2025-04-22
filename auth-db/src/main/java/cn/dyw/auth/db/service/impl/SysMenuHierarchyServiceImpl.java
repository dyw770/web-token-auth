package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysMenuHierarchy;
import cn.dyw.auth.db.mapper.SysMenuHierarchyMapper;
import cn.dyw.auth.db.service.ISysMenuHierarchyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 菜单层级表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Service
@Transactional
public class SysMenuHierarchyServiceImpl extends ServiceImpl<SysMenuHierarchyMapper, SysMenuHierarchy> implements ISysMenuHierarchyService {


    @Override
    public void savaMenuHierarchy(Integer menuId, Integer parentMenuId) {
        getBaseMapper().savaMenuHierarchy(menuId, parentMenuId);
    }

    @Override
    public void deleteMenuHierarchy(Integer menuId) {
        // 将该节点的所有子节点都提升1层
        getBaseMapper().updateMenuHierarchyToParent(menuId);
        // 再删除该节点
        getBaseMapper().deleteMenuHierarchy(menuId);
    }

    @Override
    public void updateMenuHierarchyToMenu(Integer menuId, Integer parentMenuId) {
        // 删除该节点及其子节点的父信息，使其成为孤立节点
        getBaseMapper().deleteMenuParentHierarchy(menuId);

        // 新增父节点信息
        getBaseMapper().updateMenuParentHierarchy(menuId, parentMenuId);
    }
}
