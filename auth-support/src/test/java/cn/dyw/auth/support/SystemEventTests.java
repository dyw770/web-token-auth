package cn.dyw.auth.support;


import cn.dyw.auth.support.bean.SystemEventSimple;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author dyw770
 * @since 2025-04-27
 */
@EnableAspectJAutoProxy
@ComponentScan(basePackageClasses = SystemEventTests.class)
public class SystemEventTests {
    
    private ApplicationContext context;
    
    public void init() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        context = new AnnotationConfigApplicationContext(SystemEventTests.class);
    }
    
    @Test
    public void testLogSystemEvent() {
        init();
        SystemEventSimple bean = context.getBean(SystemEventSimple.class);
        bean.logEvent("hello", "dyw");
    }
}
