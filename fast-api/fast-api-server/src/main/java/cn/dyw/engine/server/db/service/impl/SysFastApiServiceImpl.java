package cn.dyw.engine.server.db.service.impl;

import cn.dyw.auth.exception.ExtensionBusinessException;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.engine.server.db.domain.SysFastApi;
import cn.dyw.engine.server.db.mapper.SysFastApiMapper;
import cn.dyw.engine.server.db.service.ISysFastApiService;
import cn.dyw.engine.server.message.rq.ApiCreateRq;
import cn.dyw.engine.server.message.rq.ApiEditRq;
import cn.dyw.engine.server.message.rq.ApiSearchRq;
import cn.dyw.engine.server.model.FastApi;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2026-03-25
 */
@Service
public class SysFastApiServiceImpl extends ServiceImpl<SysFastApiMapper, SysFastApi> implements ISysFastApiService {
    

    @Override
    public Page<SysFastApi> queryList(ApiSearchRq rq) {
        Page<SysFastApi> page = rq.toPage();
        return this.lambdaQuery()
                .likeRight(StringUtils.isNotBlank(rq.getApiName()), SysFastApi::getApiName, rq.getApiName())
                .likeRight(StringUtils.isNotBlank(rq.getApiDescribe()), SysFastApi::getApiDescribe, rq.getApiDescribe())
                .likeRight(StringUtils.isNotBlank(rq.getApiPath()), SysFastApi::getApiPath, rq.getApiPath())
                .page(page);
    }

    @Override
    public void addApi(ApiCreateRq rq) {
        SysFastApi sysFastApi = new SysFastApi();
        BeanUtils.copyProperties(rq, sysFastApi);
        sysFastApi.setCreateTime(LocalDateTime.now());
        sysFastApi.setUpdateTime(LocalDateTime.now());
        this.save(sysFastApi);
    }

    @Override
    public void updateApi(ApiEditRq rq) {
        SysFastApi existingApi = this.getById(rq.getId());
        if (Objects.isNull(existingApi)) {
            throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "API不存在, 无法更新");
        }

        SysFastApi sysFastApi = new SysFastApi();
        BeanUtils.copyProperties(rq, sysFastApi);
        sysFastApi.setUpdateTime(LocalDateTime.now());
        this.updateById(sysFastApi);
    }

    @Override
    public void deleteApi(Integer id) {
        SysFastApi existingApi = this.getById(id);
        if (Objects.isNull(existingApi)) {
            throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "API不存在, 无法删除");
        }
        this.removeById(id);
    }

    @Override
    public void deleteApiBySqlId(Integer sqlId) {
        this.lambdaUpdate()
                .eq(SysFastApi::getSysSql, sqlId)
                .remove();
    }

    @Override
    public FastApi queryApi(String path) {
        return getBaseMapper().queryApiByPath(path);
    }
}
