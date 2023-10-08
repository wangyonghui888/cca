
alter table tybss_report.r_user_sport_order_month_utc8 modify column sport_id int ;

alter table tybss_report.r_user_sport_order_month modify column sport_id int ;

alter table tybss_report.r_merchant_sport_order_month modify column sport_id int ;

alter table tybss_report.r_merchant_sport_order_month_utc8 modify column sport_id int ;

alter table tybss_report.r_merchant_sport_order_day modify column sport_id int ;

alter table tybss_report.r_merchant_sport_order_day_utc8 modify column sport_id int ;


alter table tybss_report.r_user_order_hour
    add column failed_bet_amount decimal(32, 2) NULL COMMENT '投注失败(拒单)金额' after valid_tickets;
alter table tybss_report.r_user_order_hour
    add column failed_tickets int NULL COMMENT '投注失败(拒单)订单数' after failed_bet_amount;


alter table tybss_report.r_user_order_day_utc8
    add column failed_bet_amount decimal(32, 2) NULL COMMENT '投注失败(拒单)金额' after valid_tickets;
alter table tybss_report.r_user_order_day_utc8
    add column failed_tickets int NULL COMMENT '投注失败(拒单)订单数' after failed_bet_amount;


alter table tybss_report.r_user_order_month_utc8
    add column failed_bet_amount decimal(32, 2) NULL COMMENT '投注失败(拒单)金额' after valid_tickets;

alter table tybss_report.r_user_order_month_utc8
    add column failed_tickets int NULL COMMENT '投注失败(拒单)订单数' after failed_bet_amount;


alter table tybss_report.r_user_sport_order_day_utc8
    add column failed_bet_amount decimal(32, 2) NULL COMMENT '投注失败(拒单)金额';
alter table tybss_report.r_user_sport_order_day_utc8
    add column failed_tickets int NULL COMMENT '投注失败(拒单)订单数';

alter table tybss_report.r_user_sport_order_day_utc8
    add column valid_tickets int NULL COMMENT '有效投注订单数' after bet_amount;
alter table tybss_report.r_user_sport_order_day_utc8
    add column valid_bet_amount decimal(32, 2) NULL COMMENT '有效投注金额' after valid_tickets;


alter table tybss_report.r_user_sport_order_month_utc8
    add column failed_bet_amount decimal(32, 2) NULL COMMENT '投注失败(拒单)金额';
alter table tybss_report.r_user_sport_order_month_utc8
    add column failed_tickets int NULL COMMENT '投注失败(拒单)订单数';

alter table tybss_report.r_user_sport_order_month_utc8
    add column valid_tickets int NULL COMMENT '有效投注订单数' after bet_amount;
alter table tybss_report.r_user_sport_order_month_utc8
    add column valid_bet_amount decimal(32, 2) NULL COMMENT '有效投注金额' after valid_tickets;


alter table tybss_report.r_merchant_sport_order_day_utc8
    add column valid_bet_amount decimal(32, 2) NULL COMMENT '有效投注金额' after order_sum;
alter table tybss_report.r_merchant_sport_order_day_utc8
    add column valid_tickets int NULL COMMENT '有效投注订单数' after valid_bet_amount;

alter table tybss_report.r_merchant_sport_order_day_utc8
    add column failed_bet_amount decimal(32, 2) NULL COMMENT '投注失败(拒单)金额' after valid_tickets;
alter table tybss_report.r_merchant_sport_order_day_utc8
    add column failed_tickets int NULL COMMENT '投注失败(拒单)订单数' after failed_bet_amount;


alter table tybss_report.r_merchant_sport_order_month_utc8
    add column valid_bet_amount decimal(32, 2) NULL COMMENT '有效投注金额' after order_sum;
alter table tybss_report.r_merchant_sport_order_month_utc8
    add column valid_tickets int NULL COMMENT '有效投注订单数' after valid_bet_amount;


alter table tybss_report.r_merchant_sport_order_month_utc8
    add column failed_bet_amount decimal(32, 2) NULL COMMENT '投注失败(拒单)金额' after valid_tickets;
alter table tybss_report.r_merchant_sport_order_month_utc8
    add column failed_tickets int NULL COMMENT '投注失败(拒单)订单数' after failed_bet_amount;

