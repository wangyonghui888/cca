create table tybss_report.r_merchant_match_bet_info_bak like tybss_report.r_merchant_match_bet_info;

insert into tybss_report.r_merchant_match_bet_info_bak select * from tybss_report.r_merchant_match_bet_info;