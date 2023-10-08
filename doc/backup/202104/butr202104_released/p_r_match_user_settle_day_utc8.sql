DROP PROCEDURE IF EXISTS p_r_match_user_settle_day_utc8;
DELIMITER //
CREATE PROCEDURE p_r_match_user_settle_day_utc8(in execute_time varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 107;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_match_user_settle_day_utc8成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);


    DECLARE yearL BIGINT(10);
    DECLARE monthL BIGINT(16);

    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);


    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_time, " p_r_match_user_settle_day_utc8错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;


	set yearL = DATE_FORMAT(execute_time, '%Y');
    set monthL = DATE_FORMAT(execute_time, '%Y%m');

	set executetimeL = DATE_FORMAT(execute_time, '%Y%m%d');
	    set startTimeUTCL = unix_timestamp(execute_time) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_time, INTERVAL 1 DAY)) * 1000 - 1;


     REPLACE INTO tybss_report.r_match_user_settle_day_utc8
	  SELECT
		  concat(od.match_id,od.uid)                           id,
		   od.match_id  match_id,
			od.uid     uid,
			u.fake_name    username,
			u.user_level    user_level,
			sum( s.bet_amount)/100 settle_bet_amount,
			sum( s.profit_amount )/100 profit,
			IFNULL(CONVERT(sum( s.profit_amount ) /
			sum(s.bet_amount ), DECIMAL(32, 4)),0)                                	profitRate,
			count(DISTINCT od.order_no) settle_order_count,
			min(m.begin_time)  beging_time,
			min(m.sport_id) 								sport_id,
			min(m.tournament_id)                                                       tournament_id,
			executetimeL time,
			yearL year,
			monthL mouth,
			UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))              	 								updated_time
		FROM tybss_new.t_order_detail od
             LEFT JOIN tybss_new.t_order o ON o.order_no = od.order_no
			 LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no
             LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
			 LEFT JOIN tybss_new.t_user u ON u.uid = od.uid
            WHERE o.series_type = 1
             AND o.order_status =1
            and o.manager_code<3
			AND s.last_settle = 1 and s.bet_amount > 0
		  and m.begin_time >=startTimeUTCL and m.begin_time <=endTimeUTCL
		GROUP BY
			od.uid,
			od.match_id;

 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_match_user_settle_day_utc8,success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

	REPLACE INTO tybss_report.r_tournament_user_settle_day_utc8
	  SELECT
		  concat(m.tournament_id,m.uid,executetimeL)                           id,
		  	m.tournament_id                                                      tournament_id,
			m.uid     uid,
			min(m.username)    username,
			min(m.user_level)    user_level,
			sum( m.settle_bet_amount) settle_bet_amount,
			sum( m.profit ) profit,
			IFNULL(CONVERT(sum( m.profit ) /
			sum(m.settle_bet_amount ), DECIMAL(32, 4)),0)                                	profitRate,
			sum(m.settle_order_count) settle_order_count,
			min(m.sport_id) 								sport_id,
			time time,
			yearL year,
			monthL mouth,
			UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))              	 								updated_time
		FROM r_match_user_settle_day_utc8 m
		  where m.time =executetimeL
		GROUP BY
			m.uid,
			m.tournament_id;

	 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',r_tournament_user_settle_day_utc8,success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
