
CREATE TABLE `tybss_new`.`t_merchant_group`
(
    `id`                   bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `merchant_codes`        VARCHAR(2000)  NOT NULL COMMENT '商户组CODE',
    `merchant_names`        VARCHAR(2000)  NOT NULL COMMENT '商户名称',
     `group_name`        VARCHAR(2000)  NOT NULL COMMENT '商户组名称',
    `status`             tinyint(4)  NOT NULL default 2 COMMENT '是否开启 1为开启 2为关闭',
    `time_type`           tinyint(4)  NOT NULL default 1 COMMENT '时间类型  1为分钟 2为小时 3为日  4为月',
    `times` tinyint(4)  NOT NULL  COMMENT '时间值',
    `update_time` bigint(20)    COMMENT '上次更新时间',
    PRIMARY KEY (`id`)
)
    COMMENT = '商户分组表';