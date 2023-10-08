
ALTER TABLE `tybss_report`.`r_merchant_match_bet_info_group`
    ADD COLUMN `settle_valid_bet_money` decimal(20, 4) NULL COMMENT '有效投注金额' AFTER `parlay_profit_rate`;