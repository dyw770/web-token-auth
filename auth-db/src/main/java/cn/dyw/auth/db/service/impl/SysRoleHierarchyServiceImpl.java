package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysRoleHierarchy;
import cn.dyw.auth.db.mapper.SysRoleHierarchyMapper;
import cn.dyw.auth.db.service.ISysRoleHierarchyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@Service
@Transactional
public class SysRoleHierarchyServiceImpl extends ServiceImpl<SysRoleHierarchyMapper, SysRoleHierarchy>
        implements ISysRoleHierarchyService {

    @Override
    public void savaRoleHierarchy(String roleCode, String parentRoleCode) {
        getBaseMapper().savaRoleHierarchy(roleCode, parentRoleCode);
    }

    @Override
    public void deleteRoleHierarchy(String roleCode) {
        // 将该节点的所有子节点都提升1层
        getBaseMapper().updateRoleHierarchyToParent(roleCode);
        // 再删除该节点
        getBaseMapper().deleteRoleHierarchy(roleCode);
    }

    @Override
    public void updateRoleHierarchyToRole(String roleCode, String parentRoleCode) {
        // 删除该节点及其子节点的父信息，使其成为孤立节点
        getBaseMapper().deleteRoleParentHierarchy(roleCode);

        // 新增父节点信息
        getBaseMapper().updateRoleParentHierarchy(roleCode, parentRoleCode);
    }
}




