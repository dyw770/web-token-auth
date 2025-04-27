package cn.dyw.auth.support.system;

import cn.dyw.auth.annotation.SystemEvent;
import cn.dyw.auth.support.SystemEventHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author dyw770
 * @since 2025-04-27
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SystemEventBeanConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor systemEventAdvisor(BeanFactory beanFactory, @Autowired(required = false) SystemEventHandler systemEventHandler) {
        SystemEventInterceptor interceptor = new SystemEventInterceptor();
        if (ObjectUtils.isNotEmpty(systemEventHandler)) {
            interceptor.setSystemEventHandler(systemEventHandler);
        }

        interceptor.setBeanFactory(beanFactory);
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, SystemEvent.class, true);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);

        return advisor;
    }

}
