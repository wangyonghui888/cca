alter table r_user_order_hour
    drop column pa_bet_tickets;

alter table r_user_order_hour
    drop column pa_bet_amount;

alter table r_user_order_hour
    drop column pa_valid_tickets;

alter table r_user_order_hour
    drop column pa_valid_bet_amount;

alter table r_user_order_hour
    drop column pa_profit;

alter table r_user_order_hour
    drop column pa_return_amount;

alter table r_user_order_hour
    drop column pa_bet_amount_settled;

alter table r_user_order_hour
    drop column pa_ticket_settled;

alter table r_user_order_hour
    drop column pa_failed_bet_amount;

alter table r_user_order_hour
    drop column pa_failed_tickets;

alter table r_user_order_hour
    drop column pa_settle_tickets;

alter table r_user_order_hour
    drop column pa_settle_bet_amount;

alter table r_user_order_hour
    drop column pa_settle_profit;

alter table r_user_order_hour
    drop column pa_settle_return;

alter table r_user_order_hour
    drop column mts_bet_tickets;

alter table r_user_order_hour
    drop column mts_bet_amount;

alter table r_user_order_hour
    drop column mts_valid_tickets;

alter table r_user_order_hour
    drop column mts_valid_bet_amount;

alter table r_user_order_hour
    drop column mts_profit;

alter table r_user_order_hour
    drop column mts_return_amount;

alter table r_user_order_hour
    drop column mts_bet_amount_settled;

alter table r_user_order_hour
    drop column mts_ticket_settled;

alter table r_user_order_hour
    drop column mts_failed_bet_amount;

alter table r_user_order_hour
    drop column mts_failed_tickets;

alter table r_user_order_hour
    drop column mts_settle_tickets;

alter table r_user_order_hour
    drop column mts_settle_bet_amount;

alter table r_user_order_hour
    drop column mts_settle_profit;

alter table r_user_order_hour
    drop column mts_settle_return;



alter table tybss_report.r_merchant_finance_day add column bet_promo_tickets int   NULL COMMENT '活动投注时间注单数' after settle_valid_bet_money;
alter table tybss_report.r_merchant_finance_day add column settle_promo_tickets int   NULL COMMENT '活动结算时间时间注单数' after bet_promo_tickets;

alter table tybss_report.r_merchant_finance_day add column cashout_amount decimal (32,2)  NULL COMMENT 'cashout投注额' after settle_promo_tickets;
alter table tybss_report.r_merchant_finance_day add column cashout_profit decimal (32,2)  NULL COMMENT 'cashout盈利';
alter table tybss_report.r_merchant_finance_day add column settle_cashout_amount decimal (32,2)  NULL COMMENT '结算时间 cashout投注额';
alter table tybss_report.r_merchant_finance_day add column settle_cashout_profit decimal (32,2)   NULL COMMENT '结算时间 cashout盈利';
alter table tybss_report.r_merchant_finance_day add column cashout_tickets int   NULL COMMENT 'cashout 注单数';
alter table tybss_report.r_merchant_finance_day add column cashout_users int   NULL COMMENT 'cashout 用户数';
alter table tybss_report.r_merchant_finance_day add column settle_cashout_tickets int   NULL COMMENT '结算时间 注单数';
alter table tybss_report.r_merchant_finance_day add column settle_cashout_users int   NULL COMMENT '结算时间 用户数';



alter table tybss_report.r_merchant_finance_day_utc8 add column bet_promo_tickets int   NULL COMMENT '活动投注时间注单数' after settle_valid_bet_money;
alter table tybss_report.r_merchant_finance_day_utc8 add column settle_promo_tickets int   NULL COMMENT '活动结算时间时间注单数' after bet_promo_tickets;
alter table tybss_report.r_merchant_finance_day_utc8 add column cashout_amount decimal (32,2)  NULL COMMENT 'cashout投注额'after settle_promo_tickets;
alter table tybss_report.r_merchant_finance_day_utc8 add column cashout_profit decimal (32,2)  NULL COMMENT 'cashout盈利';
alter table tybss_report.r_merchant_finance_day_utc8 add column settle_cashout_amount decimal (32,2)  NULL COMMENT '结算时间 cashout投注额';
alter table tybss_report.r_merchant_finance_day_utc8 add column settle_cashout_profit decimal (32,2)   NULL COMMENT '结算时间 cashout盈利';
alter table tybss_report.r_merchant_finance_day_utc8 add column cashout_tickets int   NULL COMMENT 'cashout 注单数';
alter table tybss_report.r_merchant_finance_day_utc8 add column cashout_users int   NULL COMMENT 'cashout 用户数';
alter table tybss_report.r_merchant_finance_day_utc8 add column settle_cashout_tickets int   NULL COMMENT '结算时间 注单数';
alter table tybss_report.r_merchant_finance_day_utc8 add column settle_cashout_users int   NULL COMMENT '结算时间 用户数';