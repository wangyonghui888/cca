drop table if exists merchant.t_line_carrier;
create table merchant.t_line_carrier
(
    `id`                  bigint(10) not null AUTO_INCREMENT comment '主键ID',
    `line_carrier_code`   bigint(20)          default null comment '线路商编码',
    `line_carrier_name`   varchar(1000)       default null comment '线路商名称',
    `line_carrier_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '线路商开关(0-关闭,1-开启)',
    `create_by`           varchar(255)        DEFAULT NULL COMMENT '创建人',
    `create_time`         bigint(20)          default null comment '创建时间',
    `update_by`           varchar(255)        DEFAULT NULL COMMENT '更新人',
    `update_time`         bigint(20)          default null comment '更新时间',
    primary key (`id`)
) COMMENT ='线路商表';