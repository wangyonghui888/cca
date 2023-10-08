/*如果提示没有开始事件，先执行：set GLOBAL event_scheduler = 1;*/
drop event if EXISTS e_p_r_market_bet_info;
drop event if EXISTS e_p_r_merchant_market_bet_info;
drop event if EXISTS e_p_r_merchant_market_bet_info_yesterday;
drop event if EXISTS e_p_r_merchant_market_bet_info_before_yesterday;
drop event if EXISTS e_p_r_merchant_match_bet_info;
drop event if EXISTS e_p_r_merchant_match_bet_info_yesterday;
drop event if EXISTS e_p_r_merchant_match_bet_info_before_yesterday;
drop event if EXISTS e_p_r_match_bet_info_tomorrow;
drop event if EXISTS e_p_r_market_bet_info_yesterday;
drop event if EXISTS e_p_r_market_bet_info_before_yesterday;

drop event if EXISTS e_p_r_market_bet_info_tomorrow;

drop event if EXISTS e_p_r_match_bet_info;
drop event if EXISTS e_p_r_match_bet_info_yesterday;
drop event if EXISTS e_p_r_match_bet_info_before_yesterday;

CREATE EVENT e_p_r_merchant_market_bet_info ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:45:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_market_bet_info(CURRENT_DATE);

CREATE EVENT e_p_r_merchant_market_bet_info_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:50:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_market_bet_info(
        DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_p_r_merchant_market_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_market_bet_info(
        DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

CREATE EVENT e_p_r_merchant_match_bet_info ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:47:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_info(CURRENT_DATE);
CREATE EVENT e_p_r_merchant_match_bet_info_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 13:51:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
#CREATE EVENT e_p_r_merchant_match_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:53:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));


CREATE EVENT e_p_r_market_bet_info_yesterday ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:48:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_market_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_p_r_market_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:48:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_market_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

CREATE EVENT e_p_r_market_bet_info ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 00:49:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_market_bet_info(CURRENT_DATE);

#CREATE EVENT e_p_r_match_bet_info ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 00:51:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_info(CURRENT_DATE);
CREATE EVENT e_p_r_match_bet_info_yesterday ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_p_r_match_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:53:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

DROP event IF EXISTS e_p_r_match_bet_currency_info;


DROP event IF EXISTS e_p_r_match_bet_currencyinfo_yesterday;

CREATE EVENT e_p_r_match_bet_currency_info ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 00:54:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_currency_info(CURRENT_DATE);
CREATE EVENT e_p_r_match_bet_currencyinfo_yesterday ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:55:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_currency_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_p_r_match_bet_currency_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:56:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_currency_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

DROP event IF EXISTS e_p_r_merchant_match_bet_currency_info;
DROP event IF EXISTS e_p_r_merchant_match_bet_currencyinfo_yesterday;


 CREATE EVENT e_p_r_merchant_match_bet_currency_info ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 00:57:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_currency_info(CURRENT_DATE);
CREATE EVENT e_p_r_merchant_match_bet_currencyinfo_yesterday ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:58:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_currency_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_p_r_merchant_match_bet_currency_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:59:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_currency_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));


drop event if EXISTS tybss_report.e_p_r_merchant_match_bet_info_group;
#CREATE EVENT tybss_report.e_p_r_merchant_match_bet_info_group ON SCHEDULE EVERY 1 hour STARTS CURRENT_DATE ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_info_group(CURRENT_DATE);

/****************************************商户月日天统计(部分球类)****************************************/
drop event if EXISTS e_r_merchant_order_day;
drop event if EXISTS e_r_merchant_order_day_tomorrow;
drop event if EXISTS e_r_merchant_order_day_yesterday;
drop event if EXISTS e_r_merchant_order_before_yesterday;
drop event if EXISTS e_r_merchant_order_2days_ago;
drop event if EXISTS e_r_merchant_order_4days_ago;
drop event if EXISTS e_r_merchant_order_6days_ago;
drop event if EXISTS e_r_merchant_order_7days_ago;


drop event if EXISTS e_r_merchant_order_week;
drop event if EXISTS e_r_merchant_order_week_yesterday;
drop event if EXISTS e_r_merchant_order_week_before_yesterday;
drop event if EXISTS e_r_merchant_order_week_3days_ago;


