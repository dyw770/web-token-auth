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
public class LoggerGroupLevel {
    
    /**
     * Logger name
     */
    private String name;
    
    /**
     * Logger configured level
     */
    private LogLevel configuredLevel;

    /**
     * Logger group members
     */
    private List<String> members;
}
