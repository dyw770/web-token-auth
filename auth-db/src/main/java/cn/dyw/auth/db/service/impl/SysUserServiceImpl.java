package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysRole;
import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.domain.SysUserRole;
import cn.dyw.auth.db.mapper.SysUserMapper;
import cn.dyw.auth.db.message.rq.UserSearchRq;
import cn.dyw.auth.db.message.rs.UserRs;
import cn.dyw.auth.db.model.UserDto;
import cn.dyw.auth.db.service.ISysRoleService;
import cn.dyw.auth.db.service.ISysUserRoleService;
import cn.dyw.auth.db.service.ISysUserService;
import cn.dyw.auth.exception.ExtensionBusinessException;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.utils.PageUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements ISysUserService {

    private final ISysRoleService roleService;

    private final ISysUserRoleService userRoleService;

    public SysUserServiceImpl(ISysRoleService roleService, ISysUserRoleService userRoleService) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }


    @Override
    public UserDto getUserByUsername(String username) {
        return getBaseMapper().getUserByUsername(username);
    }

    @Override
    public void addRoleForUser(String username, String roleCode) {
        List<String> userRoles = roleService.userRoleList(username);
        if (userRoles.contains(roleCode)) {
            log.warn("{} 用户已经拥有 {} 角色（或者间接拥有）", username, roleCode);
            return;
        }
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUsername(username);
        sysUserRole.setRoleCode(roleCode);
        sysUserRole.setAuthTime(LocalDateTime.now());
        userRoleService.save(sysUserRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRoleForUser(String username, List<String> roleCodes) {
        List<String> userRoles = roleService.userRoleList(username);
        List<String> targetRole = roleCodes
                .stream()
                .filter(roleCode -> !userRoles.contains(roleCode))
                .distinct()
                .toList();

        if (!CollectionUtils.isEmpty(targetRole)) {
            Long count = roleService.lambdaQuery()
                    .in(SysRole::getRoleCode, targetRole)
                    .count();
            if (count != targetRole.size()) {
                log.warn("存在非法角色参数");
                throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "存在非法角色参数");
            }

            List<SysUserRole> list = targetRole.stream()
                    .map(roleCode -> {
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setUsername(username);
                        sysUserRole.setRoleCode(roleCode);
                        sysUserRole.setAuthTime(LocalDateTime.now());
                        sysUserRole.setExpiredTime(LocalDateTime.now());
                        return sysUserRole;
                    })
                    .toList();

            userRoleService.saveBatch(list);
        }

        // 删除撤销的授权
        List<String> deleteRoleCode = userRoles.stream()
                .filter(roleCode -> !roleCodes.contains(roleCode))
                .distinct()
                .toList();
        if (!CollectionUtils.isEmpty(deleteRoleCode)) {
            userRoleService.lambdaUpdate()
                    .in(SysUserRole::getRoleCode, deleteRoleCode)
                    .eq(SysUserRole::getUsername, username)
                    .remove();
        }
    }

    @Override
    public void deleteRoleForUser(String username, String roleCode) {
        userRoleService.lambdaUpdate()
                .eq(SysUserRole::getUsername, username)
                .eq(SysUserRole::getRoleCode, roleCode)
                .remove();
    }

    @Override
    public Page<UserRs> userList(UserSearchRq rq) {
        return PageUtils.queryPage(rq,
                () -> getBaseMapper().userList(rq, rq),
                () -> getBaseMapper().userListCount(rq));
    }
}




