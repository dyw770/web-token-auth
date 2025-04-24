package cn.dyw.auth.db.security;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * jdbc 请求匹配器
 * 
 * @author dyw770
 * @since 2025-04-24
 */
public class RequestMatcherRegistry extends AbstractRequestMatcherRegistry<List<RequestMatcher>> {

    
    private final ApplicationContext context;

    public RequestMatcherRegistry(ApplicationContext context) {
        this.context = context;
        super.setApplicationContext(context);
    }

    @Override
    protected List<RequestMatcher> chainRequestMatchers(List<RequestMatcher> requestMatchers) {
        return requestMatchers;
    }
    
}
