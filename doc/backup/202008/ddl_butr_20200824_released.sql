ALTER TABLE tybss_report.`r_user_order_day` ADD `settle_expect_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间预计盈利金额';
ALTER TABLE tybss_report.`r_user_order_day` ADD `settle_series_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关金额';
ALTER TABLE tybss_report.`r_user_order_day` ADD `settle_series_tickets` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关单数';
ALTER TABLE tybss_report.`r_user_order_day` ADD `settle_series_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关盈利';


ALTER TABLE tybss_report.`r_user_order_day_utc8` ADD `settle_expect_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间预计盈利金额';
ALTER TABLE tybss_report.`r_user_order_day_utc8` ADD `settle_series_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关金额';
ALTER TABLE tybss_report.`r_user_order_day_utc8` ADD `settle_series_tickets` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关单数';
ALTER TABLE tybss_report.`r_user_order_day_utc8` ADD `settle_series_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关盈利';

ALTER TABLE tybss_report.`r_merchant_order_day` ADD `settle_expect_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间预计盈利金额' ;
ALTER TABLE tybss_report.`r_merchant_order_day` ADD `settle_series_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关金额';
ALTER TABLE tybss_report.`r_merchant_order_day` ADD `settle_series_tickets` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关单数';
ALTER TABLE tybss_report.`r_merchant_order_day` ADD `settle_series_users` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关用户数';
ALTER TABLE tybss_report.`r_merchant_order_day` ADD `settle_series_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关盈利';

ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `settle_expect_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间预计盈利金额';
ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `settle_series_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关金额';
ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `settle_series_tickets` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关单数';
ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `settle_series_users` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关用户数';
ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `settle_series_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关盈利';

ALTER TABLE tybss_report.`r_user_order_month` ADD `settle_expect_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间预计盈利金额';
ALTER TABLE tybss_report.`r_user_order_month` ADD `settle_series_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关金额';
ALTER TABLE tybss_report.`r_user_order_month` ADD `settle_series_tickets` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关单数';
ALTER TABLE tybss_report.`r_user_order_month` ADD `settle_series_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关盈利';


ALTER TABLE tybss_report.`r_user_order_month_utc8` ADD `settle_expect_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间预计盈利金额';
ALTER TABLE tybss_report.`r_user_order_month_utc8` ADD `settle_series_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关金额';
ALTER TABLE tybss_report.`r_user_order_month_utc8` ADD `settle_series_tickets` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关单数';
ALTER TABLE tybss_report.`r_user_order_month_utc8` ADD `settle_series_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关盈利';

ALTER TABLE tybss_report.`r_merchant_order_month` ADD `settle_expect_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间预计盈利金额' ;
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `settle_series_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关金额';
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `settle_series_tickets` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关单数';
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `settle_series_users` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关用户数';
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `settle_series_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关盈利';

ALTER TABLE tybss_report.`r_merchant_order_month_utc8` ADD `settle_expect_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间预计盈利金额';
ALTER TABLE tybss_report.`r_merchant_order_month_utc8` ADD `settle_series_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关金额';
ALTER TABLE tybss_report.`r_merchant_order_month_utc8` ADD `settle_series_tickets` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关单数';
ALTER TABLE tybss_report.`r_merchant_order_month_utc8` ADD `settle_series_users` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关用户数';
ALTER TABLE tybss_report.`r_merchant_order_month_utc8` ADD `settle_series_profit` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '结算时间串关盈利';
