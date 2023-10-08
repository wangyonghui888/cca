
alter table tybss_new.t_account
    add column promotion_amount bigint(20) null    DEFAULT NULL COMMENT '活动余额(原始币种)';

alter table tybss_new.t_order
    add column order_type varchar(20) null   DEFAULT NULL COMMENT '注单类型(0正常真钱注单;其他活动ID,可能包含真钱额度)' after original_pre_bet_amount;

alter table tybss_new.t_order
    add column promotion_amount bigint(20) null    DEFAULT NULL COMMENT '活动余额(原始币种)' after order_type;

alter table tybss_new.t_settle
    add column order_type tinyint null    DEFAULT NULL COMMENT '注单类型(0正常真钱注单;1活动注单(包含真钱额度);2活动注单)';

alter table tybss_new.t_settle
    add column promotion_settle bigint(20) null    DEFAULT NULL COMMENT '活动返还额(原始币种)';

alter table tybss_new.t_settle
    add column promotion_profit bigint(20) null    DEFAULT NULL COMMENT '活动盈利额(原始币种)';