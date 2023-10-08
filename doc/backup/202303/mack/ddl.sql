-- #tybss_report 报表库执行

-- r_user_order_day新增字段
alter table r_user_order_day add column original_order_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)投注有效投注金额';
alter table r_user_order_day add column original_settle_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)结算有效投注金额';
alter table r_user_order_day add column original_settle_order_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单总金额';
alter table r_user_order_day add column original_settle_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_order_day add column original_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_order_day add column original_valid_bet_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)有效投注额';

-- r_user_order_day_utc8新增字段
alter table r_user_order_day_utc8 add column original_order_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)投注有效投注金额';
alter table r_user_order_day_utc8 add column original_settle_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)结算有效投注金额';
alter table r_user_order_day_utc8 add column original_settle_order_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单总金额';
alter table r_user_order_day_utc8 add column original_settle_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_order_day_utc8 add column original_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_order_day_utc8 add column original_valid_bet_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)有效投注额';

-- r_user_sport_order_day新增字段
alter table r_user_sport_order_day add column original_order_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)投注有效投注金额';
alter table r_user_sport_order_day add column original_settle_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)结算有效投注金额';
alter table r_user_sport_order_day add column original_settle_order_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单总金额';
alter table r_user_sport_order_day add column original_settle_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_sport_order_day add column original_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';

-- r_user_sport_order_day新增字段
alter table r_user_sport_order_day_utc8 add column original_order_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)投注有效投注金额';
alter table r_user_sport_order_day_utc8 add column original_settle_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)结算有效投注金额';
alter table r_user_sport_order_day_utc8 add column original_settle_order_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单总金额';
alter table r_user_sport_order_day_utc8 add column original_settle_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_sport_order_day_utc8 add column original_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_sport_order_day_utc8 add column original_valid_bet_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)有效投注额';

-- r_user_order_month新增字段
alter table r_user_order_month add column original_order_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)投注有效投注金额';
alter table r_user_order_month add column original_settle_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)结算有效投注金额';
alter table r_user_order_month add column original_settle_order_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单总金额';
alter table r_user_order_month add column original_settle_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_order_month add column original_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_order_month add column original_valid_bet_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)有效投注额';

-- r_user_order_month_utc8新增字段
alter table r_user_order_month_utc8 add column original_order_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)投注有效投注金额';
alter table r_user_order_month_utc8 add column original_settle_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)结算有效投注金额';
alter table r_user_order_month_utc8 add column original_settle_order_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单总金额';
alter table r_user_order_month_utc8 add column original_settle_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_order_month_utc8 add column original_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_order_month_utc8 add column original_valid_bet_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)有效投注额';

-- r_user_sport_order_month新增字段
alter table r_user_sport_order_month add column original_order_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)投注有效投注金额';
alter table r_user_sport_order_month add column original_settle_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)结算有效投注金额';
alter table r_user_sport_order_month add column original_settle_order_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单总金额';
alter table r_user_sport_order_month add column original_settle_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_sport_order_month add column original_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_sport_order_month add column original_valid_bet_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)有效投注额';

-- r_user_sport_order_month_utc8新增字段
alter table r_user_sport_order_month_utc8 add column original_order_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)投注有效投注金额';
alter table r_user_sport_order_month_utc8 add column original_settle_valid_bet_money decimal(32,0) DEFAULT NULL COMMENT '(原始币种)结算有效投注金额';
alter table r_user_sport_order_month_utc8 add column original_settle_order_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单总金额';
alter table r_user_sport_order_month_utc8 add column original_settle_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_sport_order_month_utc8 add column original_profit decimal(16,2) DEFAULT NULL COMMENT '(原始币种)盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)';
alter table r_user_sport_order_month_utc8 add column original_valid_bet_amount decimal(16,2) DEFAULT NULL COMMENT '(原始币种)有效投注额';

-- 修改币种编码字段长度
alter table r_user_order_day modify column currency_code varchar(20) DEFAULT NULL comment '币种编码';
alter table r_user_order_day_utc8 modify column currency_code varchar(20) DEFAULT NULL comment '币种编码';

alter table r_user_sport_order_day modify column currency_code varchar(20) DEFAULT NULL comment '币种编码';
alter table r_user_sport_order_day_utc8 modify column currency_code varchar(20) DEFAULT NULL comment '币种编码';

alter table r_user_order_month modify column currency_code varchar(20) DEFAULT NULL comment '币种编码';
alter table r_user_order_month_utc8 modify column currency_code varchar(20) DEFAULT NULL comment '币种编码';

alter table r_user_sport_order_month modify column currency_code varchar(20) DEFAULT NULL comment '币种编码';
alter table r_user_sport_order_month_utc8 modify column currency_code varchar(20) DEFAULT NULL comment '币种编码';



-- C/tybss_merchant_common、Y/tybss_merchant_y、S/tybss_merchant_s、B/tybss_merchant_b四组、汇总/报表tybss_merchant_common
alter table t_merchant add column currency_code varchar(500) DEFAULT NULL COMMENT '商户多币种';