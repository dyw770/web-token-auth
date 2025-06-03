package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户角色授权表 Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    
    /**
     * 删除角色授权
     * 同时删除子角色授权
     *
     * @param username 用户名
     * @param roleCode 角色ID
     * @return 结果
     */
    int deleteRoleForUser(@Param("username") String username, @Param("roleCode") String roleCode);
}

