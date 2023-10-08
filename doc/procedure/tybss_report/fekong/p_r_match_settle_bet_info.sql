DROP PROCEDURE IF EXISTS p_r_match_settle_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_match_settle_bet_info(in execute_time varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 103;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_match_settle_bet_info成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);

    DECLARE yearL BIGINT(10);
    DECLARE monthL BIGINT(16);

    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16); 
	
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_time, " p_r_match_settle_bet_info错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

	set yearL = DATE_FORMAT(execute_time, '%Y');
    set monthL = DATE_FORMAT(execute_time, '%Y%m');

	set executetimeL = DATE_FORMAT(execute_time, '%Y%m%d');

    set startTimeUTCL = unix_timestamp(execute_time) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_time, INTERVAL 1 DAY)) * 1000 - 1;

  REPLACE INTO tybss_report.r_match_settle_user_count
    select od.match_id,count(DISTINCT od.uid) as user_count
         from tybss_new.t_order_detail od left join tybss_new.t_order o on o.order_no=od.order_no
          LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
        where o.series_type=1 and o.order_status =1
            and m.begin_time >=startTimeUTCL and m.begin_time <=endTimeUTCL
        group by od.match_id;

    delete from tybss_report.r_match_settle_bet_info where time =executetimeL;


     REPLACE INTO tybss_report.r_match_settle_bet_info
	  SELECT  concat(m.match_id)                           		id,
            m.match_id																				match_id,      
           min(m.match_info)                                                                 		match_info,
            max(m.match_status)                                                                        		match_status,				       
           IFNULL(sum(m.settle_bet_amount ) , 0)         												settle_bet_amount,
           IFNULL(sum( m.settle_amount ), 0)      												settle_amount,
           IFNULL(sum(m.profit ) , 0)      												profit,
           IFNULL(CONVERT(sum( m.profit ) /
            sum(m.settle_bet_amount ), DECIMAL(32, 4)),0)                                	profit_rate,
            uc.settle_user_count                                                            settle_user_count,
           IFNULL(sum(m.settle_order_count), 0)     														settle_order_count,
           min(m.beging_time)                                                                         beging_time,
           min(m.tournament_level)                                                              	tournament_level,
		 ifnull(sum(case m.match_type when 1 then m.settle_bet_amount end),0)       pre_settle_bet_amount,
		 ifnull(sum(case m.match_type when 1 then m.settle_amount end),0)       		pre_settle_amount,
		 ifnull(sum(case m.match_type when 1 then m.profit end),0)       						pre_settle_profit,
		 ifnull(sum(case m.match_type when 1 then m.settle_order_count end),0)       			pre_order_count,
		 ifnull(sum(case m.match_type when 2 then m.settle_bet_amount end),0)       live_settle_bet_amount,
		 ifnull(sum(case m.match_type when 2 then m.settle_amount end),0)       		live_settle_amount,
		 ifnull(sum(case m.match_type when 2 then m.profit end),0)       						live_settle_profit,
		 ifnull(sum(case m.match_type when 2 then m.settle_order_count end),0)       				live_order_count,
           min(m.sport_id) 																					sport_id,
           min(m.pre_trader_id) 																		pre_trader_id,
           min(m.live_trader_id) 																		live_trader_id,
           min(m.pre_risk_manager_code)																pre_risk_manager_code,
           min(m.live_risk_manager_code)																live_risk_manager_code,
           min(m.tournament_id) 																		tournament_id,
           executetimeL																					time,
             yearL                                                                                year,
		   monthL																					 mouth,
		UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))              	 								updated_time
    from r_match_market_settle_day_utc8 m  left join r_match_settle_user_count uc on uc.match_id = m.match_id
    where m.beging_time >=startTimeUTCL and m.beging_time <=endTimeUTCL
		group by m.match_id;
	
	 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_match_settle_bet_info,success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);



     REPLACE INTO tybss_report.r_tournament_settle_bet_info
	  SELECT  concat(m.tournament_id, executetimeL)                           		id,
            m.tournament_id																				tournament_id,
           IFNULL(sum(m.settle_bet_amount ) , 0)         												settle_bet_amount,
           IFNULL(sum( m.settle_amount ), 0)      												settle_amount,
           IFNULL(sum(m.profit ) , 0)      												profit,
           IFNULL(CONVERT(sum( m.profit ) /
            sum(m.settle_bet_amount ), DECIMAL(32, 4)),0)                                	profit_rate,
            sum(m.settle_user_count)                                                        settle_user_count,
           IFNULL(sum(m.settle_order_count), 0)     														settle_order_count,
		 ifnull(sum(pre_settle_bet_amount),0)       pre_settle_bet_amount,
		 ifnull(sum(pre_settle_amount),0)       		pre_settle_amount,
		 ifnull(sum(pre_settle_profit),0)       						pre_settle_profit,
		 ifnull(sum(pre_order_count),0)       			pre_order_count,
		 ifnull(sum(live_settle_bet_amount),0)       live_settle_bet_amount,
		 ifnull(sum(live_settle_amount),0)       		live_settle_amount,
		 ifnull(sum(live_settle_profit),0)       						live_settle_profit,
		 ifnull(sum(live_order_count),0)       				live_order_count,
           min(m.sport_id) 																					sport_id,
           executetimeL																					time,
             yearL                                                                                year,
		   monthL																					 mouth,
		UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))              	 								updated_time
    from r_match_settle_bet_info m
    where m.time =executetimeL
		group by m.tournament_id;

	 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',r_tournament_settle_bet_info,success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
