package cn.dyw.auth.support.message.rs;

import lombok.Data;
import org.springframework.boot.logging.LogLevel;

/**
 *
 * @author dyw770
 * @since 2026-01-26
 */
@Data
public class LoggerLevel {
    
    /**
     * Logger name
     */
    private String name;
    
    /**
     * Logger configured level
     */
    private LogLevel configuredLevel;
    
    /**
     * Logger effective level
     */
    private LogLevel effectiveLevel;
}
