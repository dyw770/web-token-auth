package cn.dyw.auth.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dyw770
 * @since 2025-06-15
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    
    @GetMapping("")
    public String test() {
        return "aaa";
    }
}
