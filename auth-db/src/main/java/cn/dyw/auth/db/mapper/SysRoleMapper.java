package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysRole;
import cn.dyw.auth.db.model.RoleDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询角色列表
     *
     * @return 全部角色
     */
    List<RoleDto> queryRoleList();

    /**
     * 根据用户名查询角色列表
     * @param username 用户名
     * @return 角色列表
     */
    List<String> queryRoleCodeByUser(@Param("username") String username);
}




