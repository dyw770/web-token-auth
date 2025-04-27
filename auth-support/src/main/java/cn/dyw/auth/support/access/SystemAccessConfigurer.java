package cn.dyw.auth.support.access;

import cn.dyw.auth.support.SystemAccessHandler;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.header.HeaderWriterFilter;

/**
 * 配置过滤器
 *
 * @author dyw770
 * @since 2025-04-27
 */
public class SystemAccessConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractHttpConfigurer<SystemAccessConfigurer<H>, H> {

    @Getter
    @Setter
    private SystemAccessHandler systemAccessHandler;

    public SystemAccessConfigurer() {
        this.systemAccessHandler = new DefaultSystemAccessHandler();
    }

    @Override
    public void configure(H http) {
        SystemAccessFilter systemAccessFilter = new SystemAccessFilter();
        if (ObjectUtils.isNotEmpty(systemAccessHandler)) {
            systemAccessFilter.setSystemAccessHandler(systemAccessHandler);
        }
        http.addFilterBefore(systemAccessFilter, HeaderWriterFilter.class);
    }
}
