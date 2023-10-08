/*存储过程*/

DROP PROCEDURE IF EXISTS p_r_merchant_market_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_merchant_market_bet_info(in execute_date varchar(100))
BEGIN

    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 3;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_merchant_market_bet_info 成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, "p_r_merchant_market_bet_info 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    REPLACE INTO tybss_report.r_merchant_market_bet_info
    SELECT concat(od.match_id, od.play_id, od.market_id, n.merchant_code)                                   id,
           od.match_id,
           od.play_id,
           od.market_id,
           min(od.match_info)                                                                               match_info,
           min(od.play_name)                                                                                play_name,
           min(od.market_value)                                                                             market_value,
           min(od.market_type)                                                                              market_type,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN od.bet_amount END) / 100, 0)        bet_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) / 100, 0) order_amount_total,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.bet_amount END) / 100, 0)         valid_bet_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.settle_amount END) / 100, 0)      settle_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.profit_amount END) / 100, 0)      profit,
           IFNULL(CONVERT(sum(CASE WHEN o.order_status in (0, 1) THEN s.profit_amount END)/
                          sum(CASE WHEN o.order_status = 1 THEN o.order_amount_total END) , DECIMAL(32, 4)),
                  0)                                                                              profitRate,
           IFNULL(count(DISTINCT CASE WHEN o.order_status in (0, 1) THEN od.uid END), 0)          user_amount,
           IFNULL(count(DISTINCT CASE WHEN o.order_status in (0, 1) THEN od.order_no END), 0)     order_amount,
           UNIX_TIMESTAMP(current_timestamp(3))                                                             elasticsearch_id,
           n.merchant_code,
           n.merchant_name,
           m.begin_time                                                                                     beging_time,
           min(od.tournament_level)                                                                         tournament_level,
           m.match_status                                                                                   match_status,
            IFNULL(COUNT(DISTINCT (CASE WHEN o.order_status in (0) THEN o.order_no END)) , 0)               un_settle_order,
             ifnull(SUM(CASE WHEN  o.order_status in (0) THEN od.bet_amount/100 END) ,0)           un_settle_amount
    FROM tybss_new.t_order_detail od
             LEFT JOIN tybss_new.t_order o ON od.order_no = o.order_no
             LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no
             LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
             left join tybss_new.t_user u on u.uid = o.uid
             left join tybss_new.t_merchant n on n.merchant_code = u.merchant_code
    WHERE o.series_type = 1
      AND o.order_status in (0, 1)
      AND m.begin_time >= (unix_timestamp(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND m.begin_time <= (unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
    GROUP BY od.match_id, od.play_id, od.market_id, n.merchant_code;



    REPLACE INTO tybss_report.r_merchant_market_bet_info
    SELECT concat(od.match_id, od.play_id, od.market_id, mn.merchant_code)                        id,
           od.match_id,
           od.play_id,
           od.market_id,
           min(od.match_info)                                                                     match_info,
           min(od.play_name)                                                                      play_name,
           min(od.market_value)                                                                   market_value,
           min(od.market_type)                                                                    market_type,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN od.bet_amount END) / 100, 0)        bet_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) / 100, 0) order_amount_total,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.bet_amount END) / 100, 0)         valid_bet_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.settle_amount END) / 100, 0)      settle_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.profit_amount END) / 100, 0)      profit,
           IFNULL(CONVERT(sum(CASE WHEN o.order_status in (0, 1) THEN s.profit_amount END)/
                          sum(CASE WHEN o.order_status = 1 THEN o.order_amount_total END) , DECIMAL(32, 4)),
                  0)                                                                              profitRate,
           IFNULL(count(DISTINCT CASE WHEN o.order_status in (0, 1) THEN od.uid END), 0)          user_amount,
           IFNULL(count(DISTINCT CASE WHEN o.order_status in (0, 1) THEN od.order_no END), 0)     order_amount,
           UNIX_TIMESTAMP(current_timestamp(3))                                                   elasticsearch_id,
           mn.merchant_code,
           mn.merchant_name,
           m.begin_time                                                                           beging_time,
           min(od.tournament_level)                                                               tournament_level,
           m.match_status                                                                         match_status,
            IFNULL(COUNT(DISTINCT (CASE WHEN o.order_status in (0) THEN o.order_no END)) , 0)               un_settle_order,
             ifnull(SUM(CASE WHEN  o.order_status in (0) THEN od.bet_amount/100 END) ,0)           un_settle_amount
    FROM tybss_new.t_order_detail od
             LEFT JOIN tybss_new.t_order o ON od.order_no = o.order_no
             LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no
             LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
             left join tybss_new.t_user u on u.uid = o.uid
             left join tybss_new.t_merchant n on n.merchant_code = u.merchant_code
             left join tybss_new.t_merchant mn on n.parent_id = mn.id
    WHERE o.series_type = 1
      AND o.order_status in (0, 1)
      AND m.begin_time >= (unix_timestamp(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND m.begin_time <= (unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      and mn.merchant_code is not null
    GROUP BY od.match_id, od.play_id, od.market_id, mn.merchant_code;


/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_merchant_market_bet_info success!");


    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_market_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_market_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_market_bet_info(@execute_date);*/