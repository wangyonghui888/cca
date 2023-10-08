drop table if exists tybss_report.r_user_sport_order_hour;
create table tybss_report.r_user_sport_order_hour
(
    id                 varchar(64)    not null comment ' 自动编号 '
        primary key,
    sport_id           int(2)         not null comment '球类id',
    user_id            bigint(32)     not null,
    merchant_code      varchar(32)    null,
    parent_code        varchar(32)    null comment '商户父级编码',
    time               int(32)        null comment '格式120200110',
    bet_tickets        int            null comment '投注笔数',
    bet_amount         decimal(32, 2) null comment '投注金额',
    valid_tickets      int(6)         null comment '有效投注笔数',
    valid_bet_amount   decimal(32, 2) null comment '有效投注金额',
    profit             decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    return_amount      decimal(32, 2) null comment '返还金额',
    bet_amount_settled decimal(32, 2) null comment '已结算投注额',
    ticket_settled     int            null comment '已结算订单数',
    failed_bet_amount  decimal(32, 2) null comment '投注失败(拒单)金额',
    failed_tickets     int            null comment '投注失败(拒单)订单数',
    settle_tickets     int(6)         null comment '已结算注单数',
    settle_bet_amount  decimal(16, 2) null comment '已结算注单总金额',
    settle_profit      decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    settle_return      decimal(16)    null,
    updated_time       bigint         not null comment '根据此字段增量同步到elasticsearch'
);

create index index_update_time
    on tybss_report.r_user_sport_order_hour (updated_time);
create index index_uid_sportId
    on tybss_report.r_user_sport_order_hour (user_id, sport_id);

drop table if exists tybss_report.r_user_sport_order_hour_sub;

create table tybss_report.r_user_sport_order_hour_sub
(
    id                        varchar(64)    not null comment ' 自动编号 '
        primary key,
    sport_id                  int            not null comment '球类id',
    user_id                   bigint(32)     not null,
    merchant_code             varchar(32)    null,
    parent_code               varchar(32)    null comment '商户父级编码',
    time                      int(32)        null comment '格式120200110',
    pre_bet_tickets           int            null comment '投注笔数',
    pre_bet_amount            decimal(32, 2) null comment '投注金额',
    pre_valid_tickets         int(6)         null comment '有效投注笔数',
    pre_valid_bet_amount      decimal(32, 2) null comment '有效投注金额',
    pre_profit                decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pre_return_amount         decimal(32, 2) null comment '返还金额',
    pre_bet_amount_settled    decimal(32, 2) null comment '已结算投注额',
    pre_ticket_settled        int            null comment '已结算订单数',
    pre_failed_bet_amount     decimal(32, 2) null comment '投注失败(拒单)金额',
    pre_failed_tickets        int            null comment '投注失败(拒单)订单数',
    pre_settle_tickets        int(6)         null comment '已结算注单数',
    pre_settle_bet_amount     decimal(16, 2) null comment '已结算注单总金额',
    pre_settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pre_settle_return         decimal(16)    null,

    live_bet_tickets          int            null comment '投注笔数',
    live_bet_amount           decimal(32, 2) null comment '投注金额',
    live_valid_tickets        int(6)         null comment '有效投注笔数',
    live_valid_bet_amount     decimal(32, 2) null comment '有效投注金额',
    live_profit               decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    live_return_amount        decimal(32, 2) null comment '返还金额',
    live_bet_amount_settled   decimal(32, 2) null comment '已结算投注额',
    live_ticket_settled       int            null comment '已结算订单数',
    live_failed_bet_amount    decimal(32, 2) null comment '投注失败(拒单)金额',
    live_failed_tickets       int            null comment '投注失败(拒单)订单数',
    live_settle_tickets       int(6)         null comment '已结算注单数',
    live_settle_bet_amount    decimal(16, 2) null comment '已结算注单总金额',
    live_settle_profit        decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    live_settle_return        decimal(16)    null,

    pa_bet_tickets            int            null comment '投注笔数',
    pa_bet_amount             decimal(32, 2) null comment '投注金额',
    pa_valid_tickets          int(6)         null comment '有效投注笔数',
    pa_valid_bet_amount       decimal(32, 2) null comment '有效投注金额',
    pa_profit                 decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pa_return_amount          decimal(32, 2) null comment '返还金额',
    pa_bet_amount_settled     decimal(32, 2) null comment '已结算投注额',
    pa_ticket_settled         int            null comment '已结算订单数',
    pa_failed_bet_amount      decimal(32, 2) null comment '投注失败(拒单)金额',
    pa_failed_tickets         int            null comment '投注失败(拒单)订单数',
    pa_settle_tickets         int(6)         null comment '已结算注单数',
    pa_settle_bet_amount      decimal(16, 2) null comment '已结算注单总金额',
    pa_settle_profit          decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pa_settle_return          decimal(16)    null,

    mts_bet_tickets           int            null comment '投注笔数',
    mts_bet_amount            decimal(32, 2) null comment '投注金额',
    mts_valid_tickets         int(6)         null comment '有效投注笔数',
    mts_valid_bet_amount      decimal(32, 2) null comment '有效投注金额',
    mts_profit                decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    mts_return_amount         decimal(32, 2) null comment '返还金额',
    mts_bet_amount_settled    decimal(32, 2) null comment '已结算投注额',
    mts_ticket_settled        int            null comment '已结算订单数',
    mts_failed_bet_amount     decimal(32, 2) null comment '投注失败(拒单)金额',
    mts_failed_tickets        int            null comment '投注失败(拒单)订单数',
    mts_settle_tickets        int(6)         null comment '已结算注单数',
    mts_settle_bet_amount     decimal(16, 2) null comment '已结算注单总金额',
    mts_settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    mts_settle_return         decimal(16)    null,
    updated_time              bigint         not null comment '根据此字段增量同步到elasticsearch'
);

