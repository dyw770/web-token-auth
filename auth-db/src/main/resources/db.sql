DROP TABLE IF EXISTS `sys_api_access_log`;
CREATE TABLE `sys_api_access_log`
(
    `id`                       int          NOT NULL AUTO_INCREMENT,
    `username`                 varchar(16)  NOT NULL COMMENT '用户名',
    `api_path`                 varchar(255) NOT NULL COMMENT 'api路径',
    `api_method`               varchar(16)  NOT NULL COMMENT 'api方法',
    `api_access_time`          timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'api访问时间',
    `api_access_duration`      int          NOT NULL COMMENT 'api访问时长',
    `api_access_ip`            varchar(16)  NOT NULL COMMENT 'api访问ip',
    `api_access_ua`            varchar(255) NOT NULL COMMENT 'api访问ua',
    `api_access_result_type`   varchar(64)  NOT NULL COMMENT 'api访问结果类型',
    `api_access_result_code`   int          NOT NULL COMMENT 'api访问结果代码',
    `api_access_response_code` int          NOT NULL COMMENT 'api访问响应代码',
    PRIMARY KEY (`id`)
) COMMENT ='api访问日志表';


DROP TABLE IF EXISTS `sys_api_resource`;
CREATE TABLE `sys_api_resource`
(
    `id`          int                  NOT NULL AUTO_INCREMENT,
    `api_path`    varchar(255)         NOT NULL COMMENT 'api路径',
    `match_type`  enum ('ANT','REGEX') NOT NULL COMMENT '匹配类型',
    `api_method`  varchar(16)          NOT NULL COMMENT 'api方法',
    `description` varchar(255)         NOT NULL COMMENT '描述',
    `enable`      tinyint              NOT NULL DEFAULT '0' COMMENT '是否启用',
    `create_time` timestamp            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT ='api资源表';

INSERT INTO `sys_api_resource`
VALUES (1, '/a/**', 'ANT', 'ALL', '测试', 1, '2025-04-24 13:41:42', '2025-05-14 14:32:27'),
       (2, '/b/**', 'ANT', 'ALL', '测试', 1, '2025-04-24 13:41:42', '2025-05-14 14:35:05'),
       (3, '/admin/**', 'ANT', 'ALL', '管理员操作接口', 1, '2025-04-25 09:07:43', '2025-06-14 10:36:57'),
       (6, '/test/a/**', 'ANT', 'ALL', '测试阿萨达', 0, '2025-05-15 08:56:26', '2025-06-10 14:05:08'),
       (7, '/test/static', 'ANT', 'ALL', '测试', 1, '2025-05-17 10:15:08', '2025-06-14 11:07:03'),
       (8, '/test/info', 'ANT', 'ALL', '测试', 1, '2025-05-17 10:15:20', '2025-06-14 10:42:36');


DROP TABLE IF EXISTS `sys_api_resource_auth`;
CREATE TABLE `sys_api_resource_auth`
(
    `auth_id`         int                                      NOT NULL AUTO_INCREMENT,
    `api_resource_id` int                                      NOT NULL COMMENT 'api资源id',
    `auth_type`       enum ('ROLE','IP','STATIC','PERMISSION') NOT NULL COMMENT '授权类型',
    `auth_object`     varchar(128)                             NOT NULL COMMENT '授权对象',
    `auth_time`       timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',
    `expired_time`    timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
    PRIMARY KEY (`auth_id`),
    UNIQUE KEY `auth_unique` (`api_resource_id`, `auth_type`, `auth_object`)
) COMMENT ='api资源授权表';

INSERT INTO `sys_api_resource_auth`
VALUES (1, 1, 'ROLE', 'admin', '2025-04-24 13:42:16', '2025-04-24 13:42:16'),
       (3, 1, 'IP', '0.0.0.0/0', '2025-04-24 13:45:15', '2025-04-24 13:45:15'),
       (4, 2, 'IP', '192.168.0.0', '2025-04-24 13:42:34', '2025-04-24 13:42:34'),
       (5, 3, 'ROLE', 'admin', '2025-04-25 09:08:21', '2025-04-25 09:08:21'),
       (6, 6, 'IP', '192.168.0.0/16', '2025-05-17 07:30:33', '2025-05-17 07:30:33'),
       (7, 7, 'STATIC', 'deny', '2025-05-17 10:18:28', '2025-05-17 10:18:28'),
       (9, 8, 'ROLE', 'user', '2025-05-17 12:33:57', '2025-05-17 12:33:57'),
       (10, 6, 'PERMISSION', 'menu.test.delete', '2025-06-08 08:28:25', '2025-06-08 08:28:25'),
       (11, 8, 'PERMISSION', 'menu.test.delete', '2025-06-08 08:29:13', '2025-06-08 08:29:13'),
       (12, 1, 'IP', '192.168.19.15', '2025-06-10 14:27:06', '2025-06-10 14:27:06');



DROP TABLE IF EXISTS `sys_menu_hierarchy`;
CREATE TABLE `sys_menu_hierarchy`
(
    `ancestor_menu_id`   int NOT NULL COMMENT '祖先菜单id',
    `descendant_menu_id` int NOT NULL COMMENT '后代菜单id',
    `depth`              int NOT NULL COMMENT '深度',
    PRIMARY KEY (`ancestor_menu_id`, `descendant_menu_id`)
) COMMENT ='菜单层级表';

INSERT INTO `sys_menu_hierarchy`
VALUES (8, 8, 0),
       (8, 9, 1),
       (8, 10, 1),
       (8, 11, 2),
       (8, 12, 2),
       (8, 13, 3),
       (8, 14, 3),
       (9, 9, 0),
       (10, 10, 0),
       (10, 11, 1),
       (10, 12, 1),
       (10, 13, 2),
       (10, 14, 2),
       (11, 11, 0),
       (12, 12, 0),
       (12, 13, 1),
       (12, 14, 1),
       (13, 13, 0),
       (14, 14, 0),
       (15, 15, 0),
       (15, 16, 1),
       (15, 18, 1),
       (15, 19, 2),
       (15, 22, 2),
       (15, 23, 1),
       (15, 24, 1),
       (15, 25, 2),
       (15, 27, 1),
       (15, 28, 2),
       (15, 29, 2),
       (15, 32, 1),
       (16, 16, 0),
       (18, 18, 0),
       (19, 19, 0),
       (22, 22, 0),
       (23, 19, 1),
       (23, 22, 1),
       (23, 23, 0),
       (24, 24, 0),
       (24, 25, 1),
       (25, 25, 0),
       (26, 26, 0),
       (27, 27, 0),
       (27, 28, 1),
       (27, 29, 1),
       (28, 28, 0),
       (29, 29, 0),
       (30, 8, 1),
       (30, 9, 2),
       (30, 10, 2),
       (30, 11, 3),
       (30, 12, 3),
       (30, 13, 4),
       (30, 14, 4),
       (30, 26, 1),
       (30, 30, 0),
       (30, 31, 1),
       (31, 31, 0),
       (32, 32, 0);


DROP TABLE IF EXISTS `sys_menu_permission`;
CREATE TABLE `sys_menu_permission`
(
    `menu_id`       int         NOT NULL COMMENT '菜单',
    `permission_id` varchar(32) NOT NULL COMMENT '权限ID',
    `create_time`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`menu_id`, `permission_id`)
) COMMENT ='菜单权限池配置';

