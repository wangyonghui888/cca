
ALTER TABLE merchant.`user_order_all` CHANGE `user_name` `user_name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT NULL;


ALTER TABLE tybss_report.r_merchant_order_day
    ADD settle_users int(16) null COMMENT '总结算玩家数';
ALTER TABLE tybss_report.r_merchant_order_day
    ADD live_users int(16) null COMMENT '总比赛玩家数';

ALTER TABLE tybss_report.r_merchant_order_month
    ADD settle_users int(16) null COMMENT '总结算玩家数';
ALTER TABLE tybss_report.r_merchant_order_month
    ADD live_users int(16) null COMMENT '总比赛玩家数';

ALTER TABLE tybss_report.r_merchant_order_week
    ADD settle_users int(16) null COMMENT '总结算玩家数';
ALTER TABLE tybss_report.r_merchant_order_week
    ADD live_users int(16) null COMMENT '总比赛玩家数';

ALTER TABLE tybss_report.r_merchant_order_year
    ADD settle_users int(16) null COMMENT '总结算玩家数';
ALTER TABLE tybss_report.r_merchant_order_year
    ADD live_users int(16) null COMMENT '总比赛玩家数';

ALTER TABLE tybss_report.r_merchant_sport_order_day
    ADD settle_users int(16) null COMMENT '总结算玩家数';
ALTER TABLE tybss_report.r_merchant_sport_order_day
    ADD live_users int(16) null COMMENT '总比赛玩家数';

ALTER TABLE tybss_report.r_merchant_sport_order_month
    ADD settle_users int(16) null COMMENT '总结算玩家数';
ALTER TABLE tybss_report.r_merchant_sport_order_month
    ADD live_users int(16) null COMMENT '总比赛玩家数';

ALTER TABLE tybss_report.r_merchant_sport_order_week
    ADD settle_users int(16) null COMMENT '总结算玩家数';
ALTER TABLE tybss_report.r_merchant_sport_order_week
    ADD live_users int(16) null COMMENT '总比赛玩家数';

ALTER TABLE tybss_report.r_merchant_sport_order_year
    ADD settle_users int(16) null COMMENT '总结算玩家数';
ALTER TABLE tybss_report.r_merchant_sport_order_year
    ADD live_users int(16) null COMMENT '总比赛玩家数';


ALTER TABLE tybss_new.`t_merchant` CHANGE `white_ip` `white_ip` VARCHAR(2000) NULL COMMENT 'IP 白名单';