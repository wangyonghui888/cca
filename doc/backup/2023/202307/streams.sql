-- 6.12 cbsy 加业务汇总库

ALTER TABLE t_ac_task
    ADD COLUMN task_tittle     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '任务名称',
    ADD COLUMN exchange_type   int(11) COMMENT '兑换方式：1老虎机  2盲盒',
    ADD COLUMN condition_match int(11) DEFAULT 0 COMMENT '條件匹配：0 ：所有条件 1：任意条件';

ALTER TABLE t_ac_task
    MODIFY COLUMN task_condition varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '任务条件JOSN串用于订单过滤',
    MODIFY COLUMN remark varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '备注',
    MODIFY COLUMN condition_id int(11) NOT NULL COMMENT '条件ID\r\n20:表示标识新的逻辑条件可单多选情况\r\n每日任务：\r\n1：每日投注x笔\r\n2：当日单笔有效投注 >= x 元\r\n3：当日投注注单数 >= x 笔\r\n4：当日完成 x 笔串关玩法\r\n5：当日完成 x 场虚拟体育赛事\r\n成长任务：\r\n1：本月累计投注天数 x 天\r\n2：本周累计有效投注 >= x 元\r\n3：本月累计有效投注 >= x 元\r\n';

ALTER TABLE t_ac_bonus_log
    ADD COLUMN task_tittle varchar(255) COMMENT '任务名称';

#------------------------------------报表库 tybss_report

--   ALTER TABLE `tybss_report`.`ac_user_order_hour`
--   ADD INDEX `index_update_time`(`updated_time`) USING BTREE;

-- ac_user_order_hour 时间要改成10分钟跑一次
drop event if EXISTS e_ac_user_order_hour;
CREATE EVENT e_ac_user_order_hour ON SCHEDULE EVERY 10 minute STARTS '2021-05-18 00:05:10' ON COMPLETION PRESERVE ENABLE DO CALL p_ac_user_order_hour(ADDDATE(now(), INTERVAL -1 hour));
-- 还有一个储存过程：p_r_user_order_hour 改动需要跑脚本

-- ac_user_order_hour 时间要改成6分钟跑一次
drop event if EXISTS e_r_user_order_hour;
CREATE EVENT e_r_user_order_hour ON SCHEDULE EVERY 6 minute STARTS '2020-06-18 00:00:05' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_hour(now());

## ------------------------------------报表库 tybss_report
ALTER TABLE r_merchant_finance_month_utc8
    ADD COLUMN original_order_valid_bet_acount       decimal(32, 2) COMMENT '投注-(原始币种)有效投注投注金额' AFTER `order_valid_bet_money`,
    ADD COLUMN original_settle_valid_bet_acount      decimal(32, 2) COMMENT '结算-(原始币种)有效投注金额' AFTER `settle_valid_bet_money`,
    ADD COLUMN original_cashout_profit_amount        decimal(32, 2) COMMENT '(原始币种)cashout盈利',
    ADD COLUMN original_cashout_amount               decimal(32, 2) COMMENT '(原始币种)cashout投注额',
    ADD COLUMN original_settle_cashout_money         decimal(32, 2) COMMENT '结算时间-(原始币种)cashout投注额',
    ADD COLUMN original_settle_cashout_profit_amount decimal(32, 2) COMMENT '结算时间-(原始币种)cashout盈利';


ALTER TABLE r_merchant_finance_day
    ADD COLUMN original_order_valid_bet_acount       decimal(32, 2) COMMENT '投注-(原始币种)有效投注投注金额' AFTER `order_valid_bet_money`,
    ADD COLUMN original_settle_valid_bet_acount      decimal(32, 2) COMMENT '结算-(原始币种)有效投注金额' AFTER `settle_valid_bet_money`,
    ADD COLUMN original_cashout_profit_amount        decimal(32, 2) COMMENT '(原始币种)cashout盈利',
    ADD COLUMN original_cashout_amount               decimal(32, 2) COMMENT '(原始币种)cashout投注额',
    ADD COLUMN original_settle_cashout_money         decimal(32, 2) COMMENT '结算时间-(原始币种)cashout投注额',
    ADD COLUMN original_settle_cashout_profit_amount decimal(32, 2) COMMENT '结算时间-(原始币种)cashout盈利';


ALTER TABLE r_merchant_finance_day_utc8
    ADD COLUMN original_order_valid_bet_acount       decimal(32, 2) COMMENT '投注-(原始币种)有效投注投注金额' AFTER `order_valid_bet_money`,
    ADD COLUMN original_settle_valid_bet_acount      decimal(32, 2) COMMENT '结算-(原始币种)有效投注金额' AFTER `settle_valid_bet_money`,
    ADD COLUMN original_cashout_profit_amount        decimal(32, 2) COMMENT '(原始币种)cashout盈利',
    ADD COLUMN original_cashout_amount               decimal(32, 2) COMMENT '(原始币种)cashout投注额',
    ADD COLUMN original_settle_cashout_money         decimal(32, 2) COMMENT '结算时间-(原始币种)cashout投注额',
    ADD COLUMN original_settle_cashout_profit_amount decimal(32, 2) COMMENT '结算时间-(原始币种)cashout盈利';

##-------- 需要执行过程：p_r_merchant_finance_day;p_r_merchant_finance_day_big;r_user_order_day.sql

ALTER TABLE r_merchant_match_bet_info_group
    ADD COLUMN order_valid_bet_count           int(6) DEFAULT NULL COMMENT '投注有效笔数',
    ADD COLUMN original_order_valid_bet_acount decimal(32, 2) COMMENT '投注-(原始币种)有效投注投注金额',
    ADD COLUMN original_order_amount_total     decimal(32, 2) COMMENT '(原始币种)投注额',
    ADD COLUMN original_profit                 decimal(32, 2) COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    ADD COLUMN original_settle_amount          decimal(32, 2) COMMENT '(原始币种)结算金额',
    ADD COLUMN original_un_settle_amount       decimal(32, 2) COMMENT '(原始币种)未结算金额';

ALTER TABLE r_match_bet_info
    ADD COLUMN order_valid_bet_count           bigint(6) DEFAULT NULL COMMENT '投注有效笔数',
    ADD COLUMN original_order_valid_bet_acount decimal(32, 2) COMMENT '投注-(原始币种)有效投注投注金额',
    ADD COLUMN original_order_amount_total     decimal(32, 2) COMMENT '(原始币种)投注额',
    ADD COLUMN original_profit                 decimal(32, 2) COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    ADD COLUMN original_settle_amount          decimal(32, 2) COMMENT '(原始币种)结算金额',
    ADD COLUMN original_un_settle_amount       decimal(32, 2) COMMENT '(原始币种)未结算金额';

ALTER TABLE r_merchant_match_bet_info
    ADD COLUMN original_order_valid_bet_acount decimal(32, 2) COMMENT '投注-(原始币种)有效投注投注金额',
    ADD COLUMN original_order_amount_total     decimal(32, 2) COMMENT '(原始币种)投注额',
    ADD COLUMN original_profit                 decimal(32, 2) COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    ADD COLUMN original_settle_amount          decimal(32, 2) COMMENT '(原始币种)结算金额',
    ADD COLUMN original_un_settle_amount       decimal(32, 2) COMMENT '(原始币种)未结算金额';


##------需要执行过程 view_r_settle_full_detail_group;view_r_order_match_detail_group;p_r_merchant_match_bet_info_group;p_r_match_bet_info;p_r_merchant_match_bet_info;

##---bug 2327  p_r_market_bet_info  p_r_merchant_market_bet_info 需要执行过程