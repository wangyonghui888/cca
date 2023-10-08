DROP event IF EXISTS e_p_r_match_bet_currency_info;
CREATE EVENT e_p_r_match_bet_currency_info ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 00:54:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_currency_info(CURRENT_DATE);

DROP event IF EXISTS e_p_r_match_bet_currencyinfo_yesterday;
CREATE EVENT e_p_r_match_bet_currencyinfo_yesterday ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:55:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_currency_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));

DROP event IF EXISTS e_p_r_match_bet_currency_info_before_yesterday;
CREATE EVENT e_p_r_match_bet_currency_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:56:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_match_bet_currency_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));

DROP event IF EXISTS e_p_r_merchant_match_bet_currency_info;
CREATE EVENT e_p_r_merchant_match_bet_currency_info ON SCHEDULE EVERY 1800 SECOND STARTS '2020-06-18 00:57:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_currency_info(CURRENT_DATE);

DROP event IF EXISTS e_p_r_merchant_match_bet_currencyinfo_yesterday;
CREATE EVENT e_p_r_merchant_match_bet_currencyinfo_yesterday ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:58:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_currency_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));

DROP event IF EXISTS e_p_r_merchant_match_bet_currency_info_before_yesterday;
CREATE EVENT e_p_r_merchant_match_bet_currency_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 12:59:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_currency_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
