alter table tybss_new.t_merchant modify column agent_level int comment '0:直营站点,1:渠道站点,2:渠道站点,10:代理商';

ALTER TABLE tybss_new.t_merchant MODIFY COLUMN `key` varchar(255) DEFAULT NULL COMMENT '商户密钥';


alter table tybss_new.t_merchant
    add column old_parent varchar(64) DEFAULT NULL     COMMENT '历史上级';

alter table tybss_report.r_match_bet_info
    MODIFY column match_status varchar(3)  DEFAULT NULL  COMMENT '比赛状态';


alter table tybss_report.r_market_bet_info
    MODIFY column match_status varchar(3)  DEFAULT NULL  COMMENT '比赛状态';


alter table tybss_report.r_merchant_match_bet_info
    MODIFY column match_status varchar(3)  DEFAULT NULL  COMMENT '比赛状态';


alter table tybss_report.r_merchant_market_bet_info
    MODIFY column match_status varchar(3)  DEFAULT NULL  COMMENT '比赛状态';