package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.domain.SysApiAccessLog;
import cn.dyw.auth.db.mapper.SysApiAccessLogMapper;
import cn.dyw.auth.db.service.ISysApiAccessLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * api访问日志表 服务实现类
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Service
public class SysApiAccessLogServiceImpl extends ServiceImpl<SysApiAccessLogMapper, SysApiAccessLog> implements ISysApiAccessLogService {

}
