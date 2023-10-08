alter table tybss_new.t_user
    add column seven_day_bet_amount decimal(16, 2) null COMMENT '近七天有效投注金额';

alter table tybss_new.t_user
    add column seven_day_profit_amount decimal(16, 2) null COMMENT '近七天的用户输赢金额';

alter table tybss_new.t_user
    add column last_bet_time bigint(20) default null COMMENT '最近的投注时间';

alter table tybss_new.t_user
    add column seven_day_bet_amount decimal(16, 2) null COMMENT '近七天有效投注金额';

alter table tybss_new.t_user
    add column settled_bet_amount decimal(16, 2) null COMMENT '已结算的投注额';