INSERT INTO `sys_menu_permission`
VALUES (31, 'menu.test.delete', '2025-05-29 11:34:09'),
       (31, 'menu.test.edit', '2025-05-29 11:33:27'),
       (31, 'menu.test.read', '2025-05-29 11:33:43');


DROP TABLE IF EXISTS `sys_menus`;
CREATE TABLE `sys_menus`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `menu_name`   varchar(255) NOT NULL COMMENT '菜单名',
    `menu_router` varchar(255)          DEFAULT NULL COMMENT '菜单路由',
    `menu_icon`   varchar(255) NOT NULL COMMENT '菜单图标',
    `menu_order`  int          NOT NULL COMMENT '菜单顺序',
    `nav_show`    tinyint      NOT NULL DEFAULT '1' COMMENT '是否显示',
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT ='菜单表';

INSERT INTO `sys_menus`
VALUES (8, '多级导航', '/multilevel_menu_example', 'heroicons-solid:menu-alt-3', 1, 1, '2025-05-02 03:53:24',
        '2025-05-20 01:55:34'),
       (9, '导航1', '/multilevel_menu_example/page', 'heroicons-solid:menu-alt-3', 1, 1, '2025-05-02 03:54:53',
        '2025-05-02 03:54:53'),
       (10, '导航2', '/multilevel_menu_example/level2', 'heroicons-solid:menu-alt-3', 2, 1, '2025-05-02 03:55:13',
        '2025-05-09 05:35:19'),
       (11, '导航2-1', '/multilevel_menu_example/level2/page', 'heroicons-solid:menu-alt-3', 1, 1,
        '2025-05-02 03:55:40', '2025-05-02 03:55:40'),
       (12, '导航2-2', '/multilevel_menu_example/level2/level3', 'heroicons-solid:menu-alt-3', 2, 1,
        '2025-05-02 03:55:56', '2025-05-09 05:35:19'),
       (13, '导航2-2-1', '/multilevel_menu_example/level2/level3/page1', 'ep:credit-card', 1, 1, '2025-05-02 03:56:24',
        '2025-05-02 05:04:34'),
       (14, '导航2-2-2', '/multilevel_menu_example/level2/level3/page2', 'heroicons-solid:menu-alt-3', 2, 1,
        '2025-05-02 03:56:40', '2025-05-02 03:56:40'),
       (15, '系统', '', 'ant-design:setting-outlined', 1, 1, '2025-05-02 04:45:39', '2025-05-20 01:55:03'),
       (16, '用户管理', '/admin/user', 'ant-design:user-outline', 4, 1, '2025-05-03 04:55:37', '2025-05-12 14:45:29'),
       (18, '角色管理', '/admin/role', 'ant-design:team-outlined', 3, 1, '2025-05-10 06:56:10', '2025-05-10 06:56:10'),
       (19, '菜单编辑', '/admin/menu/edit', 'ri:menu-add-line', 1, 1, '2025-05-12 08:41:37', '2025-05-20 02:28:38'),
       (22, '菜单授权', '/admin/menu/auth', 'ri:menu-unfold-line', 2, 1, '2025-05-13 08:48:25', '2025-05-20 02:28:53'),
       (23, '菜单管理', '/admin/menu', 'ant-design:menu-outlined', 6, 1, '2025-05-13 08:55:32', '2025-05-20 02:47:13'),
       (24, 'API资源管理', '/admin/resource', 'ri:database-2-line', 5, 1, '2025-05-14 12:57:00', '2025-05-20 02:46:06'),
       (25, '系统资源授权', '/admin/resource/auth/:id(\\d+)', 'ant-design:api-outlined', 8, 0, '2025-05-16 02:55:24',
        '2025-05-16 03:21:43'),
       (26, '资源授权测试', '/simple/test', 'ri:test-tube-line', 7, 1, '2025-05-17 10:07:08', '2025-05-20 02:28:09'),
       (27, '系统日志', '/admin/logs', 'ri:file-list-line', 8, 1, '2025-05-18 15:07:17', '2025-05-20 02:46:30'),
       (28, '访问日志', '/admin/logs/access', 'ri:file-list-line', 1, 1, '2025-05-18 15:07:40', '2025-05-20 02:46:32'),
       (29, '事件日志', '/admin/logs/event', 'ri:file-list-line', 2, 1, '2025-05-18 15:07:56', '2025-05-20 02:46:35'),
       (30, '演示', '', 'i-uim:box', 2, 1, '2025-05-20 01:55:19', '2025-05-20 01:55:19'),
       (31, '按钮权限测试', '/simple/permission', 'ri:test-tube-line', 4, 1, '2025-05-21 13:22:42',
        '2025-05-21 13:22:42'),
       (32, '权限管理', '/admin/permission', 'ant-design:team-outlined', 6, 1, '2025-05-27 12:00:21',
        '2025-05-27 12:00:21');


DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `permission_id`   varchar(32) NOT NULL COMMENT '权限ID',
    `permission_desc` varchar(128)         DEFAULT NULL COMMENT '权限说明',
    `permission_type` varchar(12) NOT NULL COMMENT '权限分类',
    `create_time`     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`permission_id`)
) COMMENT ='权限表';

INSERT INTO `sys_permission`
VALUES ('menu:edit', '菜单编辑', 'GLOBAL', '2025-05-27 13:49:23'),
       ('menu.test.delete', '删除权限', 'MENU', '2025-05-29 11:34:09'),
       ('menu.test.edit', '编辑权限', 'MENU', '2025-05-29 11:33:27'),
       ('menu.test.read', '读取权限', 'MENU', '2025-05-29 11:33:43');



DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `role_code`   varchar(16)  NOT NULL COMMENT '角色名',
    `role_name`   varchar(64)  NOT NULL COMMENT '角色名',
    `description` varchar(255) NOT NULL COMMENT '角色描述',
    `del`         tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`role_code`)
) COMMENT ='角色表';

INSERT INTO `sys_role`
VALUES ('admin', '管理员', '管理员', 0, '2025-04-18 04:02:37', '2025-05-05 10:19:40'),
       ('department_admin', '部门管理员', '部门管理员', 0, '2025-04-18 04:02:37', '2025-05-05 10:19:40'),
       ('test1', 'test1', '', 1, '2025-05-12 05:48:32', '2025-05-12 05:48:54'),
       ('test2', 'test2', '', 0, '2025-05-12 05:48:37', '2025-05-12 06:40:01'),
       ('test3', 'test3', '', 0, '2025-05-12 05:48:43', '2025-05-12 06:46:36'),
       ('test4', 'test4', '', 0, '2025-05-12 05:48:49', '2025-05-12 06:39:58'),
       ('test5', 'test5', '', 0, '2025-05-12 06:08:44', '2025-05-12 06:45:48'),
       ('user', '用户', '用户', 0, '2025-04-18 04:02:37', '2025-05-05 10:19:40');


