package cn.dyw.engine.core.sql.template;

import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exception.MissArgSqlException;
import cn.dyw.engine.core.exception.RenderSqlException;
import cn.dyw.engine.core.exception.SqlException;
import cn.dyw.engine.core.exec.EngineExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dyw770
 * @since 2022-09-27
 */
@Slf4j
public class MyBatisSqlSource implements SqlSources {

    private final Map<String, Object> namedParams = new HashMap<>();

    private final Configuration configuration;

    private final BoundSql boundSql;

    private String sql;

    public MyBatisSqlSource(SqlSource sqlSource, Configuration configuration, ExecContext context) {
        this.configuration = configuration;
        for (DynamicFilterParameter parameter : context.getParameters()) {
            if (ObjectUtils.isNotEmpty(parameter) && StringUtils.isNotEmpty(parameter.getParameterName())) {
                this.namedParams.put(parameter.getParameterName(), parameter.getParameterValue());
            }
        }
        this.boundSql = sqlSource.getBoundSql(namedParams);
        this.sql = boundSql.getSql();
        log.debug("生成渲染后SQL: {}", sql);
    }

    @Override
    public String render() {
        return sql;
    }

    @Override
    public String original() {
        return this.boundSql.getSql();
    }

    @Override
    public void apply(String sql) {
        this.sql = sql;
    }

    @Override
    public PreparedStatement prepareStatement(EngineExecutor executor) {
        return prepareStatement(render(), executor);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public PreparedStatement prepareStatement(String sql, EngineExecutor executor) throws SqlException {
        try {

            PreparedStatement ps = executor.createStatement(sql);

            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

            // 用于对象类型参数的属性取值，目前用不到
            // MetaObject metaObject = configuration.newMetaObject(namedParams);

            if (parameterMappings != null) {
                for (int i = 0; i < parameterMappings.size(); i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    if (parameterMapping.getMode() != ParameterMode.OUT) {

                        String propertyName = parameterMapping.getProperty();

                        Object value;
                        if (!namedParams.containsKey(propertyName)) {
                            if (ObjectUtils.isEmpty(boundSql.getAdditionalParameter(propertyName))) {
                                log.debug("处理SQL失败, 缺少必要的参数 [{}]. sql: {}", propertyName, sql);
                                throw new MissArgSqlException("缺少SQL参数：" + propertyName);
                            } else {
                                value = boundSql.getAdditionalParameter(propertyName);
                            }
                        } else {
                            value = namedParams.get(propertyName);
                        }

                        TypeHandler typeHandler = parameterMapping.getTypeHandler();
                        JdbcType jdbcType = parameterMapping.getJdbcType();
                        if (value == null && jdbcType == null) {
                            jdbcType = configuration.getJdbcTypeForNull();
                        }
                        log.debug("添加参数, name: {}, value: {}, index: {}", propertyName, value, i + 1);
                        typeHandler.setParameter(ps, i + 1, value, jdbcType);
                    }
                }
            }
            return ps;
        } catch (SQLException e) {
            throw new RenderSqlException("预处理SQL失败", e);
        }
    }
}
