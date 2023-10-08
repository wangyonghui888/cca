update tybss_new.t_user_level set color='#B83636' where level_id=2;
update tybss_new.t_user_level set bg_color ='#AB9052' where level_id in (2,3,4,5);
update tybss_new.t_user_level set bg_color ='#B3363C' where level_id in (6,7,8);
update tybss_new.t_user_level set bg_color ='#368B37' where level_id =9;
update tybss_new.t_user_level set bg_color ='#E05179' where level_id =10;
update tybss_new.t_user_level set bg_color ='#788299' where level_id =11;
update tybss_new.t_user_level set bg_color ='#C16830' where level_id =12;
update tybss_new.t_user_level set bg_color ='#3E7CC0' where level_id =13;
update tybss_new.t_user_level set bg_color ='#633D8D' where level_id =14;


drop event if EXISTS tybss_report.e_r_merchant_finance_day_yesterday;
drop event if EXISTS e_r_merchant_finance_before_yesterday;

drop event if EXISTS tybss_report.e_r_merchant_finance_month;
drop event if EXISTS tybss_report.e_r_merchant_finance_month_yesterday;
drop event if EXISTS tybss_report.e_r_merchant_finance_month_before_yesterday;


CREATE EVENT tybss_report.e_r_merchant_finance_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:20:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));

CREATE EVENT tybss_report.e_r_merchant_finance_day_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:20:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

CREATE EVENT tybss_report.e_r_merchant_finance_month ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:30:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));

CREATE EVENT tybss_report.e_r_merchant_finance_month_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:31:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
