

##业务报表库(备份库)##
ALTER TABLE `tybss_new`.`t_merchant_group`
ADD COLUMN `group_type` tinyint(3) UNSIGNED NULL DEFAULT 1 COMMENT '1:运维组，2:业务组，3:公用组' AFTER `group_name`;

ALTER TABLE `tybss_new`.`t_merchant_group`
ADD COLUMN `group_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT NULL COMMENT '商户组code' AFTER `group_type`;

ALTER TABLE `tybss_new`.`t_merchant`
ADD COLUMN `domain_group_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT 'A' COMMENT '域名组code,关联t_merchant_domain_group.group_code' ;