DROP TABLE IF EXISTS `sys_role_hierarchy`;
CREATE TABLE `sys_role_hierarchy`
(
    `ancestor_role_code`   varchar(16) NOT NULL COMMENT '祖先角色名',
    `descendant_role_code` varchar(16) NOT NULL COMMENT '后代角色名',
    `depth`                int         NOT NULL COMMENT '深度',
    PRIMARY KEY (`ancestor_role_code`, `descendant_role_code`)
) COMMENT ='角色层级表';

INSERT INTO `sys_role_hierarchy`
VALUES ('admin', 'admin', 0),
       ('admin', 'department_admin', 1),
       ('admin', 'test2', 2),
       ('admin', 'test3', 2),
       ('admin', 'test4', 1),
       ('admin', 'test5', 3),
       ('admin', 'user', 1),
       ('department_admin', 'department_admin', 0),
       ('test2', 'test2', 0),
       ('test2', 'test5', 1),
       ('test3', 'test3', 0),
       ('test4', 'test2', 1),
       ('test4', 'test3', 1),
       ('test4', 'test4', 0),
       ('test4', 'test5', 2),
       ('test5', 'test5', 0),
       ('user', 'user', 0);


DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `role_code`    varchar(16) NOT NULL COMMENT '角色名',
    `menu_id`      int         NOT NULL COMMENT '菜单id',
    `auth_time`    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',
    `expired_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
    PRIMARY KEY (`role_code`, `menu_id`)
) COMMENT ='菜单授权表';

INSERT INTO `sys_role_menu`
VALUES ('admin', 8, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 9, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 10, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 11, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 12, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 13, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 14, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 15, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 16, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 18, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 19, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 22, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 23, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 24, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 25, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 26, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 27, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 28, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 29, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('admin', 30, '2025-05-20 01:56:18', '2025-05-20 01:56:18'),
       ('admin', 31, '2025-05-21 13:23:41', '2025-05-21 13:23:41'),
       ('admin', 32, '2025-05-27 12:00:32', '2025-05-27 12:00:32'),
       ('test2', 30, '2025-05-30 08:51:47', '2025-05-30 08:51:47'),
       ('test2', 31, '2025-05-30 08:51:47', '2025-05-30 08:51:47'),
       ('test5', 30, '2025-05-30 08:51:47', '2025-05-30 08:51:47'),
       ('test5', 31, '2025-05-30 08:51:47', '2025-05-30 08:51:47');

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `permission_id` varchar(32) NOT NULL COMMENT '权限id',
    `role_code`     varchar(16) NOT NULL COMMENT '角色id',
    `auth_time`     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',
    PRIMARY KEY (`permission_id`, `role_code`)
) COMMENT ='角色权限授权表';

INSERT INTO `sys_role_permission`
VALUES ('menu.test.delete', 'admin', '2025-06-08 08:39:36'),
       ('menu.test.read', 'admin', '2025-05-29 11:50:29');


DROP TABLE IF EXISTS `sys_system_operation_log`;
CREATE TABLE `sys_system_operation_log`
(
    `id`                    int          NOT NULL AUTO_INCREMENT,
    `username`              varchar(16)  NOT NULL COMMENT '用户名',
    `operation_time`        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `operation_ip`          varchar(16)  NOT NULL COMMENT '操作ip',
    `operation_ua`          varchar(255) NOT NULL COMMENT '操作ua',
    `operation_result_type` varchar(64)  NOT NULL COMMENT '操作结果类型',
    `operation_result_code` int          NOT NULL COMMENT '操作结果代码',
    `operation_content`     varchar(255) NOT NULL COMMENT '操作内容',
    `operation_event`       varchar(255) NOT NULL COMMENT '操作事件',
    PRIMARY KEY (`id`)
) COMMENT ='系统操作日志表';


DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `username`                varchar(16)  NOT NULL COMMENT '用户名',
    `nickname`                varchar(255) NOT NULL COMMENT '昵称',
    `password`                varchar(255) NOT NULL COMMENT '密码',
    `avatar`                  varchar(128)          DEFAULT NULL COMMENT '头像',
    `enabled`                 tinyint(1)   NOT NULL DEFAULT '1' COMMENT '是否启用',
    `account_non_expired`     tinyint(1)   NOT NULL DEFAULT '1' COMMENT '账号是否过期',
    `credentials_non_expired` tinyint(1)   NOT NULL DEFAULT '1' COMMENT '密码是否过期',
    `account_non_locked`      tinyint(1)   NOT NULL DEFAULT '1' COMMENT '账号是否锁定',
    `create_time`             timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`             timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`username`)
) COMMENT ='用户表';


