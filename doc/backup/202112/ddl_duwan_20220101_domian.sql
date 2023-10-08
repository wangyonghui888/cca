CREATE TABLE `t_domain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑id',
  `domain_type` tinyint(4) DEFAULT '0' COMMENT '域名类型 1 h5域名，2PC域名 ，3 App域名',
  `domain_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '域名',
  `enable` tinyint(4) DEFAULT '0' COMMENT '0 未使用 1已使用 2待使用 3被攻击 4被劫持',
  `enable_time` bigint(20)  DEFAULT NULL COMMENT '启用时间',
  `create_time` bigint(20)  DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建人',
  `update_time` bigint(20)  DEFAULT NULL  COMMENT '修改时间',
  `update_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '更新人',
  `merchant_group_id` bigint(10) DEFAULT NULL COMMENT '商户分组id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_domain_name` (`domain_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='域名池表';