drop event if EXISTS e_r_merchant_order_month_today;
drop event if EXISTS e_r_merchant_order_month_yesterday;
drop event if EXISTS e_r_merchant_order_month_before_yesterday;
drop event if EXISTS e_r_merchant_order_month_3days_ago;
drop event if EXISTS e_r_merchant_order_month_7days_ago;

drop event if EXISTS e_r_merchant_order_3days_ago;
drop event if EXISTS e_r_merchant_order_year;
drop event if EXISTS e_r_merchant_sport_order_day;
drop event if EXISTS e_r_merchant_sport_order_day_tomorrow;
drop event if EXISTS e_r_merchant_sport_order_day_yesterday;
drop event if EXISTS e_r_merchant_sport_order_3days_ago;
drop event if EXISTS e_r_merchant_sport_order_month;
drop event if EXISTS e_r_merchant_sport_order_month_yesterday;
drop event if EXISTS e_r_merchant_sport_order_month_3days_ago;
drop event if EXISTS e_r_merchant_sport_order_year;
drop event if EXISTS e_r_merchant_sport_order_week_yesterday;

#CREATE EVENT e_r_merchant_order_day ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 00:34:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_day(CURRENT_DATE);
#CREATE EVENT e_r_merchant_order_day_yesterday ON SCHEDULE EVERY 3 hour STARTS '2020-06-18 00:37:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_merchant_order_2days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 13:04:10' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_r_merchant_order_4days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 13:10:10' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -4 DAY));
CREATE EVENT e_r_merchant_order_6days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 13:25:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -6 DAY));
CREATE EVENT e_r_merchant_order_7days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 13:38:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY));


##CREATE EVENT e_r_merchant_order_week ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 12:39:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_week(CURRENT_DATE);


CREATE EVENT e_r_merchant_order_month_today ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 14:40:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_month(CURRENT_DATE);
CREATE EVENT e_r_merchant_order_month_yesterday ON SCHEDULE EVERY 3 hour STARTS '2020-06-18 14:39:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_merchant_order_month_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 14:40:00' DO CALL p_r_merchant_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_r_merchant_order_month_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 14:42:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));
CREATE EVENT e_r_merchant_order_month_7days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 14:45:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY));

CREATE EVENT e_r_merchant_sport_order_day ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 00:35:00' ON COMPLETION PRESERVE ENABLE DO
    CALL p_r_merchant_sport_order_day(CURRENT_DATE);
CREATE EVENT e_r_merchant_sport_order_day_yesterday ON SCHEDULE EVERY 3 hour STARTS '2020-06-18 12:43:00' ON COMPLETION PRESERVE ENABLE DO
    CALL p_r_merchant_sport_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));

CREATE EVENT e_r_merchant_sport_order_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:43:00' ON COMPLETION PRESERVE ENABLE DO
    CALL p_r_merchant_sport_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));

CREATE EVENT e_r_merchant_sport_order_month ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 12:44:00' ON COMPLETION PRESERVE ENABLE DO
    CALL p_r_merchant_sport_order_month(CURRENT_DATE);

CREATE EVENT e_r_merchant_sport_order_month_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:44:00' ON COMPLETION PRESERVE ENABLE DO
    CALL p_r_merchant_sport_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));

CREATE EVENT e_r_merchant_sport_order_month_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:44:00' ON COMPLETION PRESERVE ENABLE DO
    CALL p_r_merchant_sport_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));

/****************************************用户月日天统计(部分球类)****************************************/

drop event if EXISTS e_r_user_order_hour;
drop event if EXISTS e_r_user_order_last_hour;

drop event if EXISTS e_r_user_order_day;
drop event if EXISTS e_r_user_order_day_tomorrow;
drop event if EXISTS e_r_user_order_day_yesterday;
drop event if EXISTS e_r_user_order_2days_ago;
drop event if EXISTS e_r_user_order_4days_ago;
drop event if EXISTS e_r_user_order_before_yesterday;
drop event if EXISTS e_r_user_order_6days_ago;
drop event if EXISTS e_r_user_order_7days_ago;


