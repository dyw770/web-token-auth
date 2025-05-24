package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.domain.SysRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色权限授权表 Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    List<SysPermission> queryRolePermissions(String roleCode);
}

