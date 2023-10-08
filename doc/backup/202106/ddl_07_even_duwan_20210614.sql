drop event if EXISTS e_p_r_merchant_match_bet_info_group;
CREATE EVENT e_p_r_merchant_match_bet_info_group ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:47:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_info_group(CURRENT_DATE);
