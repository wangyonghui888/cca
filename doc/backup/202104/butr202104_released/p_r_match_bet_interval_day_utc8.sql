DROP PROCEDURE IF EXISTS p_r_match_bet_interval_day_utc8;
DELIMITER //
CREATE PROCEDURE p_r_match_bet_interval_day_utc8(in execute_time varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 106;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_match_bet_interval_day_utc8成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);

    DECLARE yearL BIGINT(10);
    DECLARE monthL BIGINT(16);

    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_time, " p_r_match_bet_interval_day_utc8错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set yearL = DATE_FORMAT(execute_time, '%Y');
    set monthL = DATE_FORMAT(execute_time, '%Y%m');
	set executetimeL = DATE_FORMAT(execute_time, '%Y%m%d');

    set startTimeUTCL = unix_timestamp(execute_time) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_time, INTERVAL 1 DAY)) * 1000 - 1;

     REPLACE INTO tybss_report.r_match_bet_interval_day_utc8
	   SELECT  concat(s.match_id,s.bet)                           		id,
			s.match_id,
			count(DISTINCT s.uid )               user_count,
			s.bet    bet,
			sum( s.settle_bet_amount )		settle_bet_amount,
			sum( s.settle_amount )  settle_amount,
			sum( s.profit )      profit,
			 IFNULL(CONVERT(sum( profit ) /
            sum(settle_bet_amount ), DECIMAL(32, 4)),0)                                	profitRate,
			count(DISTINCT s.order_no )  order_count,
			min( s.beging_time ) beging_time,
			s.tournament_id tournament_id,
			s.sport_id  sport_id,
			executetimeL																		time,
			yearL																				year,
			monthL																				mouth,
		UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))              	 								updated_time
		FROM
			(
					SELECT
						od.match_id match_id,
						IFNULL(  s.bet_amount  / 100, 0 ) settle_bet_amount,
						floor(  s.bet_amount  / 100 / 1000 ) bet,
						IFNULL( s.settle_amount  / 100, 0 ) settle_amount,
						IFNULL( s.profit_amount  / 100 * - 1, 0 ) profit,
						od.uid, od.order_no  order_no,
						m.begin_time beging_time ,
						m.sport_id sport_id,
						m.tournament_id tournament_id
					FROM
						tybss_new.t_order_detail od
						LEFT JOIN tybss_new.t_order o ON o.order_no = od.order_no
						LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no
						AND s.last_settle = 1
						AND s.bet_amount > 0
						LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
					WHERE
						o.series_type = 1
						AND o.order_status = 1
						and o.manager_code < 3
					AND  m.begin_time >=startTimeUTCL and m.begin_time <=endTimeUTCL
				) s
		GROUP BY s.match_id, s.bet;

	 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_match_bet_interval_day_utc8,success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
