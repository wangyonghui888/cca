
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

CREATE EVENT e_p_r_outright_match_bet_info_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:50:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_outright_match_bet_info(
        DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
#CREATE EVENT e_p_r_outright_match_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_outright_match_bet_info(
 #       DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));


drop event if EXISTS e_p_r_virtual_match_bet_info;
drop event if EXISTS e_p_r_virtual_match_bet_info_yesterday;
drop event if EXISTS e_p_r_virtual_match_bet_info_before_yesterday;

#CREATE EVENT e_p_r_virtual_match_bet_info ON SCHEDULE EVERY 1 hour STARTS '2020-06-18 00:45:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_virtual_match_bet_info(CURRENT_DATE);

CREATE EVENT e_p_r_virtual_match_bet_info_yesterday ON SCHEDULE EVERY 4 hour STARTS '2020-06-18 00:50:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_virtual_match_bet_info(
        DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
#CREATE EVENT e_p_r_virtual_match_bet_info_before_yesterday ON SCHEDULE EVERY 24 hour STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_virtual_match_bet_info(
  #      DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));