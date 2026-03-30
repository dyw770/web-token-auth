package cn.dyw.engine.core.exec.plugin;


import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.context.SelectExecContext;
import cn.dyw.engine.core.exec.ExecEngineTest;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.core.exec.result.SingleLineExecResult;
import cn.dyw.engine.core.model.DataPageOption;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class SingleLineResultExecPluginTest extends ExecEngineTest {

    public SelectExecContext initSingleLineContext(Integer id) {

        String sql = "select id, user_name, user_head, role_id from test_user where id = #{id}";

        SelectExecContext context = new SelectExecContext(sql, new HashMap<>());
        context.addParameter(new DynamicFilterParameter("id", id));
        context.setDbType("mysql");
        context.addExtendParameter(SingleLineResultExecPlugin.SINGLE_LINE_RESULT, Boolean.TRUE);
        context.setConfiguration(configuration);
        DataPageOption dataPageOption = new DataPageOption();
        dataPageOption.setNeedPage(false);
        context.setDataPageOption(dataPageOption);

        return context;
    }

    @Test
    public void doExec() {
        ExecResult exec = engine.exec(initSingleLineContext(1));
        Assert.assertTrue(exec instanceof SingleLineExecResult);
        System.out.println(exec);
    }
}