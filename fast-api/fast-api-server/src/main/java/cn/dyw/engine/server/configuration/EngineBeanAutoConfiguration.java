package cn.dyw.engine.server.configuration;

import cn.dyw.engine.core.exec.DefaultExecEngine;
import cn.dyw.engine.core.exec.EngineConfiguration;
import cn.dyw.engine.core.exec.IExecEngine;
import cn.dyw.engine.core.exec.plugin.PageSqlExecPlugin;
import cn.dyw.engine.core.exec.plugin.SingleLineResultExecPlugin;
import cn.dyw.engine.core.exec.plugin.SortSqlExecPlugin;
import cn.dyw.engine.core.exec.plugin.SqlExecPlugin;
import cn.dyw.engine.core.sql.template.ITemplateEngine;
import cn.dyw.engine.core.sql.template.MybatisTemplateEngine;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;
import java.util.List;

/**
 *
 * @author dyw770
 * @since 2026-03-27
 */
@ComponentScan("cn.dyw.engine.server")
@MapperScan(basePackages = "cn.dyw.engine.server.db.mapper")
@ConditionalOnClass(DataSource.class)
public class EngineBeanAutoConfiguration {

    @Bean
    public IExecEngine sqlExecEngin(DataSource dataSource, ITemplateEngine templateEngine) {
        return new DefaultExecEngine(dataSource, templateEngine);
    }

    @Bean
    public ITemplateEngine templateEngine() {
        return new MybatisTemplateEngine();
    }

    @Bean
    public EngineConfiguration engineConfiguration(List<SqlExecPlugin> plugins) {
        EngineConfiguration configuration = new EngineConfiguration();
        configuration.addPlugins(new PageSqlExecPlugin());
        configuration.addPlugins(new SortSqlExecPlugin());
        configuration.addPlugins(new SingleLineResultExecPlugin());
        for (SqlExecPlugin plugin : plugins) {
            configuration.addPlugins(plugin);
        }
        return configuration;
    }
}