drop event if EXISTS e_r_user_order_week_yesterday;
drop event if EXISTS e_r_user_order_week_before_yesterday;
drop event if EXISTS e_r_user_order_week_3days_ago;

drop event if EXISTS e_r_user_order_month;
drop event if EXISTS e_r_user_order_month_yesterday;
drop event if EXISTS e_r_user_order_month_before_yesterday;
drop event if EXISTS e_r_user_order_month_3days_ago;

drop event if EXISTS e_r_user_order_year;
drop event if EXISTS e_r_user_sport_order_day;
drop event if EXISTS e_r_user_sport_order_day_tomorrow;
drop event if EXISTS e_r_user_sport_order_day_yesterday;
drop event if EXISTS e_r_user_sport_order_before_yesterday;
drop event if EXISTS e_r_user_sport_order_month;
drop event if EXISTS e_r_user_sport_order_month_yesterday;
drop event if EXISTS e_r_user_sport_order_year;
drop event if EXISTS e_r_user_sport_order_week_yesterday;

CREATE EVENT e_r_user_order_hour ON SCHEDULE EVERY 6 minute STARTS '2020-06-18 00:00:05' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_hour(now());

CREATE EVENT e_r_user_order_last_hour ON SCHEDULE EVERY 60 minute STARTS '2020-06-18 00:00:38' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_hour(ADDDATE(now(), INTERVAL -1 hour));


#CREATE EVENT e_r_user_order_day ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:00:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_day(CURRENT_DATE);
CREATE EVENT e_r_user_order_day_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 00:05:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_user_order_2days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:16:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_r_user_order_4days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:20:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -4 DAY));
CREATE EVENT e_r_user_order_6days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:25:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -6 DAY));
CREATE EVENT e_r_user_order_7days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:37:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY));

#CREATE EVENT e_r_user_order_month ON SCHEDULE EVERY 60 minute STARTS '2020-06-18 12:19:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_month(CURRENT_DATE);

CREATE EVENT e_r_user_order_month_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 13:19:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_user_order_month_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 13:20:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -4 DAY));
CREATE EVENT e_r_user_order_month_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 13:31:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -6 DAY));

CREATE EVENT e_r_user_sport_order_day ON SCHEDULE EVERY 30 minute STARTS '2020-06-18 00:10:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_sport_order_day(CURRENT_DATE);
CREATE EVENT e_r_user_sport_order_day_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:23:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_sport_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_user_sport_order_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:24:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_sport_order_day(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

CREATE EVENT e_r_user_sport_order_month ON SCHEDULE EVERY 30 minute STARTS '2020-06-18 12:25:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_sport_order_month(CURRENT_DATE);
CREATE EVENT e_r_user_sport_order_month_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:25:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_user_sport_order_month(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));

/****************************************财务报表****************************************/
drop event if EXISTS e_r_merchant_finance_day_yesterday;
drop event if EXISTS e_r_merchant_finance_before_yesterday;
drop event if EXISTS e_r_merchant_finance_today;
drop event if EXISTS e_r_merchant_finance_day_3days_ago;
drop event if EXISTS e_r_merchant_finance_day_4days_ago;
drop event if EXISTS e_r_merchant_finance_day_6days_ago;
drop event if EXISTS e_r_merchant_finance_day_7days_ago;

drop event if EXISTS e_r_merchant_finance_month;
drop event if EXISTS e_r_merchant_finance_month_yesterday;
drop event if EXISTS e_r_merchant_finance_month_6days_ago;
drop event if EXISTS e_r_merchant_finance_month_7days_ago;

CREATE EVENT e_r_merchant_finance_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:36:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

CREATE EVENT e_r_merchant_finance_day_4days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:40:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE, INTERVAL -4 DAY));

CREATE EVENT e_r_merchant_finance_day_6days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:45:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE, INTERVAL -6 DAY));

CREATE EVENT e_r_merchant_finance_day_7days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:51:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY));

CREATE EVENT e_r_merchant_finance_month ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 13:30:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

CREATE EVENT e_r_merchant_finance_month_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 13:41:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE, INTERVAL -4 DAY));

