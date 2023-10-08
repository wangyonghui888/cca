alter table tybss_report.r_user_sport_order_day
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;

alter table tybss_report.r_user_sport_order_day
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;




alter table tybss_report.r_user_sport_order_day_utc8
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;

alter table tybss_report.r_user_sport_order_day_utc8
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;




alter table tybss_report.r_user_sport_order_month
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;

alter table tybss_report.r_user_sport_order_month
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;




alter table tybss_report.r_user_sport_order_month_utc8
    add column currency_code varchar(2) DEFAULT NULL COMMENT '币种' after user_name;

alter table tybss_report.r_user_sport_order_month_utc8
    add column exchange_rate decimal(20, 2) DEFAULT NULL COMMENT '人民币汇率' after currency_code;

