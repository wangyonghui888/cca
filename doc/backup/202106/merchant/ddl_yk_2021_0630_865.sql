ALTER TABLE `merchant`.`merchant_notice`
ADD COLUMN `begin_time` bigint(20) NULL DEFAULT 0 COMMENT '开始时间' AFTER `upload_name`,
ADD COLUMN `end_time` bigint(20) NULL DEFAULT 0 COMMENT '结束时间' AFTER `begin_time`,
ADD COLUMN `is_top` smallint(3) NULL DEFAULT 0 COMMENT '0:不置顶 1置顶' AFTER `end_time`,
ADD COLUMN `is_shuf` smallint(3) NULL DEFAULT 0 COMMENT '0不轮播 1轮播' AFTER `is_top`;