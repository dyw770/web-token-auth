package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.SpringSecurityAuthApplicationTests;
import cn.dyw.auth.db.model.ApiResourceDto;
import cn.dyw.auth.db.service.ISysApiResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-23
 */
@SpringBootTest(classes = SpringSecurityAuthApplicationTests.class)
public class SysApiResourceServiceImplTest {

    @Autowired
    private ISysApiResourceService apiResourceService;
    
    @Test
    public void listAll() {
        List<ApiResourceDto> resourceDto = apiResourceService.listAll();
        System.out.println(resourceDto);
    }
}