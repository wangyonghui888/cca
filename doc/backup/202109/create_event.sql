drop event if EXISTS e_r_merchant_finance_today;
drop event if EXISTS e_r_merchant_finance_day_3days_ago;
CREATE EVENT e_r_merchant_finance_today ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:20:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_day(CURRENT_DATE);

CREATE EVENT e_r_merchant_finance_day_3days_ago ON SCHEDULE EVERY 12 hour STARTS '2020-06-18 12:20:01' ON COMPLETION PRESERVE ENABLE DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE, INTERVAL -4 DAY));
