package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysPermission;
import cn.dyw.auth.db.model.SysMenuPermissionDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2025-05-24
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询菜单子权限列表
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    List<SysMenuPermissionDto> queryMenuPermissionList(@Param("menuId") Integer menuId);
}

