package cn.dyw.auth.db.message.rq;

import cn.dyw.auth.message.PageRq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dyw770
 * @since 2025-05-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSearchRq extends PageRq {

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
