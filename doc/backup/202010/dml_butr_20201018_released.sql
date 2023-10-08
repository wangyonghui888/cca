DROP PROCEDURE IF EXISTS p_r_market_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_market_bet_info(in execute_date varchar(100))
BEGIN

    DECLARE task_type INT(2) DEFAULT 1;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_market_bet_info成功';
    DECLARE msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_market_bet_info错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    REPLACE INTO tybss_report.r_market_bet_info
    SELECT concat(od.match_id, od.play_id, od.market_id)                                                    id,
           od.match_id,
           od.play_id,
           od.market_id,
           min(od.match_info)                                                                               match_info,
           min(od.play_name)                                                                                play_name,
           min(od.market_value)                                                                             market_value,
           min(od.market_type)                                                                              market_type,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN od.bet_amount END) / 100, 0)                                                              bet_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) / 100, 0)                                                       order_amount_total,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.bet_amount END) / 100, 0)                                                               valid_bet_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.settle_amount END) / 100, 0)                                                            settle_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.profit_amount END) / 100, 0)                                                            profit,
           IFNULL(CONVERT(sum(CASE WHEN o.order_status in (0, 1) THEN s.profit_amount END )/ 100 / sum(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END )/ 100, DECIMAL(32, 4)), 0) profitRate,
           IFNULL(count(DISTINCT CASE WHEN o.order_status in (0, 1) THEN od.uid END), 0)                                                                user_amount,
           IFNULL(count(DISTINCT CASE WHEN o.order_status in (0, 1) THEN od.order_no END), 0)                                                           order_amount,
           UNIX_TIMESTAMP(current_timestamp(3))                                                             elasticsearch_id,
             m.begin_time beging_time,
           min(od.tournament_level) tournament_level,
           m.match_status                                                                      match_status
    FROM tybss_new.t_order_detail od
             LEFT JOIN tybss_new.t_order o ON od.order_no = o.order_no
             LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no
             LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
    WHERE o.series_type = 1
      AND o.order_status in (0, 1)
      AND m.begin_time >= (unix_timestamp(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND m.begin_time <= (unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
    GROUP BY od.match_id, od.play_id, od.market_id;

    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_market_bet_info success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_market_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_market_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_market_bet_info(@execute_date);*/


/*存储过程*/

DROP PROCEDURE IF EXISTS p_r_match_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_match_bet_info(in execute_date varchar(100))
BEGIN

    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 2;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_match_bet_info成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_match_bet_info错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

/*sql开始*/
    REPLACE INTO tybss_report.r_match_bet_info
    SELECT d.match_id                                                                               id,
           d.match_id,
           MIN(d.match_info)                                                                        match_info,
           MIN(d.sport_id)                                                                          sport_id,
           MIN(d.sport_name)                                                                        sport_name,
           MIN(d.begin_time)                                                                        begin_time,
           MIN(d.match_status)                                                                      match_status,
          IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.bet_amount END) / 100, 0)                                                       bet_amount,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN s.bet_amount END) / 100, 0)                                                       valid_bet_amount,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN s.settle_amount END) / 100, 0)                                                    settle_amount,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN s.profit_amount END) / 100, 0)                                                    profit,
           MIN(d.tournament_id)                                                                     tournament_id,
           COUNT(DISTINCT(CASE WHEN d.order_status in (0, 1) THEN d.uid END) )                                                                    user_amount,
           COUNT(DISTINCT(CASE WHEN d.order_status in (0, 1) THEN d.order_no END))                                                               order_amount,
           IFNULL(CONVERT(SUM(CASE WHEN d.order_status in (0, 1) THEN s.profit_amount END) / 100 / SUM(CASE WHEN d.order_status in (0, 1) THEN d.bet_amount END)/ 100, DECIMAL(32, 4)), 0) profit_rate,
           COUNT(DISTINCT(CASE WHEN d.order_status in (0, 1) THEN  d.play_id END))                                                                play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                                  elasticsearch_id,
           MIN(d.tournament_name)                                                                   tournamentName,
           MIN(d.tournament_level)                                                                   tournamentLevel
    FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.series_type = 1
    GROUP BY d.match_id;
    /*sql结束*/

