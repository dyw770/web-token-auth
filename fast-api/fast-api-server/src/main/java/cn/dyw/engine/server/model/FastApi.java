package cn.dyw.engine.server.model;

import cn.dyw.engine.server.db.domain.SysFastApi;
import cn.dyw.engine.server.db.domain.SysFastSql;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author dyw770
 * @since 2026-03-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FastApi extends SysFastApi {
    
    private SysFastSql fastSql;
}
