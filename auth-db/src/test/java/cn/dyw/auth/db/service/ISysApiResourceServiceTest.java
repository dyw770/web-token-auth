package cn.dyw.auth.db.service;

import cn.dyw.auth.db.AuthDbTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author dyw770
 * @since 2025-07-21
 */
@SpringBootTest(classes = AuthDbTests.class)
public class ISysApiResourceServiceTest {

    @Autowired
    private ISysApiResourceService apiResourceService;

    @Test
    public void testListAll() {
        apiResourceService.listAll();
    }

}