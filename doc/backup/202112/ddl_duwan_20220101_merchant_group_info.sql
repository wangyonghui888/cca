CREATE TABLE `t_merchant_group_info` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '逻辑id',
  `merchant_code` bigint(20) DEFAULT NULL COMMENT '商户编码',
  `merchant_name` varchar(1000) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户名称',
  `create_time` bigint(20) DEFAULT NULL COMMENT '商户入住时间',
  `merchant_group_id` bigint(20) DEFAULT NULL COMMENT '商户分组id',
  `update_time` bigint(20) DEFAULT NULL COMMENT '操作时间',
  `operator` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;