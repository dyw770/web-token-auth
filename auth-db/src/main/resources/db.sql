drop table if exists sys_user;
create table if not exists sys_user
(
    `username`                varchar(16)  not null primary key comment '用户名',
    `nickname`                varchar(255) not null comment '昵称',
    `password`                varchar(255) not null comment '密码',
    `enabled`                 boolean      not null default true comment '是否启用',
    `account_non_expired`     boolean      not null default true comment '账号是否过期',
    `credentials_non_expired` boolean      not null default true comment '密码是否过期',
    `account_non_locked`      boolean      not null default true comment '账号是否锁定',
    `create_time`             timestamp    not null default current_timestamp comment '创建时间',
    `update_time`             timestamp    not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '用户表';

drop table if exists sys_role;
create table if not exists sys_role
(
    `role_code`   varchar(16)  not null primary key comment '角色名',
    `description` varchar(255) not null comment '角色描述',
    `del`         boolean      not null default false comment '是否删除',
    `create_time` timestamp    not null default current_timestamp comment '创建时间',
    `update_time` timestamp    not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '角色表';

drop table if exists sys_role_hierarchy;
create table if not exists sys_role_hierarchy
(
    `ancestor_role_code`   varchar(16) not null comment '祖先角色名',
    `descendant_role_code` varchar(16) not null comment '后代角色名',
    `depth`                int         not null comment '深度',
    primary key (`ancestor_role_code`, `descendant_role_code`)
) comment '角色层级表';

drop table if exists sys_user_role;
create table if not exists sys_user_role
(
    `username`     varchar(16) not null comment '用户名',
    `role_code`    varchar(16) not null comment '角色名',
    `auth_time`    timestamp   not null default current_timestamp comment '授权时间',
    `expired_time` timestamp   not null default current_timestamp comment '过期时间',
    primary key (`username`, `role_code`)
) comment '用户角色授权表';

drop table if exists sys_api_resource;
create table if not exists sys_api_resource
(
    `id`          int auto_increment primary key,
    `api_path`    varchar(255)          not null comment 'api路径',
    `match_type`  enum ('ANT', 'REGEX') not null comment '匹配类型',
    `api_method`  varchar(16)           not null comment 'api方法',
    `description` varchar(255)          not null comment '描述',
    `create_time` timestamp             not null default current_timestamp comment '创建时间',
    `update_time` timestamp             not null default current_timestamp on update current_timestamp comment '更新时间'
) comment 'api资源表';

drop table if exists sys_api_resource_auth;
create table if not exists sys_api_resource_auth
(
    `api_resource_id` int                           not null comment 'api资源id',
    `auth_type`       enum ('ROLE', 'IP', 'STATIC') not null comment '授权类型',
    `auth_object`     varchar(128)                  not null comment '授权对象',
    `auth_time`       timestamp                     not null default current_timestamp comment '授权时间',
    `expired_time`    timestamp                     not null default current_timestamp comment '过期时间',
    primary key (`api_resource_id`, `auth_type`, `auth_object`)
) comment 'api资源授权表';

drop table if exists sys_menus;
create table if not exists sys_menus
(
    `id`          int auto_increment primary key,
    `menu_name`   varchar(255) not null comment '菜单名',
    `menu_router` varchar(255) not null comment '菜单路由',
    `menu_icon`   varchar(255) not null comment '菜单图标',
    `menu_order`  int          not null comment '菜单顺序',
    `create_time` timestamp    not null default current_timestamp comment '创建时间',
    `update_time` timestamp    not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '菜单表';

drop table if exists sys_menu_hierarchy;
create table if not exists sys_menu_hierarchy
(
    `ancestor_menu_id`   int not null comment '祖先菜单id',
    `descendant_menu_id` int not null comment '后代菜单id',
    `depth`              int not null comment '深度',
    primary key (`ancestor_menu_id`, `descendant_menu_id`)
) comment '菜单层级表';

drop table if exists sys_role_menu;
create table if not exists sys_role_menu
(
    `role_code`    varchar(16) not null comment '角色名',
    `menu_id`      int         not null comment '菜单id',
    `auth_time`    timestamp   not null default current_timestamp comment '授权时间',
    `expired_time` timestamp   not null default current_timestamp comment '过期时间',
    primary key (`role_code`, `menu_id`)
) comment '菜单授权表';

drop table if exists sys_api_access_log;
create table if not exists sys_api_access_log
(
    `id`                       int auto_increment primary key,
    `username`                 varchar(16)  not null comment '用户名',
    `api_path`                 varchar(255) not null comment 'api路径',
    `api_method`               varchar(16)  not null comment 'api方法',
    `api_access_time`          timestamp    not null default current_timestamp comment 'api访问时间',
    `api_access_duration`      int          not null comment 'api访问时长',
    `api_access_ip`            varchar(16)  not null comment 'api访问ip',
    `api_access_ua`            varchar(255) not null comment 'api访问ua',
    `api_access_result_type`   varchar(64)  not null comment 'api访问结果类型',
    `api_access_result_code`   int          not null comment 'api访问结果代码',
    `api_access_response_code` int          not null comment 'api访问响应代码'
) comment 'api访问日志表';

drop table if exists sys_system_operation_log;
create table if not exists sys_system_operation_log
(
    `id`                    int auto_increment primary key,
    `username`              varchar(16)  not null comment '用户名',
    `operation_time`        timestamp    not null default current_timestamp comment '操作时间',
    `operation_ip`          varchar(16)  not null comment '操作ip',
    `operation_ua`          varchar(255) not null comment '操作ua',
    `operation_result_type` varchar(64)  not null comment '操作结果类型',
    `operation_result_code` int          not null comment '操作结果代码',
    `operation_content`     varchar(255) not null comment '操作内容',
    `operation_event`       varchar(255) not null comment '操作事件'
) comment '系统操作日志表';
