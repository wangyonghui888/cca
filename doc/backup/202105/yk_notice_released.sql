ALTER TABLE `merchant`.`merchant_notice`
MODIFY COLUMN `release_range` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT '' COMMENT '发布范围,1:直营商户，2:渠道商户,3:二级商户,4:投注用户,5:panada内部用户,6:所有商户投注用户，7部分商户投注用户==》以英文逗号拼接,' AFTER `modify_time`;



ALTER TABLE `merchant`.`merchant_notice`
ADD COLUMN `merchant_codes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT '' COMMENT '发布的商户codes' AFTER `release_range`,
ADD COLUMN `is_full` tinyint(2) NULL DEFAULT 0 COMMENT '是否选中全部商户，0未全选 1全选' AFTER `merchant_codes`;



ALTER TABLE `merchant`.`merchant_notice`
ADD COLUMN `en_title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT '' COMMENT '英文公告标题' AFTER `title`,
ADD COLUMN `zh_title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT '' COMMENT '繁体公告标题' AFTER `en_title`,
ADD COLUMN `jp_title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT '' COMMENT '日文公告标题' AFTER `zh_title`,
ADD COLUMN `vi_title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT '' COMMENT '越南公告标题' AFTER `jp_title`,
ADD COLUMN `en_context` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL COMMENT '正文-英文' AFTER `context`,
ADD COLUMN `zh_context` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL COMMENT '正文-繁体' AFTER `en_context`,
ADD COLUMN `jp_context` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL COMMENT '正文-日文' AFTER `zh_context`,
ADD COLUMN `vi_context` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL COMMENT '正文-日文' AFTER `jp_context`;