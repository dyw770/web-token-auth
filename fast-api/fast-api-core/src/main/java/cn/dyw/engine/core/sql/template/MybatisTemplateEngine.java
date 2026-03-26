package cn.dyw.engine.core.sql.template;

import cn.dyw.engine.core.context.ExecContext;
import cn.dyw.engine.core.exception.RenderSqlException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用mybatisSQL模板引擎
 *
 * @author dyw770
 * @since 2021-08-03
 */
@Slf4j
public class MybatisTemplateEngine implements ITemplateEngine {

    private Configuration configuration;

    private XMLLanguageDriver xmlLanguageDriver;

    private final Map<String, WeakReference<MyBatisSqlSourceFactory>> sqlSourceCache = new ConcurrentHashMap<>();


    private void init() {
        this.configuration = new Configuration();
        this.xmlLanguageDriver = new XMLLanguageDriver();
    }

    public MybatisTemplateEngine() {
        this.init();
    }

    @Override
    public SqlSources process(ExecContext context) throws RenderSqlException {
        WeakReference<MyBatisSqlSourceFactory> sourceFactoryWeakReference = sqlSourceCache.get(context.getSqlTemplate());
        MyBatisSqlSourceFactory sourceFactory;
        if (ObjectUtils.isEmpty(sourceFactoryWeakReference) || ObjectUtils.isEmpty(sourceFactoryWeakReference.get())) {
            SqlSource sqlSource = xmlLanguageDriver.createSqlSource(configuration,
                    "<script>" + context.getSqlTemplate() + "</script>",
                    null);
            sourceFactory = new MyBatisSqlSourceFactory(sqlSource, configuration);
            sourceFactoryWeakReference = new WeakReference<>(sourceFactory);
            sqlSourceCache.put(context.getSqlTemplate(), sourceFactoryWeakReference);
        } else {
            sourceFactory = sourceFactoryWeakReference.get();
        }
        return sourceFactory.applyContext(context);
    }

    @Override
    public List<String> analysisTemplate(String templateSql) {
        // TODO: 解析参数名
        return null;
    }


    static class MyBatisSqlSourceFactory {

        private final SqlSource sqlSource;

        private final Configuration configuration;

        public MyBatisSqlSourceFactory(SqlSource sqlSource, Configuration configuration) {
            this.sqlSource = sqlSource;
            this.configuration = configuration;
        }

        /**
         * 应用上下文
         *
         * @param context 上下文
         * @return 结果
         */
        public SqlSources applyContext(ExecContext context) {
            return new MyBatisSqlSource(this.sqlSource, this.configuration, context);
        }
    }
}


