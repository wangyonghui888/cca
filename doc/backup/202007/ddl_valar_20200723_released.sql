ALTER TABLE tybss_report.`r_merchant_order_day` ADD `bet_unsettled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注未结算用户';

ALTER TABLE tybss_report.`r_merchant_order_day` ADD `bet_failed_users` INT(8) NULL DEFAULT NULL  COMMENT '投注失败用户';

ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `bet_unsettled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注未结算用户';

ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `bet_failed_users` INT(8) NULL DEFAULT NULL  COMMENT '投注失败用户';

