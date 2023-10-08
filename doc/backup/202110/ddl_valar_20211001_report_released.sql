drop event if EXISTS e_r_user_order_3days_ago;
CREATE EVENT e_r_user_order_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:20:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));


drop event if EXISTS e_r_user_order_month_3days_ago;
CREATE EVENT e_r_user_order_month_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 13:21:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));


drop event if EXISTS e_r_merchant_order_3days_ago;
CREATE EVENT e_r_merchant_order_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 15:35:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));


drop event if EXISTS e_r_merchant_order_month_3days_ago;
CREATE EVENT e_r_merchant_order_month_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 16:41:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));


drop event if EXISTS e_userHourDataCleanTruncate;
CREATE EVENT e_userHourDataCleanTruncate ON SCHEDULE EVERY 7 day STARTS '2021-02-08 23:59:30' ON COMPLETION PRESERVE ENABLE DO CALL p_user_hour_clean();



CREATE UNIQUE INDEX index_match_id ON tybss_report.r_match_bet_info (match_id);
CREATE INDEX index_begin_time ON tybss_report.r_match_bet_info (begin_time);

update tybss_report.user_order_all
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_finance_day_utc8
set parent_id=null ,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_finance_day
set parent_id=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');


update tybss_report.r_merchant_finance_month
set parent_id=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_finance_month_utc8
set parent_id=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');


update tybss_report.r_merchant_order_day
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_order_day_utc8
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_order_day_utc8_sub
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_order_month
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_order_month_utc8
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_order_month_utc8_sub
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_sport_order_day
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');


update tybss_report.r_merchant_sport_order_day_utc8
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');


update tybss_report.r_merchant_sport_order_day_utc8_sub
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_sport_order_month
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_sport_order_month_utc8
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_merchant_sport_order_month_utc8_sub
set parent_code=null,agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_order_day_utc8
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_order_day
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');


/*update tybss_report.r_user_order_day_utc8_sub
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');
*/
CALL my_tools.sp_batch_dml('u', 'tybss_report', 'r_user_order_day_utc8_sub', null, null, null, 'parent_code=null',
                           'time', 'merchant_code in ("526016", "442254", "976523", "812774")', null, 1, 10, 0.01,
                           null);

update tybss_report.r_user_order_hour
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');


update tybss_report.r_user_order_month
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_order_month_utc8
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_order_month_utc8_sub
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_sport_order_day
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_sport_order_day_utc8
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

/*update tybss_report.r_user_sport_order_day_utc8_sub
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');
*/
CALL my_tools.sp_batch_dml('u', 'tybss_report', 'r_user_sport_order_day_utc8_sub', null, null, null, 'parent_code=null',
                           'time', 'merchant_code in ("526016", "442254", "976523", "812774")', null, 1, 10, 0.01,
                           null);

update tybss_report.r_user_sport_order_hour
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_sport_order_hour_sub
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_sport_order_month
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_sport_order_month_utc8
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');

update tybss_report.r_user_sport_order_month_utc8_sub
set parent_code=null
where merchant_code in ('526016', '442254', '976523', '812774');
