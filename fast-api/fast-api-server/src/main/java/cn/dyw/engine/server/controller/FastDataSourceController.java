package cn.dyw.engine.server.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SQL模板管理控制器
 *
 * @author dyw770
 * @since 2026-03-26
 */
@Validated
@RestController
@RequestMapping("${app.fast-api.api-context-path:/data/source}")
public class FastDataSourceController {
    
}