/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_match_bet_info success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_match_bet_info(@execute_date);*/

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
           IFNULL(sum(od.bet_amount) / 100, 0)                                                              bet_amount,
           IFNULL(sum(o.order_amount_total) / 100, 0)                                                       order_amount_total,
           IFNULL(sum(s.bet_amount) / 100, 0)                                                               valid_bet_amount,
           IFNULL(sum(s.settle_amount) / 100, 0)                                                            settle_amount,
           IFNULL(sum(s.profit_amount) / 100, 0)                                                            profit,
           IFNULL(CONVERT(sum(s.profit_amount / 100) / sum(o.order_amount_total / 100), DECIMAL(32, 4)), 0) profitRate,
           count(DISTINCT od.uid)                                                                           user_amount,
           count(DISTINCT od.order_no)                                                                      order_amount,
           UNIX_TIMESTAMP(current_timestamp(3))                                                             elasticsearch_id,
           n.merchant_code,
           n.merchant_name,
           m.begin_time beging_time,
           min(od.tournament_level) tournament_level,
           m.match_status                                                                      match_status
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
    SELECT concat(od.match_id, od.play_id, od.market_id, mn.merchant_code)                                   id,
           od.match_id,
           od.play_id,
           od.market_id,
           min(od.match_info)                                                                               match_info,
           min(od.play_name)                                                                                play_name,
           min(od.market_value)                                                                             market_value,
           min(od.market_type)                                                                              market_type,
            IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN od.bet_amount END) / 100, 0)                                                              bet_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) / 100, 0)                                                       order_amount_total,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.bet_amount END) / 100, 0)                                                               valid_bet_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.settle_amount END) / 100, 0)                                                            settle_amount,
           IFNULL(sum(CASE WHEN o.order_status in (0, 1) THEN s.profit_amount END) / 100, 0)                                                            profit,
           IFNULL(CONVERT(sum(CASE WHEN o.order_status in (0, 1) THEN s.profit_amount END )/ 100 / sum(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END )/ 100, DECIMAL(32, 4)), 0) profitRate,
           IFNULL(count(DISTINCT CASE WHEN o.order_status in (0, 1) THEN od.uid END), 0)                                                                user_amount,
           IFNULL(count(DISTINCT CASE WHEN o.order_status in (0, 1) THEN od.order_no END), 0)                                                           order_amount,
           UNIX_TIMESTAMP(current_timestamp(3))                                                             elasticsearch_id,
           mn.merchant_code,
           mn.merchant_name,
           m.begin_time beging_time,
           min(od.tournament_level) tournament_level,
           m.match_status                                                                      match_status
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
           MIN(d.match_status)                                                                      match_status,
           IFNULL(SUM(d.bet_amount) / 100, 0)                                                       bet_amount,
           IFNULL(SUM(s.bet_amount) / 100, 0)                                                       valid_bet_amount,
           IFNULL(SUM(s.settle_amount) / 100, 0)                                                    settle_amount,
           IFNULL(SUM(s.profit_amount) / 100, 0)                                                    profit,
           MIN(d.tournament_id)                                                                     tournament_id,
           COUNT(DISTINCT d.uid)                                                                    user_amount,
           COUNT(DISTINCT d.order_no)                                                               order_amount,
           IFNULL(CONVERT(SUM(s.profit_amount / 100) / SUM(d.bet_amount / 100), DECIMAL(32, 4)), 0) profit_rate,
           COUNT(DISTINCT d.play_id)                                                                play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                                  elasticsearch_id,
           MIN(d.tournament_name)                                                                   tournamentName,
           d.merchant_code,
           d.merchant_name,
           MIN(d.tournament_level)                                                                   tournamentLevel
    FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.series_type = 1
      AND d.order_status in (0, 1)
    GROUP BY d.match_id, d.merchant_code;


    REPLACE INTO tybss_report.r_merchant_match_bet_info
    SELECT CONCAT(d.match_id, mn.merchant_code)                                                      id,
           d.match_id,
           MIN(d.match_info)                                                                        match_info,
           MIN(d.sport_id)                                                                          sport_id,
           MIN(d.sport_name)                                                                        sport_name,
           MIN(d.begin_time)                                                                        begin_time,
           MIN(d.match_status)                                                                      match_status,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.bet_amount END) / 100, 0)                                                       bet_amount,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN s.bet_amount END) / 100, 0)                                                       valid_bet_amount,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN s.settle_amount END) / 100, 0)                                                    settle_amount,
           IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN s.profit_amount END) / 100, 0)                                                    profit,
           MIN(d.tournament_id)                                                                     tournament_id,
           COUNT(DISTINCT(CASE WHEN d.order_status in (0, 1) THEN d.uid END) )                                                                    user_amount,
           COUNT(DISTINCT(CASE WHEN d.order_status in (0, 1) THEN d.order_no END))                                                               order_amount,
           IFNULL(CONVERT(SUM(CASE WHEN d.order_status in (0, 1) THEN s.profit_amount END) / 100 / SUM(CASE WHEN d.order_status in (0, 1) THEN d.bet_amount END)/ 100, DECIMAL(32, 4)), 0) profit_rate,
           COUNT(DISTINCT(CASE WHEN d.order_status in (0, 1) THEN  d.play_id END))                                                                play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                                  elasticsearch_id,
           MIN(d.tournament_name)                                                                   tournamentName,
           mn.merchant_code,
          mn.merchant_name,
           MIN(d.tournament_level)                                                                   tournamentLevel
    FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
             left join tybss_new.t_merchant mn on d.parent_id = mn.id
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.series_type = 1
    GROUP BY d.match_id, mn.merchant_code;
    /*sql结束*/

/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_merchant_match_bet_info success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_match_bet_info(@execute_date);*/