INSERT INTO `sys_user`
VALUES ('admin', '管理员', '{bcrypt}$2a$10$BUgHj9Jvt1MqojztvPU9penFz74D2prpaXFE9Qy24s1j4AHdXbcBa', NULL, 1, 1, 1, 1,
        '2025-05-03 10:02:44', '2025-05-10 11:48:19'),
       ('test', '测试', '{bcrypt}$2a$10$s6QFyTcuvRhiXlsUWWqc0.8xCSe7CFSznMA5/cQWAxwgGs0Xwk41K',
        'https://content-management-files.canva.cn/f67e0c02-7936-4054-adc8-744ad9becf37/1-anime2x.png', 1, 1, 1, 1,
        '2025-04-18 06:17:04', '2025-05-18 07:34:15');


DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `username`     varchar(16) NOT NULL COMMENT '用户名',
    `role_code`    varchar(16) NOT NULL COMMENT '角色名',
    `auth_time`    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',
    `expired_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
    PRIMARY KEY (`username`, `role_code`)
) COMMENT ='用户角色授权表';

INSERT INTO `sys_user_role`
VALUES ('admin', 'admin', '2025-06-03 03:44:38', '2025-06-03 03:44:38'),
       ('test', 'admin', '2025-04-18 06:17:05', '2025-04-19 06:17:05');
