package cn.dyw.engine.core.exec;


import org.junit.Test;

public class DefaultExecEngineTest extends ExecEngineTest {

    @Test
    public void testSelectExecutor() throws Exception {
        ExecResult exec = engine.exec(initSelectContext());
        System.out.println(exec);
    }

    @Test
    public void testUpdateExecutor() throws Exception {
        ExecResult exec = engine.exec(initUpdateContext());
        System.out.println(exec);
    }

    @Test
    public void testDeleteExecutor() throws Exception {
        ExecResult exec = engine.exec(initDeleteContext());
        System.out.println(exec);
    }
}