package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.model.UserDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return 用户
     */
    UserDto getUserByUsername(@Param("username") String username);
}




