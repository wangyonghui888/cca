CREATE TABLE `merchant`.`merchant_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑id',
  `merchant_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户编码',
  `file_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '文件名称',
  `file_size` bigint(20) DEFAULT '0' COMMENT '文件大小',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `operat_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '操作人',
  `page_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '来源页面',
  `export_rate` bigint(10) DEFAULT '0' COMMENT '导出进度',
  `export_param` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '导出参数',
  `export_bean` varchar(100) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '导出执行类',
  `status` bigint(4) DEFAULT '0' COMMENT '导出状态0 未开始 1 处理中 2 文件已准备 3 失败',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '导出备注',
  `file_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '文件路径',
  `end_time` bigint(20) DEFAULT '0' COMMENT '完成时间',
  `ftp_file_name` varchar(256) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT 'ftp存储文件名',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `file_name` (`file_name`) USING BTREE COMMENT '文件名',
  KEY `file_merchant_code` (`merchant_code`) USING BTREE COMMENT '商户'
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;