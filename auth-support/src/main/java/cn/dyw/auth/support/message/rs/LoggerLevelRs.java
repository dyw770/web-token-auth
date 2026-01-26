package cn.dyw.auth.support.message.rs;

import lombok.Data;
import org.springframework.boot.logging.LogLevel;

import java.util.List;

/**
 *
 * @author dyw770
 * @since 2026-01-26
 */
@Data
public class LoggerLevelRs {

    /**
     * Logger
     */
    private List<LoggerLevel> loggers;

    /**
     * Logger group
     */
    private List<LoggerGroupLevel> groups;


    /**
     * Logger level
     */
    private List<LogLevel> levels;
}
