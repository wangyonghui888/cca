ALTER TABLE `tybss_report`.`r_merchant_sport_order_month`
    MODIFY COLUMN `bet_user_rate` decimal(32, 4) NULL DEFAULT NULL AFTER `register_total_user_sum`;



ALTER TABLE `tybss_report`.`r_merchant_order_month`
    MODIFY COLUMN `bet_user_rate` decimal(32, 4) NULL DEFAULT NULL AFTER `time`;




ALTER TABLE `tybss_report`.`r_user_order_hour`
    MODIFY COLUMN `settle_profit_rate` decimal(32, 4) NULL DEFAULT NULL AFTER `settle_return`;