package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.SpringSecurityAuthApplicationTests;
import cn.dyw.auth.db.domain.SysRole;
import cn.dyw.auth.db.model.RoleDto;
import cn.dyw.auth.db.service.ISysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-04-17
 */
@SpringBootTest(classes = SpringSecurityAuthApplicationTests.class)
public class SysRoleServiceImplTest {

    @Autowired
    private ISysRoleService roleService;

    @Test
    @Rollback(value = false)
    @Transactional
    public void savaRole() {

        SysRole adminRole = new SysRole();
        adminRole.setRoleCode("admin");
        adminRole.setDescription("管理员");
        roleService.savaRole(adminRole, "");

        SysRole userRole = new SysRole();
        userRole.setRoleCode("user");
        userRole.setDescription("用户");
        roleService.savaRole(userRole, "");

        SysRole departmentAdmin = new SysRole();
        departmentAdmin.setRoleCode("department_admin");
        departmentAdmin.setDescription("部门管理员");
        roleService.savaRole(departmentAdmin, "admin");

        SysRole admin1 = new SysRole();
        admin1.setRoleCode("admin1");
        admin1.setDescription("admin1");
        roleService.savaRole(admin1, "admin");

        SysRole admin2 = new SysRole();
        admin2.setRoleCode("admin2");
        admin2.setDescription("admin2");
        roleService.savaRole(admin2, "admin1");

        SysRole admin3 = new SysRole();
        admin3.setRoleCode("admin3");
        admin3.setDescription("admin3");
        roleService.savaRole(admin3, "admin1");

        SysRole admin4 = new SysRole();
        admin4.setRoleCode("admin4");
        admin4.setDescription("admin4");
        roleService.savaRole(admin4, "admin3");
    }

    @Test
    @Rollback(value = false)
    public void testDeleteRole() {
        roleService.deleteRole("admin4");
    }

    @Test
    @Rollback(value = false)
    public void testUpdateRoleHierarchy() {
        roleService.updateRoleHierarchy("admin4", "admin2");
    }

    @Test
    public void roleList() {
        List<RoleDto> roleDto = roleService.roleList();

        System.out.println(roleDto);
    }
}