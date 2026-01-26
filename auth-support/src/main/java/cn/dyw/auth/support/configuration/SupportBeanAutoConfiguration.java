package cn.dyw.auth.support.configuration;

import cn.dyw.auth.support.controller.LoggingController;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggerGroups;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author dyw770
 * @since 2026-01-26
 */
@Configuration
public class SupportBeanAutoConfiguration {

    /**
     * 日志控制器
     *
     * @param loggingSystem        LoggingSystem
     * @param loggerGroupsProvider LoggerGroups
     * @param logFile              LogFile
     * @return 日志控制器
     */
    @Bean
    @ConditionalOnBean(LoggingSystem.class)
    public LoggingController loggingController(LoggingSystem loggingSystem,
                                               ObjectProvider<LoggerGroups> loggerGroupsProvider,
                                               ObjectProvider<LogFile> logFile) {
        return new LoggingController(loggingSystem, loggerGroupsProvider.getIfAvailable(LoggerGroups::new), logFile);
    }
}
