ALTER TABLE tybss_report.`ac_user_order_hour`
    DROP `eu_valid_bet_amount`,
    DROP `olympic_bet`,
    DROP `olympic_profit`,
    DROP `olympic_tickets`,
    DROP `olympic_play1_tickets`,
    DROP `olympic_play2_tickets`,
    DROP `olympic_play4_tickets`,
    DROP `olympic_play17_tickets`,
    DROP `olympic_play18_tickets`,
    DROP `olympic_play19_tickets`,
    DROP `olympic_corner_tickets`,
    DROP `olympic_punish_tickets`,
    DROP `olympic_win_tickets`;

ALTER TABLE tybss_report.`ac_user_order_day`
    DROP `eu_valid_bet_amount`,
    DROP `olympic_bet`,
    DROP `olympic_profit`,
    DROP `olympic_tickets`,
    DROP `olympic_play1_tickets`,
    DROP `olympic_play2_tickets`,
    DROP `olympic_play4_tickets`,
    DROP `olympic_play17_tickets`,
    DROP `olympic_play18_tickets`,
    DROP `olympic_play19_tickets`,
    DROP `olympic_corner_tickets`,
    DROP `olympic_punish_tickets`,
    DROP `olympic_win_tickets`;


alter table tybss_report.ac_user_order_hour
    add column valid_tickets int DEFAULT NULL COMMENT '有效注单数' after valid_bet_amount;

alter table tybss_report.ac_user_order_day
    add column valid_tickets int DEFAULT NULL COMMENT '有效注单数' after valid_bet_amount;

