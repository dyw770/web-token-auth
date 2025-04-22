package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysApiAccessLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * api访问日志表 Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Mapper
public interface SysApiAccessLogMapper extends BaseMapper<SysApiAccessLog> {

}

