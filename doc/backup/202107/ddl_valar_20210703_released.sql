
alter table tybss_report.ac_user_order_hour
    add column olympic_bet Decimal(32, 2) DEFAULT NULL COMMENT '奥运会投注' after eu_valid_bet_amount;

alter table tybss_report.ac_user_order_hour
    add column olympic_profit Decimal(32, 2) DEFAULT NULL COMMENT '奥运会投注盈利' after olympic_bet;

alter table tybss_report.ac_user_order_hour
    add column olympic_tickets int DEFAULT NULL COMMENT '奥运会投注主单' after olympic_profit;



alter table tybss_report.ac_user_order_hour
    add column olympic_play1_tickets int DEFAULT NULL COMMENT '全场独赢主单' after olympic_tickets;

alter table tybss_report.ac_user_order_hour
    add column olympic_play2_tickets int DEFAULT NULL COMMENT '全场大小主单' after olympic_play1_tickets;

alter table tybss_report.ac_user_order_hour
    add column olympic_play4_tickets int DEFAULT NULL COMMENT '全场让球主单' after olympic_play2_tickets;



alter table tybss_report.ac_user_order_hour
    add column olympic_play17_tickets int DEFAULT NULL COMMENT '半场独赢主单' after olympic_play4_tickets;

alter table tybss_report.ac_user_order_hour
    add column olympic_play18_tickets int DEFAULT NULL COMMENT '半场大小主单' after olympic_play17_tickets;

alter table tybss_report.ac_user_order_hour
    add column olympic_play19_tickets int DEFAULT NULL COMMENT '半场让球主单' after olympic_play18_tickets;

alter table tybss_report.ac_user_order_hour
    add column olympic_corner_tickets int DEFAULT NULL COMMENT '角球主单' after olympic_play19_tickets;

alter table tybss_report.ac_user_order_hour
    add column olympic_punish_tickets int DEFAULT NULL COMMENT '罚牌主单' after olympic_corner_tickets;

alter table tybss_report.ac_user_order_hour
    add column olympic_win_tickets int DEFAULT NULL COMMENT '盈利主单' after olympic_punish_tickets;










alter table tybss_report.ac_user_order_day
    add column olympic_bet Decimal(32, 2) DEFAULT NULL COMMENT '奥运会投注' after eu_valid_bet_amount;

alter table tybss_report.ac_user_order_day
    add column olympic_profit Decimal(32, 2) DEFAULT NULL COMMENT '奥运会投注盈利' after olympic_bet;

alter table tybss_report.ac_user_order_day
    add column olympic_tickets int DEFAULT NULL COMMENT '奥运会投注主单' after olympic_profit;


alter table tybss_report.ac_user_order_day
    add column olympic_play1_tickets int DEFAULT NULL COMMENT '全场独赢主单' after olympic_tickets;

alter table tybss_report.ac_user_order_day
    add column olympic_play2_tickets int DEFAULT NULL COMMENT '全场大小主单' after olympic_play1_tickets;

alter table tybss_report.ac_user_order_day
    add column olympic_play4_tickets int DEFAULT NULL COMMENT '全场让球主单' after olympic_play2_tickets;



alter table tybss_report.ac_user_order_day
    add column olympic_play17_tickets int DEFAULT NULL COMMENT '半场独赢主单' after olympic_play4_tickets;

alter table tybss_report.ac_user_order_day
    add column olympic_play18_tickets int DEFAULT NULL COMMENT '半场大小主单' after olympic_play17_tickets;

alter table tybss_report.ac_user_order_day
    add column olympic_play19_tickets int DEFAULT NULL COMMENT '半场让球主单' after olympic_play18_tickets;

alter table tybss_report.ac_user_order_day
    add column olympic_corner_tickets int DEFAULT NULL COMMENT '角球主单' after olympic_play19_tickets;

alter table tybss_report.ac_user_order_day
    add column olympic_punish_tickets int DEFAULT NULL COMMENT '罚牌主单' after olympic_corner_tickets;

alter table tybss_report.ac_user_order_day
    add column olympic_win_tickets int DEFAULT NULL COMMENT '盈利主单' after olympic_punish_tickets;



ALTER TABLE tybss_report.ac_user_order_day ADD INDEX index_user_id (`uid`);
ALTER TABLE tybss_report.ac_user_order_day ADD INDEX index_time_user_id (`time`,`uid`);
ALTER TABLE tybss_report.ac_user_order_hour ADD INDEX index_user_id (`uid`);
ALTER TABLE tybss_report.ac_user_order_hour ADD INDEX index_time_id (`time`);