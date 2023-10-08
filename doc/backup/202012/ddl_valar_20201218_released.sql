drop table  if exists tybss_report.r_user_order_hour;
create table tybss_report.r_user_order_hour
(
    id                    varchar(64)    not null comment '自动编号'
        primary key,
    user_id               bigint(32)     null,
    user_name             varchar(32)    null,
    merchant_code         varchar(32)    null,
    merchant_name         varchar(255)   null,
    time                  int(32)        null comment '格式20200110',
    profit                decimal(16, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    return_amount         decimal(16, 2) null comment '返还金额',
    profit_rate           decimal(16, 4) null,
    bet_amount            decimal(16, 2) null comment '投注金额',
    valid_bet_amount      decimal(16, 2) null comment '有效投注额',
    bet_num               int(6)         null comment '投注笔数',
    valid_tickets         int(6)         null comment '有效注单数',
    bet_amount_settled    decimal(16, 2) null comment '已结算投注额',
    ticket_settled        int(6)         null comment '已结算订单数',
    settle_order_num      int(6)         null comment '已结算注单数',
    settle_order_amount   decimal(16, 2) null comment '已结算注单总金额',
    settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    settle_return         decimal(16)    null,
    settle_profit_rate    decimal(16, 4) null,
    updated_time          bigint         not null comment '根据此字段增量同步到elasticsearch',
    year                  int            null,
    parent_code           varchar(32)    null comment '商户父级编码',
    parent_name           varchar(100)   null comment '商户父级名称',
    settle_expect_profit  decimal(32, 2) null comment '预计盈利金额',
    settle_series_amount  decimal(32, 2) null comment '结算时间串关金额',
    settle_series_tickets decimal(32, 2) null comment '结算时间串关单数',
    settle_series_profit  decimal(32, 2) null comment '结算时间串关盈利'
);

create index index_update_time
    on tybss_report.r_user_order_hour (updated_time);

create index index_uid
    on tybss_report.user_order_all (user_id);

create index index_uid
    on tybss_report.r_user_order_hour (user_id);

create index index_uid
    on tybss_report.r_user_order_day (user_id);

create index index_uid
    on tybss_report.r_user_order_month (user_id);

create index index_uid
    on tybss_report.r_user_order_day_utc8 (user_id);


create index index_uid
    on tybss_report.r_user_order_month_utc8 (user_id);
