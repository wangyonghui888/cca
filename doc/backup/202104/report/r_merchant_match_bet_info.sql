/*存储过程*/

DROP PROCEDURE IF EXISTS p_r_merchant_match_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_merchant_match_bet_info(in execute_date varchar(100))
BEGIN

    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 4;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_merchant_match_bet_info成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_merchant_match_bet_info 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    REPLACE INTO tybss_report.r_merchant_match_bet_info
    SELECT CONCAT(d.match_id, d.merchant_code)                                                      id,
           d.match_id,
           MIN(d.match_info)                                                                        match_info,
           MIN(d.sport_id)                                                                          sport_id,
           MIN(d.sport_name)                                                                        sport_name,
           MIN(d.begin_time)                                                                        begin_time,
          CASE
                WHEN d.match_over =1 THEN 4
                WHEN d.match_period_id =999 THEN 4
                ELSE d.match_status
            END                                                                              match_status,
           IFNULL(SUM(d.bet_amount) / 100, 0)                                                       bet_amount,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.bet_amount END) / 100, 0) valid_bet_amount,
           IFNULL(SUM(s.settle_amount) / 100, 0)                                                    settle_amount,
           IFNULL(SUM(s.profit_amount) / 100, 0)                                                    profit,
           MIN(d.tournament_id)                                                                     tournament_id,
          COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.uid END))            user_amount,
           COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.order_no END))       order_amount,
              IFNULL(CONVERT(sum(CASE WHEN d.order_status in (0, 1) THEN s.profit_amount END)/
                          sum(CASE WHEN d.order_status = 1 THEN d.bet_amount END) , DECIMAL(32, 4)),
                  0)                                                                              profitRate,
           COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.play_id END))        play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                                  elasticsearch_id,
           MIN(d.tournament_name)                                                                   tournamentName,
           d.merchant_code,
           d.merchant_name,
           MIN(d.tournament_level)                                                                  tournamentLevel,
             IFNULL( COUNT(DISTINCT (CASE WHEN d.order_status in (0) THEN d.order_no END)), 0)             un_settle_order,
            IFNULL( SUM(CASE WHEN  d.order_status in (0) THEN d.bet_amount/100 END), 0)                un_settle_amount,
            d.agent_level        agent_level,
            0,0,0,0
    FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.series_type = 1
      AND d.order_status in (0, 1)
    GROUP BY d.match_id, d.merchant_code;
    SET exce_msg = CONCAT(execute_date, " p_r_merchant_match_bet_info success!");
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

    UPDATE tybss_report.r_merchant_match_bet_info a
        INNER JOIN (
          SELECT  d.match_id  matchId, d.merchant_code merchantCode, IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.order_amount_total END) / 100,
                                0)                                                                 validBetAmount,
                              IFNULL(SUM(s.profit_amount) / 100, 0)                                     profit,
                                       IFNULL(CONVERT(SUM(s.profit_amount) /
                                        SUM(CASE WHEN d.order_status = 1 THEN d.order_amount_total END), DECIMAL(32, 4)), 0)
                                                                                                   profitRate,
                    COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.order_no END))  validTickets
                FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
                 WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
                    AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
                    AND d.series_type > 1
                    AND d.order_status in (0, 1)
                  GROUP BY d.match_id, d.merchant_code
        ) b ON a.match_id = b.matchId and  a.merchant_code = b.merchantCode
        SET a.parlay_vaild_bet_amount = b.validBetAmount ,a.parlay_profit=b.profit,a.parlay_profit_rate=b.profitRate,a.parlay_valid_tickets=b.validTickets;



    REPLACE INTO tybss_report.r_merchant_match_bet_info
    SELECT CONCAT(d.match_id, mn.merchant_code)                                           id,
           d.match_id,
           MIN(d.match_info)                                                              match_info,
           MIN(d.sport_id)                                                                sport_id,
           MIN(d.sport_name)                                                              sport_name,
           MIN(d.begin_time)                                                              begin_time,
           CASE
                WHEN d.match_over =1 THEN 4
                WHEN d.match_period_id =999 THEN 4
                ELSE d.match_status
            END                                                                              match_status,
           IFNULL(SUM(d.bet_amount) / 100, 0)                                             bet_amount,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.bet_amount END) / 100, 0) valid_bet_amount,
           IFNULL(SUM(s.settle_amount) / 100, 0)                                          settle_amount,
           IFNULL(SUM(s.profit_amount) / 100, 0)                                          profit,
           MIN(d.tournament_id)                                                           tournament_id,
           COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.uid END))            user_amount,
           COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.order_no END))       order_amount,
              IFNULL(CONVERT(sum(CASE WHEN d.order_status in (0, 1) THEN s.profit_amount END)/
                          sum(CASE WHEN d.order_status = 1 THEN d.bet_amount END) , DECIMAL(32, 4)),
                  0)                                                                              profitRate,
           COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.play_id END))        play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                        elasticsearch_id,
           MIN(d.tournament_name)                                                         tournamentName,
           mn.merchant_code,
           mn.merchant_name,
           MIN(d.tournament_level)                                                        tournamentLevel,
             IFNULL( COUNT(DISTINCT (CASE WHEN d.order_status in (0) THEN d.order_no END)), 0)             un_settle_order,
            IFNULL( SUM(CASE WHEN  d.order_status in (0) THEN d.bet_amount/100 END), 0)                un_settle_amount,
            mn.agent_level        agent_level,
            0,0,0,0
    FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
             left join tybss_new.t_merchant mn on d.parent_id = mn.id
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.series_type = 1
      and mn.merchant_code is not null
    GROUP BY d.match_id, mn.merchant_code;

        UPDATE tybss_report.r_merchant_match_bet_info a
        INNER JOIN (
          SELECT  d.match_id  matchId, mn.merchant_code merchantCode, IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.order_amount_total END) / 100,
                                0)                                                                 validBetAmount,
                              IFNULL(SUM(s.profit_amount) / 100, 0)                                     profit,
                                       IFNULL(CONVERT(SUM(s.profit_amount) /
                                        SUM(CASE WHEN d.order_status = 1 THEN d.order_amount_total END), DECIMAL(32, 4)), 0)
                                                                                                   profitRate,
                    COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.order_no END))  validTickets
               FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
             left join tybss_new.t_merchant mn on d.parent_id = mn.id
            WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
              AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
                    AND d.series_type > 1
                   and mn.merchant_code is not null
                   AND d.order_status in (0, 1)
                  GROUP BY d.match_id, mn.merchant_code
        ) b ON a.match_id = b.matchId and  a.merchant_code = b.merchantCode
        SET a.parlay_vaild_bet_amount = b.validBetAmount ,a.parlay_profit=b.profit,a.parlay_profit_rate=b.profitRate,a.parlay_valid_tickets=b.validTickets;



    /*sql结束*/
/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_match_bet_info2 success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_match_bet_info(@execute_date);*/