create index index_update_time
    on tybss_report.r_user_sport_order_hour_sub (updated_time);
create index index_uid_sportId
    on tybss_report.r_user_sport_order_hour_sub (user_id, sport_id);


drop table if exists tybss_report.r_user_order_hour_sub;

create table tybss_report.r_user_order_hour_sub
(
    id                        varchar(64)    not null comment ' 自动编号 '
        primary key,
    user_id                   bigint(32)     not null,
    merchant_code             varchar(32)    null,
    parent_code               varchar(32)    null comment '商户父级编码',
    time                      int            not null comment '格式2020011023',
    pre_bet_tickets           int            null comment '投注笔数',
    pre_bet_amount            decimal(32, 2) null comment '投注金额',
    pre_valid_tickets         int            null comment '有效投注笔数',
    pre_valid_bet_amount      decimal(32, 2) null comment '有效投注金额',
    pre_profit                decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pre_return_amount         decimal(32, 2) null comment '返还金额',
    pre_bet_amount_settled    decimal(32, 2) null comment '已结算投注额',
    pre_ticket_settled        int            null comment '已结算订单数',
    pre_failed_bet_amount     decimal(32, 2) null comment '投注失败(拒单)金额',
    pre_failed_tickets        int            null comment '投注失败(拒单)订单数',
    pre_settle_tickets        int            null comment '已结算注单数',
    pre_settle_bet_amount     decimal(16, 2) null comment '已结算注单总金额',
    pre_settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pre_settle_return         decimal(16)    null,

    live_bet_tickets          int            null comment '投注笔数',
    live_bet_amount           decimal(32, 2) null comment '投注金额',
    live_valid_tickets        int            null comment '有效投注笔数',
    live_valid_bet_amount     decimal(32, 2) null comment '有效投注金额',
    live_profit               decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    live_return_amount        decimal(32, 2) null comment '返还金额',
    live_bet_amount_settled   decimal(32, 2) null comment '已结算投注额',
    live_ticket_settled       int            null comment '已结算订单数',
    live_failed_bet_amount    decimal(32, 2) null comment '投注失败(拒单)金额',
    live_failed_tickets       int            null comment '投注失败(拒单)订单数',
    live_settle_tickets       int            null comment '已结算注单数',
    live_settle_bet_amount    decimal(16, 2) null comment '已结算注单总金额',
    live_settle_profit        decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    live_settle_return        decimal(16)    null,

    pa_bet_tickets            int            null comment '投注笔数',
    pa_bet_amount             decimal(32, 2) null comment '投注金额',
    pa_valid_tickets          int            null comment '有效投注笔数',
    pa_valid_bet_amount       decimal(32, 2) null comment '有效投注金额',
    pa_profit                 decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pa_return_amount          decimal(32, 2) null comment '返还金额',
    pa_bet_amount_settled     decimal(32, 2) null comment '已结算投注额',
    pa_ticket_settled         int            null comment '已结算订单数',
    pa_failed_bet_amount      decimal(32, 2) null comment '投注失败(拒单)金额',
    pa_failed_tickets         int            null comment '投注失败(拒单)订单数',
    pa_settle_tickets         int            null comment '已结算注单数',
    pa_settle_bet_amount      decimal(16, 2) null comment '已结算注单总金额',
    pa_settle_profit          decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pa_settle_return          decimal(16)    null,

    mts_bet_tickets           int            null comment '投注笔数',
    mts_bet_amount            decimal(32, 2) null comment '投注金额',
    mts_valid_tickets         int            null comment '有效投注笔数',
    mts_valid_bet_amount      decimal(32, 2) null comment '有效投注金额',
    mts_profit                decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    mts_return_amount         decimal(32, 2) null comment '返还金额',
    mts_bet_amount_settled    decimal(32, 2) null comment '已结算投注额',
    mts_ticket_settled        int            null comment '已结算订单数',
    mts_failed_bet_amount     decimal(32, 2) null comment '投注失败(拒单)金额',
    mts_failed_tickets        int            null comment '投注失败(拒单)订单数',
    mts_settle_tickets        int            null comment '已结算注单数',
    mts_settle_bet_amount     decimal(16, 2) null comment '已结算注单总金额',
    mts_settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    mts_settle_return         decimal(16)    null,

    series_bet_tickets        int            null comment '串关投注笔数',
    series_bet_amount         decimal(32, 2) null comment '串关投注金额',
    series_valid_tickets      int            null comment '串关有效投注笔数',
    series_valid_bet_amount   decimal(32, 2) null comment '串关有效投注金额',
    series_profit             decimal(32, 2) null comment '串关盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    series_return_amount      decimal(32, 2) null comment '串关返还金额',
    series_bet_amount_settled decimal(32, 2) null comment '串关已结算投注额',
    series_ticket_settled     int            null comment '串关已结算订单数',
    series_failed_bet_amount  decimal(32, 2) null comment '串关投注失败(拒单)金额',
    series_failed_tickets     int            null comment '串关投注失败(拒单)订单数',
    series_settle_tickets     int            null comment '串关已结算注单数',
    series_settle_bet_amount  decimal(16, 2) null comment '串关已结算注单总金额',
    series_settle_profit      decimal(16, 2) null comment '串关已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    series_settle_return      decimal(16)    null
);

