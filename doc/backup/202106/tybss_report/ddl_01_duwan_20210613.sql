alter table tybss_report.r_merchant_match_bet_info
    add column match_type int(4)  DEFAULT 1 COMMENT '1 常规赛事 2电竞赛事';