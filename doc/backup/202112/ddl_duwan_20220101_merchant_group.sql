CREATE TABLE `t_merchant_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_name` varchar(2000) COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户组名称',
  `group_type` tinyint(3) unsigned DEFAULT '1' COMMENT '1:运维组，2:业务组，3:公用组',
  `group_code` tinyint(3) unsigned DEFAULT '1' COMMENT '商户组code 1 为电竞 2为彩票',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '是否开启 1为开启 2为关闭',
  `time_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '时间类型  1为分钟 2为小时 3为日  4为月',
  `times` tinyint(4) NOT NULL COMMENT '时间值',
  `update_time` bigint(20) DEFAULT NULL COMMENT '上次更新时间',
  `alarm_num` bigint(10) DEFAULT '20' COMMENT '报警数字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='B端商户分组表';