alter table tybss_report.r_user_order_day
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;

alter table tybss_report.r_user_order_day
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;



alter table tybss_report.r_user_order_day_utc8
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;


alter table tybss_report.r_user_order_day_utc8
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;



alter table tybss_report.r_user_order_month
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;

alter table tybss_report.r_user_order_month
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;


alter table tybss_report.r_user_order_month_utc8
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;

alter table tybss_report.r_user_order_month_utc8
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;


alter table tybss_report.user_order_all
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;

alter table tybss_report.user_order_all
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;

UPDATE tybss_report.r_user_order_day_utc8 tu inner join tybss_new.t_user uo on tu.user_id = uo.uid and tu.time >= 20210701
set tu.currency_code=uo.currency_code;

UPDATE tybss_report.r_user_order_day_utc8 tu inner join tybss_new.t_currency_rate uo on tu.currency_code = uo.currency_code and tu.time >= 20210701
set tu.exchange_rate=uo.rate;