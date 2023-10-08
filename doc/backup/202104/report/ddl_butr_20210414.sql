alter table tybss_report.r_match_bet_info
    add column parlay_vaild_bet_amount decimal(32, 2) NULL COMMENT '串关有效投注金额';
alter table tybss_report.r_match_bet_info
    add column parlay_valid_tickets int NULL COMMENT '串关有效投注订单数';
alter table tybss_report.r_match_bet_info
    add column parlay_profit decimal(32, 2) NULL COMMENT '串关盈利金额';
alter table tybss_report.r_match_bet_info
    add column parlay_profit_rate decimal(32, 2) NULL COMMENT '串关盈利率';


alter table tybss_report.r_merchant_match_bet_info
    add column parlay_vaild_bet_amount decimal(32, 2) NULL COMMENT '串关有效投注金额';
alter table tybss_report.r_merchant_match_bet_info
    add column parlay_valid_tickets int NULL COMMENT '串关有效投注订单数';
alter table tybss_report.r_merchant_match_bet_info
    add column parlay_profit decimal(32, 2) NULL COMMENT '串关盈利金额';
alter table tybss_report.r_merchant_match_bet_info
    add column parlay_profit_rate decimal(32, 2) NULL COMMENT '串关盈利率';