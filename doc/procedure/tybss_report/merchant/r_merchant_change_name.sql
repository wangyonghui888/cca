DROP PROCEDURE IF EXISTS p_r_merchant_change_name;

DELIMITER //
CREATE PROCEDURE `p_r_merchant_change_name`(in merchantCode varchar(100),in  merchantName varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 113;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_merchant_change_name成功';
    DECLARE msg TEXT;
    DECLARE total INT(10) DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, merchantCode,merchantName, ' p_r_merchant_change_name错误信息：', msg);
        SET result = 2;
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END;


update r_merchant_finance_day set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_finance_day_temp set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_finance_day_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_finance_month set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_finance_month_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_match_bet_currency_info set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_match_bet_info set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_match_bet_info_group set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_order_day set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_order_day_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_order_month set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_order_month_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_sport_order_day set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_sport_order_day_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_sport_order_month set merchant_name = merchantName where merchant_code = merchantCode;
update r_merchant_sport_order_month_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_order_day set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_order_day_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_order_hour set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_order_month set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_order_month_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_sport_order_day set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_sport_order_day_utc8	 set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_sport_order_month	set merchant_name = merchantName where merchant_code = merchantCode;
update r_user_sport_order_month_utc8 set merchant_name = merchantName where merchant_code = merchantCode;
update t_ticket set merchant_name = merchantName where merchant_code = merchantCode;
update user_order_all set merchant_name = merchantName where merchant_code = merchantCode;

SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(merchantCode,merchantName, ',p_r_merchant_change_name:(2), success!');
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