create index index_uid_sportId
    on tybss_report.r_user_order_hour_sub (user_id);


drop table if exists tybss_report.r_user_order_day_utc8_sub;

create table tybss_report.r_user_order_day_utc8_sub
(
    id                        varchar(64)    not null comment ' 自动编号 '
        primary key,
    user_id                   bigint(32)     not null,
    merchant_code             varchar(32)    null,
    parent_code               varchar(32)    null comment '商户父级编码',
    time                      int            not null comment '格式20200110',
    pre_bet_tickets           int            null comment '投注笔数',
    pre_bet_amount            decimal(32, 2) null comment '投注金额',
    pre_valid_tickets         int            null comment '有效投注笔数',
    pre_valid_bet_amount      decimal(32, 2) null comment '有效投注金额',
    pre_profit                decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pre_return_amount         decimal(32, 2) null comment '返还金额',
    pre_bet_amount_settled    decimal(32, 2) null comment '已结算投注额',
    pre_ticket_settled        int            null comment '已结算订单数',
    pre_failed_bet_amount     decimal(32, 2) null comment '投注失败(拒单)金额',
    pre_failed_tickets        int            null comment '投注失败(拒单)订单数',
    pre_settle_tickets        int            null comment '已结算注单数',
    pre_settle_bet_amount     decimal(16, 2) null comment '已结算注单总金额',
    pre_settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pre_settle_return         decimal(16, 2) null,

    live_bet_tickets          int            null comment '投注笔数',
    live_bet_amount           decimal(32, 2) null comment '投注金额',
    live_valid_tickets        int            null comment '有效投注笔数',
    live_valid_bet_amount     decimal(32, 2) null comment '有效投注金额',
    live_profit               decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    live_return_amount        decimal(32, 2) null comment '返还金额',
    live_bet_amount_settled   decimal(32, 2) null comment '已结算投注额',
    live_ticket_settled       int            null comment '已结算订单数',
    live_failed_bet_amount    decimal(32, 2) null comment '投注失败(拒单)金额',
    live_failed_tickets       int            null comment '投注失败(拒单)订单数',
    live_settle_tickets       int            null comment '已结算注单数',
    live_settle_bet_amount    decimal(16, 2) null comment '已结算注单总金额',
    live_settle_profit        decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    live_settle_return        decimal(16, 2) null,

    pa_bet_tickets            int            null comment '投注笔数',
    pa_bet_amount             decimal(32, 2) null comment '投注金额',
    pa_valid_tickets          int            null comment '有效投注笔数',
    pa_valid_bet_amount       decimal(32, 2) null comment '有效投注金额',
    pa_profit                 decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pa_return_amount          decimal(32, 2) null comment '返还金额',
    pa_bet_amount_settled     decimal(32, 2) null comment '已结算投注额',
    pa_ticket_settled         int            null comment '已结算订单数',
    pa_failed_bet_amount      decimal(32, 2) null comment '投注失败(拒单)金额',
    pa_failed_tickets         int            null comment '投注失败(拒单)订单数',
    pa_settle_tickets         int            null comment '已结算注单数',
    pa_settle_bet_amount      decimal(16, 2) null comment '已结算注单总金额',
    pa_settle_profit          decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pa_settle_return          decimal(16, 2) null,

    mts_bet_tickets           int            null comment '投注笔数',
    mts_bet_amount            decimal(32, 2) null comment '投注金额',
    mts_valid_tickets         int            null comment '有效投注笔数',
    mts_valid_bet_amount      decimal(32, 2) null comment '有效投注金额',
    mts_profit                decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    mts_return_amount         decimal(32, 2) null comment '返还金额',
    mts_bet_amount_settled    decimal(32, 2) null comment '已结算投注额',
    mts_ticket_settled        int            null comment '已结算订单数',
    mts_failed_bet_amount     decimal(32, 2) null comment '投注失败(拒单)金额',
    mts_failed_tickets        int            null comment '投注失败(拒单)订单数',
    mts_settle_tickets        int            null comment '已结算注单数',
    mts_settle_bet_amount     decimal(16, 2) null comment '已结算注单总金额',
    mts_settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    mts_settle_return         decimal(16, 2) null,

    series_bet_tickets        int            null comment '串关投注笔数',
    series_bet_amount         decimal(32, 2) null comment '串关投注金额',
    series_valid_tickets      int            null comment '串关有效投注笔数',
    series_valid_bet_amount   decimal(32, 2) null comment '串关有效投注金额',
    series_profit             decimal(32, 2) null comment '串关盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    series_return_amount      decimal(32, 2) null comment '串关返还金额',
    series_bet_amount_settled decimal(32, 2) null comment '串关已结算投注额',
    series_ticket_settled     int            null comment '串关已结算订单数',
    series_failed_bet_amount  decimal(32, 2) null comment '串关投注失败(拒单)金额',
    series_failed_tickets     int            null comment '串关投注失败(拒单)订单数',
    series_settle_tickets     int            null comment '串关已结算注单数',
    series_settle_bet_amount  decimal(16, 2) null comment '串关已结算注单总金额',
    series_settle_profit      decimal(16, 2) null comment '串关已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    series_settle_return      decimal(16, 2) null,
    updated_time              bigint         not null comment '根据此字段增量同步到elasticsearch'
);

