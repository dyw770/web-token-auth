package cn.dyw.auth.support.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.auth.support.message.rs.LoggerGroupLevel;
import cn.dyw.auth.support.message.rs.LoggerLevel;
import cn.dyw.auth.support.message.rs.LoggerLevelRs;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.logging.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dyw770
 * @since 2026-01-22
 */
@Validated
@RestController
@RequestMapping("${app.auth.api-context-path:/admin}/logging")
public class LoggingController {

    /**
     * LoggingSystem
     */
    private final LoggingSystem loggingSystem;

    /**
     * LoggerGroups
     */
    private final LoggerGroups loggerGroups;

    /**
     * LogFile
     */
    private final ObjectProvider<LogFile> logFile;


    public LoggingController(LoggingSystem loggingSystem,
                             LoggerGroups loggerGroups,
                             ObjectProvider<LogFile> logFile) {
        this.loggingSystem = loggingSystem;
        this.loggerGroups = loggerGroups;
        this.logFile = logFile;
    }

    /**
     * 获取日志级别
     *
     * @return 日志级别
     */
    @GetMapping("level")
    public Result<LoggerLevelRs> level() {
        loggingSystem.getSupportedLogLevels();
        List<LoggerConfiguration> configurations = loggingSystem.getLoggerConfigurations();
        List<LoggerLevel> loggers = new ArrayList<>(configurations.size());
        LoggerLevelRs levelRs = new LoggerLevelRs();
        for (LoggerConfiguration configuration : configurations) {
            LoggerLevel level = new LoggerLevel();
            level.setName(configuration.getName());
            level.setConfiguredLevel(configuration.getConfiguredLevel());
            level.setEffectiveLevel(configuration.getEffectiveLevel());
            loggers.add(level);
        }
        levelRs.setLoggers(loggers);
        levelRs.setLevels(loggingSystem.getSupportedLogLevels().stream().toList());
        levelRs.setGroups(new ArrayList<>(16));
        loggerGroups.forEach(group -> {
                    LoggerGroupLevel groupLevel = new LoggerGroupLevel();
                    groupLevel.setName(group.getName());
                    groupLevel.setConfiguredLevel(group.getConfiguredLevel());
                    groupLevel.setMembers(group.getMembers());
                    levelRs.getGroups().add(groupLevel);
                }
        );
        return Result.createSuccess(levelRs);
    }

    @GetMapping("configure")
    public Result<Void> configureLogLevel(@NotBlank @RequestParam("name") String name,
                                          @RequestParam("level") LogLevel level) {
        LoggerGroup group = this.loggerGroups.get(name);
        if (group != null && group.hasMembers()) {
            group.configureLogLevel(level, this.loggingSystem::setLogLevel);
        } else {
            this.loggingSystem.setLogLevel(name, level);
        }
        return Result.createSuccess();
    }

    /**
     * 获取日志文件
     *
     * @return 日志文件
     */
    @GetMapping(path = "file", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<Resource> logFile() {
        if (ObjectUtils.isEmpty(this.logFile.getIfAvailable())) {
            return ResponseEntity.ok()
                    .body(new ByteArrayResource("No Log File".getBytes(StandardCharsets.UTF_8)));
        }
        FileSystemResource resource = new FileSystemResource(this.logFile.getIfAvailable().toString());
        return ResponseEntity.ok()
                .body(resource);
    }
}
