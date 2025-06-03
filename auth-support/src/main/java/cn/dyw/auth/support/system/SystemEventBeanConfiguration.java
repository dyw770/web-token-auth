package cn.dyw.auth.support.system;

import cn.dyw.auth.support.SystemEventHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dyw770
 * @since 2025-04-27
 */
@Configuration
public class SystemEventBeanConfiguration {

    @Bean
    public SystemEventInterceptor systemEventAdvisor(BeanFactory beanFactory, @Autowired(required = false) SystemEventHandler systemEventHandler) {
        SystemEventInterceptor interceptor = new SystemEventInterceptor();
        if (ObjectUtils.isNotEmpty(systemEventHandler)) {
            interceptor.setSystemEventHandler(systemEventHandler);
        }

        interceptor.setBeanFactory(beanFactory);
        return interceptor;
    }

    @Bean
    public SystemEventAspect systemEventAspect(SystemEventInterceptor interceptor) {
        return new SystemEventAspect(interceptor);
    }
}