create index index_update_time
    on tybss_report.r_user_order_day_utc8_sub (updated_time);
create index index_uid
    on tybss_report.r_user_order_day_utc8_sub (user_id);


drop table if exists tybss_report.r_user_order_month_utc8_sub;

create table tybss_report.r_user_order_month_utc8_sub
(
    id                        varchar(64)    not null comment ' 自动编号 '
        primary key,
    user_id                   bigint(32)     not null,
    merchant_code             varchar(32)    null,
    parent_code               varchar(32)    null comment '商户父级编码',
    time                      int            not null comment '格式202001',
    pre_bet_tickets           int            null comment '投注笔数',
    pre_bet_amount            decimal(32, 2) null comment '投注金额',
    pre_valid_tickets         int            null comment '有效投注笔数',
    pre_valid_bet_amount      decimal(32, 2) null comment '有效投注金额',
    pre_profit                decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pre_return_amount         decimal(32, 2) null comment '返还金额',
    pre_bet_amount_settled    decimal(32, 2) null comment '已结算投注额',
    pre_ticket_settled        int            null comment '已结算订单数',
    pre_failed_bet_amount     decimal(32, 2) null comment '投注失败(拒单)金额',
    pre_failed_tickets        int            null comment '投注失败(拒单)订单数',
    pre_settle_tickets        int            null comment '已结算注单数',
    pre_settle_bet_amount     decimal(16, 2) null comment '已结算注单总金额',
    pre_settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pre_settle_return         decimal(16, 2) null,

    live_bet_tickets          int            null comment '投注笔数',
    live_bet_amount           decimal(32, 2) null comment '投注金额',
    live_valid_tickets        int            null comment '有效投注笔数',
    live_valid_bet_amount     decimal(32, 2) null comment '有效投注金额',
    live_profit               decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    live_return_amount        decimal(32, 2) null comment '返还金额',
    live_bet_amount_settled   decimal(32, 2) null comment '已结算投注额',
    live_ticket_settled       int            null comment '已结算订单数',
    live_failed_bet_amount    decimal(32, 2) null comment '投注失败(拒单)金额',
    live_failed_tickets       int            null comment '投注失败(拒单)订单数',
    live_settle_tickets       int            null comment '已结算注单数',
    live_settle_bet_amount    decimal(16, 2) null comment '已结算注单总金额',
    live_settle_profit        decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    live_settle_return        decimal(16, 2) null,

    pa_bet_tickets            int            null comment '投注笔数',
    pa_bet_amount             decimal(32, 2) null comment '投注金额',
    pa_valid_tickets          int            null comment '有效投注笔数',
    pa_valid_bet_amount       decimal(32, 2) null comment '有效投注金额',
    pa_profit                 decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pa_return_amount          decimal(32, 2) null comment '返还金额',
    pa_bet_amount_settled     decimal(32, 2) null comment '已结算投注额',
    pa_ticket_settled         int            null comment '已结算订单数',
    pa_failed_bet_amount      decimal(32, 2) null comment '投注失败(拒单)金额',
    pa_failed_tickets         int            null comment '投注失败(拒单)订单数',
    pa_settle_tickets         int            null comment '已结算注单数',
    pa_settle_bet_amount      decimal(16, 2) null comment '已结算注单总金额',
    pa_settle_profit          decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    pa_settle_return          decimal(16, 2) null,

    mts_bet_tickets           int            null comment '投注笔数',
    mts_bet_amount            decimal(32, 2) null comment '投注金额',
    mts_valid_tickets         int            null comment '有效投注笔数',
    mts_valid_bet_amount      decimal(32, 2) null comment '有效投注金额',
    mts_profit                decimal(32, 2) null comment '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    mts_return_amount         decimal(32, 2) null comment '返还金额',
    mts_bet_amount_settled    decimal(32, 2) null comment '已结算投注额',
    mts_ticket_settled        int            null comment '已结算订单数',
    mts_failed_bet_amount     decimal(32, 2) null comment '投注失败(拒单)金额',
    mts_failed_tickets        int            null comment '投注失败(拒单)订单数',
    mts_settle_tickets        int            null comment '已结算注单数',
    mts_settle_bet_amount     decimal(16, 2) null comment '已结算注单总金额',
    mts_settle_profit         decimal(16, 2) null comment '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    mts_settle_return         decimal(16, 2) null,

    series_bet_tickets        int            null comment '串关投注笔数',
    series_bet_amount         decimal(32, 2) null comment '串关投注金额',
    series_valid_tickets      int            null comment '串关有效投注笔数',
    series_valid_bet_amount   decimal(32, 2) null comment '串关有效投注金额',
    series_profit             decimal(32, 2) null comment '串关盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    series_return_amount      decimal(32, 2) null comment '串关返还金额',
    series_bet_amount_settled decimal(32, 2) null comment '串关已结算投注额',
    series_ticket_settled     int            null comment '串关已结算订单数',
    series_failed_bet_amount  decimal(32, 2) null comment '串关投注失败(拒单)金额',
    series_failed_tickets     int            null comment '串关投注失败(拒单)订单数',
    series_settle_tickets     int            null comment '串关已结算注单数',
    series_settle_bet_amount  decimal(16, 2) null comment '串关已结算注单总金额',
    series_settle_profit      decimal(16, 2) null comment '串关已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    series_settle_return      decimal(16, 2) null,
    updated_time              bigint         not null comment '根据此字段增量同步到elasticsearch'
);

create index index_update_time
    on tybss_report.r_user_order_month_utc8_sub (updated_time);
create index index_uid
    on tybss_report.r_user_order_month_utc8_sub (user_id);