CREATE EVENT e_r_merchant_finance_month_6days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 13:51:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE, INTERVAL -6 DAY));

CREATE EVENT e_r_merchant_finance_month_7days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 13:56:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY));


/****************************************财务报表****************************************/
/*p_del_before_5d_log每天凌晨1点一次定时执行*/
drop event if EXISTS e_p_del_before_5d_log;
CREATE EVENT e_p_del_before_5d_log ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 1 HOUR) DO CALL p_del_before_5d_log();

drop event if EXISTS tybss_report.e_p_user_order_all;
drop event if EXISTS tybss_report.e_p_user_order_all_yesterday;
CREATE EVENT tybss_report.e_p_user_order_all ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:35:01' ON COMPLETION PRESERVE ENABLE DO CALL tybss_report.p_user_order_all(
        DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));

CREATE EVENT tybss_report.e_p_user_order_all_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:45:01' ON COMPLETION PRESERVE ENABLE DO CALL tybss_report.p_user_order_all(
        DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));


/****************************************BC赛事报表****************************************/
drop event if EXISTS e_p_r_bc_match_bet_info;
drop event if EXISTS e_p_r_bc_match_bet_info_yesterday;
drop event if EXISTS e_p_r_bc_match_bet_info_before_yesterday;
#CREATE EVENT e_p_r_bc_match_bet_info ON SCHEDULE EVERY 2 hour STARTS '2020-06-18 00:47:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_bc_match_bet_info(CURRENT_DATE);
#CREATE EVENT e_p_r_bc_match_bet_info_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_bc_match_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
#CREATE EVENT e_p_r_bc_match_bet_info_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:53:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_bc_match_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));


/****************************************用户近7天投注，盈利统计****************************************/
drop event if EXISTS e_p_user_seven_order;
#CREATE EVENT e_p_user_seven_order ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:50:00' ON COMPLETION PRESERVE ENABLE DO CALL p_user_seven_order(CURRENT_DATE);


drop event if EXISTS e_userHourDataCleanTruncate;
CREATE EVENT e_userHourDataCleanTruncate ON SCHEDULE EVERY 7 day STARTS '2021-02-08 23:59:30' ON COMPLETION PRESERVE ENABLE DO CALL p_user_hour_clean();


drop event if EXISTS e_ac_user_order_hour;
drop event if EXISTS e_ac_user_order_hour_3days;
drop event if EXISTS e_ac_user_order_hour_1days;

CREATE EVENT e_ac_user_order_hour ON SCHEDULE EVERY 10 minute STARTS '2021-05-18 00:05:10' ON COMPLETION PRESERVE ENABLE DO CALL p_ac_user_order_hour(ADDDATE(now(), INTERVAL -1 hour));

CREATE EVENT e_ac_user_order_hour_3days ON SCHEDULE EVERY 1 hour STARTS '2021-05-18 00:00:10' ON COMPLETION PRESERVE ENABLE DO CALL p_ac_user_order_hour(ADDDATE(now(), INTERVAL -48 hour));

CREATE EVENT e_ac_user_order_hour_1days ON SCHEDULE EVERY 1 hour STARTS '2021-05-18 00:00:10' ON COMPLETION PRESERVE ENABLE DO CALL p_ac_user_order_hour(ADDDATE(now(), INTERVAL -24 hour));



drop event if EXISTS e_p_r_device_type_day;
drop event if EXISTS e_r_user_play_order_day;
drop event if EXISTS e_r_user_tournament_order_day;
drop event if EXISTS e_r_user_order_overview_day;



drop event if EXISTS e_p_r_merchant_outright_match_bet_info;
drop event if EXISTS e_p_r_merchant_outright_match_bet_info_yesterday;
drop event if EXISTS e_p_r_merchant_outright_match_bet_info_before_yesterday;

#CREATE EVENT e_p_r_merchant_outright_match_bet_info ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:45:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_outright_match_bet_info(CURRENT_DATE);

