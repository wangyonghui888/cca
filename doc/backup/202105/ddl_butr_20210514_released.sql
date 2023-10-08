
CREATE TABLE `tybss_new`.`t_merchant_agent`
(
    `id`                   bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `merchant_code`        VARCHAR(45)  NOT NULL,
    `agent_id`             VARCHAR(45)  NOT NULL,
    `agent_name`            VARCHAR(45)  NOT NULL,
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
    COMMENT = '信用商户代理表';