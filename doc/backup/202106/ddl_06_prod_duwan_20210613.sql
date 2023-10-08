/*存储过程*/
DROP PROCEDURE IF EXISTS p_r_merchant_match_bet_info_group;
DELIMITER //
CREATE PROCEDURE p_r_merchant_match_bet_info_group(in execute_date varchar(100))
BEGIN

    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 4;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'r_merchant_match_bet_info_group 成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_merchant_match_bet_info_group 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    REPLACE INTO tybss_report.r_merchant_match_bet_info_group
    SELECT CONCAT(d.match_id, d.merchant_code,d.order_currency_code)                              id,
           d.match_id,
           d.order_currency_code,
		   (CASE
                WHEN d.sport_id = 1001 THEN 2
                WHEN d.sport_id = 1002 THEN 2
				WHEN d.sport_id = 1004 THEN 2
                WHEN d.sport_id = 1007 THEN 2
				WHEN d.sport_id = 1008 THEN 2
                WHEN d.sport_id = 1009 THEN 2
				WHEN d.sport_id = 1010 THEN 2
                WHEN d.sport_id = 1011 THEN 2
				WHEN d.sport_id = 1012 THEN 2
                ELSE 1
               END)                                                                          match_type,
           MIN(d.match_info)                                                                 match_info,
           MIN(d.sport_id)                                                                   sport_id,
           MIN(d.sport_name)                                                                 sport_name,
           MIN(d.begin_time)                                                                 begin_time,
           (CASE
                WHEN d.match_over = 1 THEN 4
                WHEN d.match_period_id = 999 THEN 4
                ELSE d.match_status
               END)                                                                          match_status,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then d.bet_amount else 0 end) / 100, 0)    bet_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.bet_amount END) / 100,
                  0)                                                                         valid_bet_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then s.settle_amount end) / 100, 0)        settle_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then s.profit_amount end) / 100, 0)        profit,
           MIN(d.tournament_id)                                                              tournament_id,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.uid END))
                                                                                             user_amount,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.order_no END))
                                                                                             order_amount,
           IFNULL(CONVERT(sum(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN s.profit_amount END) /
                          sum(
                                  CASE WHEN d.series_type = 1 and d.order_status = 1 THEN d.bet_amount END), DECIMAL(32, 4)),
                  0)                                                                         profitRate,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.play_id END))
                                                                                             play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                           elasticsearch_id,
           MIN(d.tournament_name)                                                            tournamentName,
           d.merchant_code,
           d.merchant_name,
           MIN(d.tournament_level)                                                           tournamentLevel,
           IFNULL(COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.order_no END)), 0)
                                                                                             un_settle_order,
           IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.bet_amount / 100 END), 0)
                                                                                             un_settle_amount,
           d.agent_level                                                                     agent_level,
           IFNULL(SUM(CASE WHEN d.series_type > 1 and d.order_status in (0, 1) THEN d.order_amount_total END) / 100,
                  0)                                                                         parlay_vaild_bet_amount,
           COUNT(DISTINCT (CASE
                               WHEN d.series_type > 1 and d.order_status in (0, 1)
                                   THEN d.order_no END))                                     parlay_valid_tickets,
           IFNULL(SUM(case when d.series_type > 1 then s.profit_amount else 0 end) / 100, 0) parlay_profit,
           0                                                                                 parlay_profit_rate
    FROM view_r_order_match_detail_group d
             LEFT JOIN view_r_settle_full_detail_group s ON d.order_no = s.order_no
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.order_status in (0, 1)
    GROUP BY d.match_id, d.merchant_code, d.order_currency_code;
    SET exce_msg = CONCAT(execute_date, " p_r_merchant_match_bet_info success!");
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);


    REPLACE INTO tybss_report.r_merchant_match_bet_info_group
    SELECT CONCAT(d.match_id, mn.merchant_code,d.order_currency_code)                                        id,
           d.match_id,
           d.order_currency_code                  order_currency_code,
		   (CASE
                WHEN d.sport_id = 1001 THEN 2
                WHEN d.sport_id = 1002 THEN 2
				WHEN d.sport_id = 1004 THEN 2
                WHEN d.sport_id = 1007 THEN 2
				WHEN d.sport_id = 1008 THEN 2
                WHEN d.sport_id = 1009 THEN 2
				WHEN d.sport_id = 1010 THEN 2
                WHEN d.sport_id = 1011 THEN 2
				WHEN d.sport_id = 1012 THEN 2
                ELSE 1
               END)                                                                                       match_type,
           MIN(d.match_info)                                                                              match_info,
           MIN(d.sport_id)                                                                                sport_id,
           MIN(d.sport_name)                                                                              sport_name,
           MIN(d.begin_time)                                                                              begin_time,
           CASE
               WHEN d.match_over = 1 THEN 4
               WHEN d.match_period_id = 999 THEN 4
               ELSE d.match_status
               END                                                                                        match_status,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then d.bet_amount else 0 end) / 100, 0)
                                                                                                          bet_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.bet_amount END) / 100, 0)
                                                                                                          valid_bet_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then s.settle_amount end) / 100, 0)
                                                                                                          settle_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then s.profit_amount end) / 100, 0)
                                                                                                          profit,
           MIN(d.tournament_id)                                                                           tournament_id,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.uid END))      user_amount,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.order_no END)) order_amount,
           IFNULL(CONVERT(sum(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN s.profit_amount END) /
                          sum(
                                  CASE WHEN d.series_type = 1 and d.order_status = 1 THEN d.bet_amount END), DECIMAL(32, 4)),
                  0)                                                                                      profitRate,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.play_id END))  play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                                        elasticsearch_id,
           MIN(d.tournament_name)                                                                         tournamentName,
           mn.merchant_code,
           mn.merchant_name,
           MIN(d.tournament_level)                                                                        tournamentLevel,
           IFNULL(COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.order_no END)),
                  0)                                                                                      un_settle_order,
           IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.bet_amount / 100 END),
                  0)                                                                                      un_settle_amount,
           mn.agent_level                                                                                 agent_level,
           IFNULL(SUM(CASE WHEN d.series_type > 1 and d.order_status in (0, 1) THEN d.order_amount_total END) / 100,
                  0)                                                                                      parlay_vaild_bet_amount,
           COUNT(DISTINCT
                 (CASE
                      WHEN d.series_type > 1 and d.order_status in (0, 1)
                          THEN d.order_no END))                                                           parlay_valid_tickets,
           IFNULL(SUM(case when d.series_type > 1 then s.profit_amount else 0 end) / 100, 0)              parlay_profit,
           0                                                                                              parlay_profit_rate
    FROM view_r_order_match_detail_group d
             LEFT JOIN view_r_settle_full_detail_group s ON d.order_no = s.order_no
             left join tybss_new.t_merchant mn on d.parent_id = mn.id
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      and mn.merchant_code is not null
    GROUP BY d.match_id, mn.merchant_code, d.order_currency_code;

    /*sql结束*/
/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " r_merchant_match_bet_info_group success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_match_bet_info(@execute_date);*/