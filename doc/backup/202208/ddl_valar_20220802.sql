
alter table tybss_report.t_ticket add pre_order int(3)  null COMMENT '预投住订单(1.是,其他情况均为否)';

ALTER TABLE tybss_report.`user_order_all`
    DROP `achieve_amount_num`,
    DROP `achieve_amount_rate`,
    DROP `greterThan2_num`,
    DROP `lessThan2_num`,
    DROP `soccer_num`,
    DROP `basketball_num`,
    DROP `others_num`,
    DROP `series_num`,
    DROP `soccer_handicap_num`,
    DROP `all_num`,
    DROP `soccer_overunder_num`,
    DROP `basketball_handicap_num`,
    DROP `basketball_overunder_num`,
    DROP `soccer_handicap_main`,
    DROP `soccer_handicap_second`,
    DROP `soccer_overunder_main`,
    DROP `soccer_overunder_second`,
    DROP `order_valid_bet_count`,
    DROP `profit_order_num`,
    DROP `refuse_order_num`,
    DROP `refuse_order_amount`,
    DROP `cancel_order_num`,
    DROP `cancel_order_amount`;
