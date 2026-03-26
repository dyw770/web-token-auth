package cn.dyw.engine.db.service.impl;

import cn.dyw.engine.db.domain.SysFastSql;
import cn.dyw.engine.db.mapper.SysFastSqlMapper;
import cn.dyw.engine.db.service.ISysFastSqlService;
import cn.dyw.engine.message.rq.SqlSearchRq;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2026-03-25
 */
@Service
public class SysFastSqlServiceImpl extends ServiceImpl<SysFastSqlMapper, SysFastSql> implements ISysFastSqlService {

    @Override
    public Page<SysFastSql> queryList(SqlSearchRq rq) {
        Page<SysFastSql> page = rq.toPage();
        return this.lambdaQuery()
                .likeRight(StringUtils.isNotBlank(rq.getSqlName()), SysFastSql::getSqlName, rq.getSqlName())
                .likeRight(StringUtils.isNotBlank(rq.getSqlDescribe()), SysFastSql::getSqlDescribe, rq.getSqlDescribe())
                .page(page);
    }
}
