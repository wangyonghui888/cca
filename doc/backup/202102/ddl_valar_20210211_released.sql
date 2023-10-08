alter table tybss_report.r_match_bet_info
    add column pre_bet_amount Decimal(16, 4) DEFAULT NULL COMMENT '早盘投注额' after un_settle_amount;

alter table tybss_report.r_match_bet_info
    add column pre_valid_tickets int DEFAULT NULL COMMENT '早盘有效注单数' after pre_bet_amount;

alter table tybss_report.r_match_bet_info
    add column pre_bet_users int DEFAULT NULL COMMENT '早盘投注用户数' after pre_valid_tickets;

alter table tybss_report.r_match_bet_info
    add column pre_profit Decimal(16, 4) DEFAULT NULL COMMENT '早盘盈利' after pre_bet_users;

alter table tybss_report.r_match_bet_info
    add column pre_un_settle_order int DEFAULT NULL COMMENT '早盘未结算' after pre_profit;

alter table tybss_report.r_match_bet_info
    add column pre_un_settle_amount Decimal(16, 4) DEFAULT NULL COMMENT '早盘未结算' after pre_un_settle_order;



alter table tybss_report.r_match_bet_info
    add column live_bet_amount Decimal(16, 4) DEFAULT NULL COMMENT '滚球投注额' after pre_un_settle_amount;

alter table tybss_report.r_match_bet_info
    add column live_valid_tickets int DEFAULT NULL COMMENT '滚球有效注单数' after live_bet_amount;

alter table tybss_report.r_match_bet_info
    add column live_bet_users int DEFAULT NULL COMMENT '滚球投注用户数' after live_valid_tickets;

alter table tybss_report.r_match_bet_info
    add column live_profit Decimal(16, 4) DEFAULT NULL COMMENT '滚球盈利' after live_bet_users;

alter table tybss_report.r_match_bet_info
    add column live_un_settle_order int DEFAULT NULL COMMENT '滚球未结算' after live_profit;

alter table tybss_report.r_match_bet_info
    add column live_un_settle_amount Decimal(16, 4) DEFAULT NULL COMMENT '滚球未结算' after live_un_settle_order;



alter table tybss_report.r_match_bet_info
    add column live_pa_bet_amount Decimal(16, 4) DEFAULT NULL COMMENT '滚球PA投注额' after live_un_settle_amount;

alter table tybss_report.r_match_bet_info
    add column live_pa_valid_tickets int DEFAULT NULL COMMENT '滚球PA有效注单数' after live_pa_bet_amount;

alter table tybss_report.r_match_bet_info
    add column live_pa_bet_users int DEFAULT NULL COMMENT '滚球PA投注用户数' after live_pa_valid_tickets;

alter table tybss_report.r_match_bet_info
    add column live_pa_profit Decimal(16, 4) DEFAULT NULL COMMENT '滚球PA盈利' after live_pa_bet_users;
alter table tybss_report.r_match_bet_info
    add column live_pa_un_settle_order int DEFAULT NULL COMMENT '滚球未结算' after live_pa_profit;

alter table tybss_report.r_match_bet_info
    add column live_pa_un_settle_amount Decimal(16, 4) DEFAULT NULL COMMENT '滚球未结算' after live_pa_un_settle_order;



alter table tybss_report.r_match_bet_info
    add column pre_pa_bet_amount Decimal(16, 4) DEFAULT NULL COMMENT '早盘PA投注额' after live_pa_un_settle_amount;

alter table tybss_report.r_match_bet_info
    add column pre_pa_valid_tickets int DEFAULT NULL COMMENT '早盘PA有效注单数' after pre_pa_bet_amount;

alter table tybss_report.r_match_bet_info
    add column pre_pa_bet_users int DEFAULT NULL COMMENT '早盘PA投注用户数' after pre_pa_valid_tickets;

alter table tybss_report.r_match_bet_info
    add column pre_pa_profit Decimal(16, 4) DEFAULT NULL COMMENT '早盘PA盈利' after pre_pa_bet_users;

alter table tybss_report.r_match_bet_info
    add column pre_pa_un_settle_order int DEFAULT NULL COMMENT '滚球未结算' after pre_pa_profit;

alter table tybss_report.r_match_bet_info
    add column pre_pa_un_settle_amount Decimal(16, 4) DEFAULT NULL COMMENT '滚球未结算' after pre_pa_un_settle_order;



alter table tybss_report.r_match_bet_info
    add column pre_mts_bet_users int DEFAULT NULL COMMENT '早盘MTS投注用户数' after pre_pa_un_settle_amount;

alter table tybss_report.r_match_bet_info
    add column live_mts_bet_users int DEFAULT NULL COMMENT '滚球MTS投注用户数' after pre_mts_bet_users;

UPDATE tybss_new.t_merchant tu inner join (select o.id,
                                                  o.currency
                                           from t_merchant o
                                           where o.agent_level = 1) uo on tu.parent_id = uo.id
set tu.currency=uo.currency;