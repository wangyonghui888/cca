CREATE TABLE `merchant`.`merchant_file`  (
  `id` bigint(20) NOT NULL COMMENT '逻辑id',
  `merchant_code` varchar(128) NULL COMMENT '商户编码',
  `file_name` varchar(256) NULL COMMENT '文件名称',
  `file_size` bigint(20) NULL COMMENT '文件大小',
  `create_time` bigint(20) NULL COMMENT '创建时间',
  `operat_name` varchar(50) NULL COMMENT '操作人',
  `page_name` varchar(256) NULL COMMENT '来源页面',
  `export_rate` bigint(10) NULL COMMENT '导出进度',
  `export_param` varchar(1024) NULL COMMENT '导出参数',
  `export_type` bigint(10) NULL COMMENT '导出来源类型',
  `status` bigint(4) NULL COMMENT '导出状态1 未开始 2 处理中 3 文件已准备',
  `remark` varchar(512) NULL COMMENT '导出备注',
  `file_path` varchar(256) NULL COMMENT '文件路径',
  PRIMARY KEY (`id`),
  INDEX `file_name`(`file_name`) USING BTREE COMMENT '文件名',
  INDEX `file_merchant_code`(`merchant_code`) USING BTREE COMMENT '商户'
);