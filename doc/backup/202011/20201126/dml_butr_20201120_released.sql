
alter table tybss_report.r_merchant_match_bet_info add column agent_level  tinyint(2);
alter table tybss_report.r_merchant_market_bet_info add column agent_level  tinyint(2);
alter table tybss_report.r_merchant_market_bet_info add column sport_id  int(5)	;



CALL  p_r_merchant_loop_exc('2020-11-01','2020-11-25','p_r_merchant_match_bet_info','1');

CALL  p_r_merchant_loop_exc('2020-11-01','2020-11-25','p_r_merchant_market_bet_info','1');