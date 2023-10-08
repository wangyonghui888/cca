/*drop event if EXISTS e_r_match_market_settle_hour;
drop event if EXISTS e_r_match_market_settle_last_hour;
CREATE EVENT e_r_match_market_settle_hour ON SCHEDULE EVERY 5 minute STARTS '2020-06-18 00:00:05' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_market_settle_hour_utc8(now());
CREATE EVENT e_r_match_market_settle_last_hour ON SCHEDULE EVERY 40 minute STARTS '2020-06-18 00:00:08' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_market_settle_hour_utc8(ADDDATE(now(), INTERVAL -1 hour));
*/
drop event if EXISTS e_r_match_market_settle_day;
drop event if EXISTS e_r_match_market_settle_day_yesterday;
drop event if EXISTS e_r_match_market_settle_before_yesterday;
drop event if EXISTS e_r_match_market_settle_3days_ago;

CREATE EVENT e_r_match_market_settle_day ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:01:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_market_settle_day_utc8(CURRENT_DATE);
CREATE EVENT e_r_match_market_settle_day_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:06:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_market_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_match_market_settle_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:16:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_market_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_r_match_market_settle_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:21:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_market_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));



drop event if EXISTS e_r_match_settle_bet_info;
drop event if EXISTS e_r_match_settle_bet_info_yesterday;
drop event if EXISTS e_r_match_settle_bet_info_before_yesterday;
drop event if EXISTS e_r_match_settle_bet_info_3days_ago;

CREATE EVENT e_r_match_settle_bet_info ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:02:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_settle_bet_info(CURRENT_DATE);
CREATE EVENT e_r_match_settle_bet_info_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:07:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_settle_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_match_settle_bet_info_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:17:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_settle_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_r_match_settle_bet_info_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:22:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_settle_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));


drop event if EXISTS e_r_match_play_settle_day;
drop event if EXISTS e_r_match_play_settle_day_yesterday;
drop event if EXISTS e_r_match_play_settle_day_before_yesterday;
drop event if EXISTS e_r_match_play_settle_day_3days_ago;

CREATE EVENT e_r_match_play_settle_day ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:03:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_play_settle_day_utc8(CURRENT_DATE);
CREATE EVENT e_r_match_play_settle_day_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:08:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_play_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_match_play_settle_day_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:18:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_play_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_r_match_play_settle_day_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:23:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_play_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));


drop event if EXISTS e_r_match_bet_interval_day;
drop event if EXISTS e_r_match_bet_interval_day_yesterday;
drop event if EXISTS e_r_match_bet_interval_day_before_yesterday;
drop event if EXISTS e_r_match_bet_interval_day_3days_ago;

CREATE EVENT e_r_match_bet_interval_day ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:04:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_interval_day_utc8(CURRENT_DATE);
CREATE EVENT e_r_match_bet_interval_day_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:09:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_interval_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_match_bet_interval_day_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:19:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_interval_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_r_match_bet_interval_day_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:24:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_interval_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));


drop event if EXISTS e_r_match_user_settle_bet_info;
drop event if EXISTS e_r_match_user_settle_bet_info_yesterday;
drop event if EXISTS e_r_match_user_settle_bet_info_before_yesterday;
drop event if EXISTS e_r_match_user_settle_bet_info_3days_ago;

CREATE EVENT e_r_match_user_settle_bet_info ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:05:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_user_settle_day_utc8(CURRENT_DATE);
CREATE EVENT e_r_match_user_settle_bet_info_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:10:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_user_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_r_match_user_settle_bet_info_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:20:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_user_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_r_match_user_settle_bet_info_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:25:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_user_settle_day_utc8(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));

drop event if EXISTS e_p_r_sport_duty_trader_day;
CREATE EVENT e_p_r_sport_duty_trader_day ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 23:59:58' ON COMPLETION PRESERVE ENABLE DO CALL p_r_sport_duty_trader_day();



drop event if EXISTS e_p_r_rist_duty_trader_day;
drop event if EXISTS e_p_r_rist_duty_trader_day_yesterday;
drop event if EXISTS e_p_r_rist_duty_trader_day_before_yesterday;
drop event if EXISTS e_p_r_rist_duty_trader_day_3days_ago;

CREATE EVENT e_p_r_rist_duty_trader_day ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:06:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_rist_duty_trader_day(CURRENT_DATE);
CREATE EVENT e_p_r_rist_duty_trader_day_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:11:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_rist_duty_trader_day(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_p_r_rist_duty_trader_day_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:21:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_rist_duty_trader_day(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_p_r_rist_duty_trader_day_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:26:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_rist_duty_trader_day(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));


drop event if EXISTS e_p_r_device_type_day;
drop event if EXISTS e_p_r_device_type_day_yesterday;
drop event if EXISTS e_p_r_device_type_day_before_yesterday;
drop event if EXISTS e_p_r_device_type_day_3days_ago;

CREATE EVENT e_p_r_device_type_day ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:06:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_device_type_day(CURRENT_DATE);
CREATE EVENT e_p_r_device_type_day_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:11:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_device_type_day(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_p_r_device_type_day_before_yesterday ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:21:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_device_type_day(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CREATE EVENT e_p_r_device_type_day_3days_ago ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:26:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_device_type_day(DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY));
