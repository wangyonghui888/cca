
ALTER TABLE `tybss_report`.`r_user_order_day_utc8`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_user_order_day_utc8_sub`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `updated_time`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_merchant_match_bet_info`
    ADD COLUMN `settle_valid_bet_money` decimal(20, 4) NULL COMMENT '有效投注金额' AFTER `match_type`;



ALTER TABLE `tybss_report`.`r_user_order_month`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_user_order_month_utc8`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


ALTER TABLE `tybss_report`.`r_user_order_day`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


ALTER TABLE `tybss_report`.`r_user_order_month`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_user_order_hour`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `mts_settle_return`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


ALTER TABLE `tybss_report`.`r_user_order_hour_sub`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `live_settle_return`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;





ALTER TABLE `tybss_report`.`r_merchant_order_month_utc8_sub`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `updated_time`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;

ALTER TABLE `tybss_report`.`r_user_order_month_utc8_sub`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `updated_time`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_merchant_sport_order_day`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `bet_settled_users`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_merchant_sport_order_day_utc8_sub`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `updated_time`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


ALTER TABLE `tybss_report`.`r_merchant_sport_order_day_utc8`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `bet_settled_users`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


ALTER TABLE `tybss_report`.`r_user_sport_order_day_utc8`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `failed_tickets`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_user_sport_order_day_utc8_sub`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `updated_time`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_user_sport_order_day`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `ticket_settled`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


ALTER TABLE `tybss_report`.`r_user_sport_order_month`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `ticket_settled`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;

ALTER TABLE `tybss_report`.`r_user_sport_order_month_utc8`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `ticket_settled`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_merchant_sport_order_month`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `bet_settled_users`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


ALTER TABLE `tybss_report`.`r_merchant_sport_order_month_utc8`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `bet_settled_users`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.`r_merchant_sport_order_month_utc8_sub`
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `updated_time`,
ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;





ALTER TABLE `tybss_report`.r_merchant_order_day
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;



ALTER TABLE `tybss_report`.r_merchant_order_day_utc8
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


ALTER TABLE `tybss_report`.r_merchant_order_month
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;

ALTER TABLE `tybss_report`.r_merchant_order_month_utc8
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `settle_series_profit`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;

ALTER TABLE `tybss_report`.r_merchant_order_day_utc8_sub
    ADD COLUMN `order_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '投注有效投注金额' AFTER `updated_time`,
    ADD COLUMN `settle_valid_bet_money` decimal(32, 0) NULL DEFAULT NULL COMMENT '结算有效投注金额' AFTER `order_valid_bet_money`;


