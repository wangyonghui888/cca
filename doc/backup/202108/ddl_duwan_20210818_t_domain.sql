drop table if exists `tybss_new`.`t_domain`;
CREATE TABLE `tybss_new`.`t_domain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑id',
  `domain_type` tinyint(4) DEFAULT 0 COMMENT '域名类型 1 前端域名，2 App域名，3 静态资源域名',
  `domain_name` varchar(500) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '域名',
  `enable` tinyint(4) DEFAULT 0 COMMENT '0 未使用 1使用 2待使用 3已失效',
  `enable_time` datetime DEFAULT NULL COMMENT '启用时间',
  `delete_tag` tinyint(4) DEFAULT 0 COMMENT '删除状态 0 未删除 1已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_user` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT = '域名池表';
CREATE UNIQUE INDEX index_domain_name ON tybss_new.t_domain (domain_name);