package cn.dyw.auth.db.model;

import cn.dyw.auth.db.domain.SysUser;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-18
 */
@ToString
public class UserDto extends SysUser {

    /**
     * 角色列表
     */
    @Getter
    private final List<String> roles = new ArrayList<>();
}
