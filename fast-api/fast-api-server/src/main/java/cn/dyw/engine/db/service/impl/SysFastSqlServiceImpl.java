package cn.dyw.engine.db.service.impl;

import cn.dyw.auth.exception.ExtensionBusinessException;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.engine.db.domain.SysFastSql;
import cn.dyw.engine.db.mapper.SysFastSqlMapper;
import cn.dyw.engine.db.service.ISysFastSqlService;
import cn.dyw.engine.message.rq.SqlCreateRq;
import cn.dyw.engine.message.rq.SqlEditRq;
import cn.dyw.engine.message.rq.SqlSearchRq;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public void addSql(SqlCreateRq rq) {
        SysFastSql sysFastSql = new SysFastSql();
        BeanUtils.copyProperties(rq, sysFastSql);
        sysFastSql.setCreateTime(LocalDateTime.now());
        sysFastSql.setUpdateTime(LocalDateTime.now());
        this.save(sysFastSql);
    }

    @Override
    public void updateSql(SqlEditRq rq) {
        SysFastSql existingSql = this.getById(rq.getId());
        if (Objects.isNull(existingSql)) {
            throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "SQL模板不存在, 无法更新");
        }

        SysFastSql sysFastSql = new SysFastSql();
        BeanUtils.copyProperties(rq, sysFastSql);
        sysFastSql.setUpdateTime(LocalDateTime.now());
        this.updateById(sysFastSql);
    }

    @Override
    public void deleteSql(Integer id) {
        SysFastSql existingSql = this.getById(id);
        if (Objects.isNull(existingSql)) {
            throw new ExtensionBusinessException(MessageCode.PARAM_ERROR, "SQL模板不存在, 无法删除");
        }
        this.removeById(id);
    }
}
