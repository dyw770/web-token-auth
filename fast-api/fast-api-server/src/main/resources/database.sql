CREATE TABLE `sys_fast_api`
(
    `id`           int comment 'id'                                       NOT NULL AUTO_INCREMENT,
    `api_name`     varchar(32) comment '名称'                             NOT NULL,
    `api_describe` varchar(128) comment '描述'                            NOT NULL,
    `api_path`     varchar(128) comment '路径'                            NOT NULL,
    `sys_sql`      int comment 'sql'                                      NOT NULL,
    `create_time`  timestamp default current_timestamp comment '创建时间' NOT NULL,
    `update_time`  timestamp default current_timestamp comment '更新时间' NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `sys_fast_sql`
(
    `id`               int comment 'id'                                               NOT NULL AUTO_INCREMENT,
    `sql_name`         varchar(32) comment '名称'                                     NOT NULL,
    `sql_describe`     varchar(128) comment '描述'                                    NOT NULL,
    `sql_template`     text comment 'sql模板'                                         NOT NULL,
    `custom_count_sql` text comment '自定义计数sql',
    `create_time`      timestamp comment '创建时间'                                   NOT NULL,
    `update_time`      timestamp comment '更新时间'                                   NOT NULL,
    `statement_type`   enum ('select','update','delete', 'insert') comment '语句类型' NOT NULL,
    `sort_fields`      text comment '排序字段',
    `parameters`       text comment '参数',
    `data_page`        text comment '分页参数',
    `extend`           text comment '扩展字段',
    `data_field_binds` text comment '数据字段绑定',
    PRIMARY KEY (`id`)
);

