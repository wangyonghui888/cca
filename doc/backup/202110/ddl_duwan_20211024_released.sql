ALTER TABLE `merchant`.`merchant_file`
MODIFY COLUMN `file_name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL COMMENT '文件名称' AFTER `merchant_code`,
MODIFY COLUMN `ftp_file_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL COMMENT 'ftp存储文件名' AFTER `end_time`;