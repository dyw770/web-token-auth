package cn.dyw.auth.db.model;

import cn.dyw.auth.db.domain.SysApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * api 资源
 *
 * @author dyw770
 * @since 2025-04-23
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApiResourceDto extends SysApiResource {

    /**
     * 资源授权角色
     */
    @Getter
    private List<String> roles = new ArrayList<>();
}
