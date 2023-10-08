ALTER TABLE `tybss_merchant_common`.`t_merchant_config`
ADD COLUMN `min_series_num` tinyint(4) NOT NULL DEFAULT 2 COMMENT '最小串关数,默认值为2' AFTER `video_domain`,
ADD COLUMN `max_series_num` tinyint(4) NOT NULL DEFAULT 10 COMMENT '最大串关数,默认值为10' AFTER `min_series_num`;