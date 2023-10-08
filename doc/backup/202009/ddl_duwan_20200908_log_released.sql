drop table if exists merchant.`merchant_log`;
CREATE TABLE merchant.`merchant_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`       bigint(20)                              DEFAULT '0' COMMENT '用户id',
    `user_name`     varchar(64) COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '用户名',
    `operat_type`   tinyint(255) NOT NULL COMMENT '操作类型 参考代码枚举',
    `type_name`     varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '类型名称',
    `page_name`     varchar(256) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '页面名称',
    `page_code`     varchar(64) COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '页面编码 参考josn配置',
    `merchant_code` varchar(128) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户编码',
    `merchant_name` varchar(256) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户名称',
    `data_id`       varchar(128) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '数据id',
    `operat_field`  json                                    DEFAULT NULL COMMENT '操作字段',
    `before_values` json                                    DEFAULT NULL COMMENT '操作之前的值',
    `after_values`  json                                    DEFAULT NULL COMMENT '操作之后的值',
    `log_tag`       tinyint(255)                            DEFAULT NULL COMMENT '日志标识 0 商户端 1后端',
    `operat_time`   bigint(20)                              DEFAULT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC COMMENT ='商户日志';
ALTER TABLE `merchant`.`merchant_log`
    ADD INDEX `idx_username` (`user_name`) USING BTREE,
    ADD INDEX `idx_page_type` (`operat_type`, `page_code`) USING BTREE,
    ADD INDEX `idx_merchan_code` (`merchant_code`) USING BTREE;