DROP PROCEDURE IF EXISTS p_r_sport_duty_trader_day;
DELIMITER //
CREATE PROCEDURE p_r_sport_duty_trader_day()
BEGIN
    DECLARE task_type INT(2) DEFAULT 108;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_sport_duty_trader_day成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);

      DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, CURRENT_DATE(), " p_r_sport_duty_trader_day错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
       END;

	 set executetimeL = DATE_FORMAT(CURRENT_DATE(), '%Y%m%d');
    delete from tybss_report.r_sport_duty_trader_day where time =executetimeL;
     REPLACE INTO tybss_report.r_sport_duty_trader_day
	   SELECT  concat(s.id,executetimeL)                           		id,
			s.id trader_id,
			s.shift,
			s.sport_id,
			s.user_code,
			s.user_id,
			s.create_time,
			s.update_time,
			executetimeL
		FROM
			s_sport_duty_trader s;

	 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(CURRENT_DATE(), ',p_r_sport_duty_trader_day,success!', executetimeL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
