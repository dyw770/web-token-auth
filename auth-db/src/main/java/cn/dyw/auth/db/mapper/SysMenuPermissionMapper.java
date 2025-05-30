package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysMenuPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-19
 */
public interface SysMenuPermissionMapper extends BaseMapper<SysMenuPermission> {

    int deleteRoleMenuPermissions(@Param("menuIds") List<Integer> menuIds, @Param("roleCode") String roleCode);
}




