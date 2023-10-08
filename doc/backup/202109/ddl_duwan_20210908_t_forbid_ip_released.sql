drop table if exists `tybss_new`.`t_forbid_ip`;
CREATE TABLE `tybss_new`.`t_forbid_ip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑id',
  `ip_name` varchar(255)  COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '异常Ip',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT = '异常IP表';
CREATE UNIQUE INDEX index_ip_name ON tybss_new.t_forbid_ip (ip_name);