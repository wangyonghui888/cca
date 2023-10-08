DROP PROCEDURE IF EXISTS p_r_rist_duty_trader_day;
DELIMITER //
CREATE PROCEDURE p_r_rist_duty_trader_day(in execute_time varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 106;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_rist_duty_trader_day成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);

    DECLARE yearL BIGINT(10);
    DECLARE monthL BIGINT(16);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_time, " p_r_rist_duty_trader_day错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set yearL = DATE_FORMAT(execute_time, '%Y');
    set monthL = DATE_FORMAT(execute_time, '%Y%m');
	set executetimeL = DATE_FORMAT(execute_time, '%Y%m%d');


     REPLACE INTO tybss_report.r_rist_duty_trader_day
	  SELECT
		    concat(m.live_trader_id,m.time)                           		id,
				m.live_trader_id trader_id,
				min(s.shift),
				min(s.sport_id),
				min(s.user_code),
				sum( m.settle_bet_amount ) settle_bet_amount,
				 sum(profit) profit,
				sum( m.profit ) / sum( m.settle_bet_amount ) profit_rate,
				sum( m.settle_order_count ) settle_order_count,
				sum( CASE WHEN m.live_risk_manager_code = 'MTS' THEN 1 WHEN m.live_risk_manager_code = 'PA' THEN 1 ELSE 0 END ) all_count,
				sum( CASE WHEN m.live_risk_manager_code = 'MTS' THEN 1 ELSE 0 END ) mts_count,
				sum( CASE WHEN m.live_risk_manager_code = 'PA' THEN 1 ELSE 0 END ) pa_count,
				if(sum( CASE WHEN m.live_risk_manager_code = 'PA' THEN 1 ELSE 0 END )>0,sum( m.settle_bet_amount )/ sum( CASE WHEN m.live_risk_manager_code = 'PA' THEN 1 ELSE 0 END ),0)  bet_avg,
				if(sum( CASE WHEN m.live_risk_manager_code = 'PA' THEN 1 ELSE 0 END )>0,sum( m.profit )/sum( CASE WHEN m.live_risk_manager_code = 'PA' THEN 1 ELSE 0 END ),0)  profit_ave,
					m.time,
				yearL																				year,
			 monthL																				mouth,
		UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))              	 								updated_time
			FROM
				r_match_settle_bet_info m
				left join r_sport_duty_trader_day s on s.user_id = m.live_trader_id
				where s.time = m.time and m.time =executetimeL
			GROUP BY
				m.live_trader_id,m.time;


	 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_rist_duty_trader_day,success!', executetimeL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
