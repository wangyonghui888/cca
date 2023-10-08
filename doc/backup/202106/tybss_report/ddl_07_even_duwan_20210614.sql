drop event if EXISTS tybss_report.e_p_r_merchant_match_bet_info_group;
CREATE EVENT tybss_report.e_p_r_merchant_match_bet_info_group ON SCHEDULE EVERY 1 hour STARTS CURRENT_DATE ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_match_bet_info_group(CURRENT_DATE);
