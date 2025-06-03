package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysUserRole;
import cn.dyw.auth.db.mapper.SysUserRoleMapper;
import cn.dyw.auth.db.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色授权表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public void deleteRoleForUser(String username, String roleCode) {
        getBaseMapper().deleteRoleForUser(username, roleCode);
    }
}
