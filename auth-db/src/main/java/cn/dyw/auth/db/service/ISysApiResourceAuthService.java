package cn.dyw.auth.db.service;

import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.message.rq.ApiResourceAuthAddRq;
import cn.dyw.auth.db.message.rq.ApiResourceAuthUpdateRq;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * api资源授权表 服务类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-24
 */
public interface ISysApiResourceAuthService extends IService<SysApiResourceAuth> {

    /**
     * 删除资源授权
     *
     * @param authType   授权类型
     * @param authObject 授权对象
     */
    void removeResourceAuth(SysApiResourceAuth.AuthType authType, String authObject);

    /**
     * 删除资源授权
     *
     * @param authType    授权类型
     * @param authObjects 授权对象
     */
    void removeResourceAuth(SysApiResourceAuth.AuthType authType, List<String> authObjects);

    /**
     * 删除授权
     *
     * @param authId 授权ID
     */
    void removeAuth(Integer authId);

    /**
     * 更新授权
     *
     * @param rq 更新授权参数
     */
    void updateAuth(ApiResourceAuthUpdateRq rq);

    /**
     * 新增授权
     *
     * @param rq 请求参数
     */
    void saveAuth(ApiResourceAuthAddRq rq);

    /**
     * 删除所有授权
     *
     * @param resourceId 资源ID
     */
    void removeAllAuth(Integer resourceId);
}
