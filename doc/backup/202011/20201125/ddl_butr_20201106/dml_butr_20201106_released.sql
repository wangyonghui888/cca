alter table tybss_report.r_match_bet_info add column un_settle_order bigint(50);
alter table tybss_report.r_match_bet_info add column  un_settle_amount  decimal(32,2);
alter table tybss_report.r_merchant_match_bet_info add column un_settle_order  bigint(50);
alter table tybss_report.r_merchant_match_bet_info add column un_settle_amount  decimal(32,2);


alter table tybss_report.r_market_bet_info add column un_settle_order bigint(50);
alter table tybss_report.r_market_bet_info add column un_settle_amount  decimal(32,2);
alter table tybss_report.r_merchant_market_bet_info add column un_settle_order bigint(50);
alter table tybss_report.r_merchant_market_bet_info add column un_settle_amount  decimal(32,2);

alter table tybss_report.r_market_bet_info MODIFY  column settle_amount  decimal(32,2);
alter table tybss_report.r_market_bet_info MODIFY  column bet_amount  decimal(32,2);
alter table tybss_report.r_market_bet_info MODIFY  column order_amount_total  decimal(32,2);
alter table tybss_report.r_merchant_market_bet_info MODIFY  column settle_amount  decimal(32,2);
alter table tybss_report.r_merchant_market_bet_info MODIFY  column bet_amount  decimal(32,2);
alter table tybss_report.r_merchant_market_bet_info MODIFY  column order_amount_total  decimal(32,2);
alter table tybss_report.r_merchant_match_bet_info MODIFY  column valid_bet_amount  decimal(32,2);
alter table tybss_report.r_match_bet_info MODIFY  column valid_bet_amount  decimal(32,2);

CALL  p_r_merchant_loop_exc('2020-06-01','2020-07-01','p_r_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-06-01','2020-07-01','p_r_merchant_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-06-01','2020-07-01','p_r_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-06-01','2020-07-01','p_r_merchant_market_bet_info','1');

CALL  p_r_merchant_loop_exc('2020-07-01','2020-08-01','p_r_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-07-01','2020-08-01','p_r_merchant_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-07-01','2020-08-01','p_r_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-07-01','2020-08-01','p_r_merchant_market_bet_info','1');

CALL  p_r_merchant_loop_exc('2020-08-01','2020-09-01','p_r_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-08-01','2020-09-01','p_r_merchant_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-08-01','2020-09-01','p_r_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-08-01','2020-09-01','p_r_merchant_market_bet_info','1');

CALL  p_r_merchant_loop_exc('2020-09-01','2020-10-01','p_r_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-09-01','2020-10-01','p_r_merchant_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-09-01','2020-10-01','p_r_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-09-01','2020-10-01','p_r_merchant_market_bet_info','1');

CALL  p_r_merchant_loop_exc('2020-10-01','2020-11-01','p_r_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-10-01','2020-11-01','p_r_merchant_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-10-01','2020-11-01','p_r_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-10-01','2020-11-01','p_r_merchant_market_bet_info','1');

CALL  p_r_merchant_loop_exc('2020-11-01','2020-11-20','p_r_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-11-01','2020-11-20','p_r_merchant_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-11-01','2020-11-20','p_r_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-11-01','2020-11-20','p_r_merchant_market_bet_info','1');