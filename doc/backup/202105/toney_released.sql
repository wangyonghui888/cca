ALTER TABLE `tybss_report`.`r_match_settle_bet_info`
    MODIFY COLUMN `settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '已结算注单总金额' AFTER `match_status`,
    MODIFY COLUMN `settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '结算金额' AFTER `settle_bet_amount`,
    MODIFY COLUMN `pre_settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘注单总金额' AFTER `tournament_level`,
    MODIFY COLUMN `pre_settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘结算总金额' AFTER `pre_settle_bet_amount`,
    MODIFY COLUMN `pre_settle_profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘盈利总金额' AFTER `pre_settle_amount`,
    MODIFY COLUMN `live_settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球注单总金额' AFTER `pre_order_count`,
    MODIFY COLUMN `live_settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球结算总金额' AFTER `live_settle_bet_amount`,
    MODIFY COLUMN `live_settle_profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球盈利总金额' AFTER `live_settle_amount`;



ALTER TABLE `tybss_report`.`r_tournament_settle_bet_info`
    MODIFY COLUMN `settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '已结算注单总金额' AFTER `tournament_id`,
    MODIFY COLUMN `settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '结算金额' AFTER `settle_bet_amount`,
    MODIFY COLUMN `profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '盈利金额' AFTER `settle_amount`,
    MODIFY COLUMN `pre_settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘注单总金额' AFTER `settle_order_count`,
    MODIFY COLUMN `pre_settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘结算总金额' AFTER `pre_settle_bet_amount`,
    MODIFY COLUMN `pre_settle_profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘盈利总金额' AFTER `pre_settle_amount`,
    MODIFY COLUMN `live_settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球注单总金额' AFTER `pre_order_count`,
    MODIFY COLUMN `live_settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球结算总金额' AFTER `live_settle_bet_amount`,
    MODIFY COLUMN `live_settle_profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球盈利总金额' AFTER `live_settle_amount`;



ALTER TABLE `tybss_report`.`r_match_market_settle_day_utc8`
    MODIFY COLUMN `settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '已结算注单总金额' AFTER `match_type`,
    MODIFY COLUMN `settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '结算金额' AFTER `settle_bet_amount`,
    MODIFY COLUMN `profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '盈利金额' AFTER `settle_amount`;

alter table tybss_report.r_merchant_match_bet_info
    MODIFY column parlay_profit_rate decimal(32, 4) NULL COMMENT '串关盈利率';

alter table tybss_report.r_match_bet_info
    MODIFY column parlay_profit_rate decimal(32, 4) NULL COMMENT '串关盈利率';

ALTER TABLE tybss_report.r_merchant_match_bet_info
    convert to CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;

