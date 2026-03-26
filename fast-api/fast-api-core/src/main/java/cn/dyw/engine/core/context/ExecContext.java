package cn.dyw.engine.core.context;

import cn.dyw.engine.core.exec.EngineConfiguration;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行器上下午
 *
 * @author dyw770
 * @since 2022-09-19
 */
@Data
public abstract class ExecContext {

    /**
     * 参数
     */
    private final List<DynamicFilterParameter> parameters = new ArrayList<>(16);


    private final Map<String, Object> extend = new HashMap<>(16);

    /**
     * sql模板
     */
    private String sqlTemplate;
    
    /**
     * 语句类型
     */

    private StatementType statementType;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 全局配置
     */
    private EngineConfiguration configuration;

    public void addExtendParameter(String key, Object value) {
        extend.put(key, value);
    }

    public void addExtendParameter(Map<String, Object> map) {
        extend.putAll(map);
    }


    public void addParameter(DynamicFilterParameter parameter) {
        parameters.add(parameter);
    }

    public void addParameter(List<DynamicFilterParameter> parameters) {
        this.parameters.addAll(parameters);
    }

    public void removeParameter(DynamicFilterParameter parameter) {
        this.parameters.remove(parameter);
    }

    public void clearParameter() {
        this.parameters.clear();
    }
}
