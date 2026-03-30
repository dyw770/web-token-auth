package cn.dyw.engine.core.exec;


import cn.dyw.engine.core.context.DeleteExecContext;
import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.SelectExecContext;
import cn.dyw.engine.core.context.UpdateExecContext;
import cn.dyw.engine.core.exec.plugin.PageSqlExecPlugin;
import cn.dyw.engine.core.exec.plugin.SingleLineResultExecPlugin;
import cn.dyw.engine.core.exec.plugin.SortSqlExecPlugin;
import cn.dyw.engine.core.model.DataPageOption;
import cn.dyw.engine.core.model.DataSortField;
import cn.dyw.engine.core.model.PageType;
import cn.dyw.engine.core.sql.template.ITemplateEngine;
import cn.dyw.engine.core.sql.template.MybatisTemplateEngine;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Before;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class ExecEngineTest {

    public DataSource dataSource;


    public DefaultExecEngine engine;

    public ITemplateEngine templateEngine;

    public EngineConfiguration configuration;

    @Before
    public void init() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.configFromPropety(initDatasourceProperties());
        dataSource.init();
        this.dataSource = dataSource;

        this.templateEngine = new MybatisTemplateEngine();

        this.engine = new DefaultExecEngine(this.dataSource, this.templateEngine);

        configuration = new EngineConfiguration();
        configuration.addPlugins(new PageSqlExecPlugin());
        configuration.addPlugins(new SortSqlExecPlugin());
        configuration.addPlugins(new SingleLineResultExecPlugin());
    }


    public Properties initDatasourceProperties() {
        Properties properties = new Properties();
        properties.setProperty("druid.url", "jdbc:mysql://192.168.19.110:3308/sql-exec-engine");
        properties.setProperty("druid.username", "root");
        properties.setProperty("druid.password", "litao929659095");
        properties.setProperty("druid.driverClassName", "com.mysql.cj.jdbc.Driver");
        return properties;
    }

    public SelectExecContext initSelectContext() {

        String sql = "select id, user_name, user_head, role_id from test_user where id = #{id} and id = #{id} limit #{size} offset #{offset}";

        SelectExecContext context = new SelectExecContext(sql, new HashMap<>());
        context.addParameter(new DynamicFilterParameter("id", 1));
        context.setDbType("mysql");

        context.setConfiguration(configuration);
        DataPageOption dataPageOption = new DataPageOption();
        dataPageOption.setPage(0);
        dataPageOption.setSize(10);
        dataPageOption.setPageType(PageType.fullCustom);
        dataPageOption.setNeedPage(true);
        context.setDataPageOption(dataPageOption);
        context.setCustomCountSql("select count(1) from test_user where id = #{id} and id = #{id}");

        context.addSort(new DataSortField("id", true));
        context.addSort(new DataSortField("user_name", false));

        return context;
    }

    public UpdateExecContext initUpdateContext() {

        String sql = "update test_user set user_name = #{name} where id = #{id}";

        UpdateExecContext context = new UpdateExecContext(sql);
        context.addParameter(new DynamicFilterParameter("id", 1));
        context.addParameter(new DynamicFilterParameter("name", "测试"));
        context.setDbType("mysql");
        context.setConfiguration(configuration);

        return context;
    }

    public DeleteExecContext initDeleteContext() {

        String sql = "delete from test_user where id = #{id}";

        DeleteExecContext context = new DeleteExecContext(sql);
        context.addParameter(new DynamicFilterParameter("id", 10));
        context.setDbType("mysql");
        context.setConfiguration(configuration);

        return context;
    }

}