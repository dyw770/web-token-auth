package cn.dyw.engine.core.context;

import lombok.Data;

/**
 * SQL模板渲染需要的动态参数
 *
 * @author dyw770
 * @since 2021-08-03
 */
@Data
public class DynamicFilterParameter {

    private String parameterName;

    private String parameterDesc;

    private Object parameterType;

    private Object parameterValue;

    public DynamicFilterParameter() {
    }

    public DynamicFilterParameter(String parameterName, Object parameterValue) {
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public DynamicFilterParameter(String parameterName, String parameterDesc, Object parameterType, Object parameterValue) {
        this.parameterName = parameterName;
        this.parameterDesc = parameterDesc;
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
    }
}
