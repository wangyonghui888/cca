
DROP PROCEDURE IF EXISTS p_r_match_market_settle_day_utc8;
DELIMITER //
CREATE PROCEDURE p_r_match_market_settle_day_utc8(in execute_time varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 102;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_match_market_settle_hour_utc8成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);

    DECLARE yearL BIGINT(10);
    DECLARE dayL BIGINT(16);
    DECLARE monthL BIGINT(16);


    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);  
	
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_time, " p_r_match_market_settle_day_utc8错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
	
	set executetimeL = DATE_FORMAT(execute_time, '%Y%m%d');
	


    set yearL = DATE_FORMAT(execute_time, '%Y');
    set dayL = DATE_FORMAT(execute_time, '%Y%m%d');
    set monthL = DATE_FORMAT(execute_time, '%Y%m');

    set startTimeUTCL = unix_timestamp(execute_time) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_time, INTERVAL 1 DAY)) * 1000 - 1;

    delete from tybss_report.r_match_market_settle_day_utc8 where time =executetimeL;

    REPLACE INTO tybss_report.r_match_market_settle_day_utc8
    SELECT concat(od.market_id,od.match_type,executetimeL)                           id,
           od.match_id																			match_id,
           od.play_id																			play_id,
           od.market_id																			market_id,
           min(od.match_info)                                                                   match_info,
           max(CASE
                WHEN m.match_over =1 THEN 4
                WHEN m.match_period_id =999 THEN 4
                ELSE m.match_status
            END )                                                                        		match_status,	
           min(od.play_name)                                                                    play_name,
           min(od.market_value)                                                                 market_value,
           min(od.market_type)                                                                  market_type,
		   od.match_type																		match_type,
           IFNULL(sum(s.bet_amount ) / 100, 0)         											settle_bet_amount,
           IFNULL(sum( s.settle_amount ) / 100, 0)      										settle_amount,
           IFNULL(sum( s.profit_amount ) / 100* -1, 0)      										profit,
           IFNULL(CONVERT(sum( s.profit_amount )* -1 /
                          sum(s.bet_amount ), DECIMAL(32, 4)),0)                                profitRate,
           IFNULL(count(DISTINCT s.uid ), 0)          											settle_user_count,
           IFNULL(count(DISTINCT  s.order_no ), 0)     											settle_order_count,
           m.begin_time                                                                         beging_time,
           m.tournament_level                                                               	tournament_level,
           m.sport_id																			sport_id,
           m.pre_trader_id																		pre_trader_id,
           m.live_trader_id																		live_trader_id,
            ifnull(m.pre_risk_manager_code,'')													pre_risk_manager_code,
            ifnull(m.live_risk_manager_code,'')													live_risk_manager_code,               
           m.tournament_id																		tournament_id,
		   executetimeL																			 time,
		   yearL                                                                                year,
		   monthL																					 mouth,
		   UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))              	 								updated_time
    FROM tybss_new.t_order_detail od
            LEFT JOIN tybss_new.t_order o ON o.order_no = od.order_no
			 LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no  AND s.last_settle = 1 and s.bet_amount > 0
             LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
    WHERE o.series_type = 1
     AND o.order_status =1
    and o.manager_code < 3
      and s.create_time >= startTimeUTCL  and s.create_time <= endTimeUTCL
    GROUP BY od.match_id, od.play_id, od.market_id,od.match_type;
	
	 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',r_match_market_settle_day_utc8,success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
