-- merchant库 1383需求添加
CREATE TABLE `merchant`.`merchant_config` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `merchant_code` varchar(225) NOT NULL COMMENT '商户编码',
   `default_time_type` tinyint(1) unsigned NOT NULL DEFAULT '2' COMMENT '默认时间类型|1 投注时间|2 开赛时间 |3 结算时间，默认为2 开赛时间',
   `is_nature_day` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '默认是否勾选自然日|0 不勾选 |1 勾选，默认为1 勾选',
   PRIMARY KEY (`id`) USING BTREE,
   UNIQUE KEY `merchant_code-idx` (`merchant_code`) USING BTREE COMMENT '商户编码唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='对外商户配置表';