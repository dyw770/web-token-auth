package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysUser;
import cn.dyw.auth.db.message.rq.UserSearchRq;
import cn.dyw.auth.db.message.rs.UserRs;
import cn.dyw.auth.db.model.UserDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 查询用户列表
     *
     * @param rq   查询参数
     * @param page 分页参数
     * @return 用户列表
     */
    Page<UserRs> userList(@Param("rq") UserSearchRq rq, @Param("page") Page<UserRs> page);

    /**
     * 查询用户列表计数
     *
     * @param rq 查询参数
     * @return 用户列表
     */
    long userListCount(@Param("rq") UserSearchRq rq);
}