CREATE EVENT e_p_r_merchant_outright_match_bet_info_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:50:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_outright_match_bet_info(
        DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
#CREATE EVENT e_p_r_merchant_outright_match_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_outright_match_bet_info(
#   DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

drop event if EXISTS e_p_r_merchant_virtual_match_bet_info;
drop event if EXISTS e_p_r_merchant_virtual_match_bet_info_yesterday;
drop event if EXISTS e_p_r_merchant_virtual_match_bet_info_before_yesterday;

#CREATE EVENT e_p_r_merchant_virtual_match_bet_info ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:45:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_virtual_match_bet_info(CURRENT_DATE);

CREATE EVENT e_p_r_merchant_virtual_match_bet_info_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:50:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_virtual_match_bet_info(
        DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
#CREATE EVENT e_p_r_merchant_virtual_match_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_virtual_match_bet_info(
#      DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));


drop event if EXISTS e_p_r_outright_match_bet_info;
drop event if EXISTS e_p_r_outright_match_bet_info_yesterday;
drop event if EXISTS e_p_r_outright_match_bet_info_before_yesterday;

#CREATE EVENT e_p_r_outright_match_bet_info ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:45:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_outright_match_bet_info(CURRENT_DATE);

CREATE EVENT e_p_r_outright_match_bet_info_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:50:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_outright_match_bet_info(
        DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
#CREATE EVENT e_p_r_outright_match_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_outright_match_bet_info(
#       DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));


drop event if EXISTS e_p_r_virtual_match_bet_info;
drop event if EXISTS e_p_r_virtual_match_bet_info_yesterday;
drop event if EXISTS e_p_r_virtual_match_bet_info_before_yesterday;

#CREATE EVENT e_p_r_virtual_match_bet_info ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:45:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_virtual_match_bet_info(CURRENT_DATE);

CREATE EVENT e_p_r_virtual_match_bet_info_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:50:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_virtual_match_bet_info(
        DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
#CREATE EVENT e_p_r_virtual_match_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_virtual_match_bet_info(
#      DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));


-- 删除event触发时间
drop event if EXISTS e_p_r_order_clean_schedule;
drop event if EXISTS e_p_r_order_clean_schedule_35;
drop event if EXISTS e_p_r_order_clean_schedule_40;
drop event if EXISTS e_p_r_order_clean_schedule_36;
CREATE EVENT e_p_r_order_clean_schedule_36 ON SCHEDULE EVERY 8 minute STARTS '2022-03-27 00:05:30' ON COMPLETION PRESERVE ENABLE DO CALL p_r_ticket_clean_schedule(ADDDATE(now(), INTERVAL -36 day));

drop event if EXISTS e_p_r_order_clean_schedule_65;
drop event if EXISTS e_p_r_order_clean_schedule_70;
#CREATE EVENT e_p_r_order_clean_schedule_70 ON SCHEDULE EVERY 10 minute STARTS '2022-03-27 00:16:40' ON COMPLETION PRESERVE ENABLE DO CALL p_r_ticket_clean_schedule(ADDDATE(now(), INTERVAL -70 day));

drop event if EXISTS e_p_r_order_clean_schedule_52;
drop event if EXISTS e_p_r_order_clean_schedule_60;
CREATE EVENT e_p_r_order_clean_schedule_60 ON SCHEDULE EVERY 8 minute STARTS '2022-03-27 00:31:40' ON COMPLETION PRESERVE ENABLE DO CALL p_r_ticket_clean_schedule(ADDDATE(now(), INTERVAL -60 day));


drop event if EXISTS e_p_r_order_clean_schedule_95;
drop event if EXISTS e_p_r_order_clean_schedule_90;
CREATE EVENT e_p_r_order_clean_schedule_90 ON SCHEDULE EVERY 8 minute STARTS '2022-03-27 00:02:40' ON COMPLETION PRESERVE ENABLE DO CALL p_r_ticket_clean_schedule(ADDDATE(now(), INTERVAL -90 day));


drop event if EXISTS e_p_r_order_clean_schedule_120;
CREATE EVENT e_p_r_order_clean_schedule_120 ON SCHEDULE EVERY 8 minute STARTS '2022-03-27 00:02:40' ON COMPLETION PRESERVE ENABLE DO CALL p_r_ticket_clean_schedule(ADDDATE(now(), INTERVAL -120 day));



