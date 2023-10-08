/*存储过程*/

DROP PROCEDURE IF EXISTS p_r_bc_match_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_bc_match_bet_info(in execute_date varchar(100))
BEGIN

    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 5;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE executetimeL BIGINT(10);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_bc_match_bet_info成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_bc_match_bet_info错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set executetimeL = DATE_FORMAT(execute_date, '%Y%m%d');

/*sql开始*/
    DELETE FROM tybss_report.r_bc_match_bet_info WHERE time = executetimeL;
    REPLACE INTO tybss_report.r_bc_match_bet_info
    SELECT s.id,
           s.match_zs,
           s.match_en,
           s.sport_id,
           s.begin_time,
           s.bet_amount,
           s.profit_amount,
           s.pre_profit,
           s.rolling_profit,
           s.profit_rate,
           executetimeL                         AS time,
           UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) AS update_time
    FROM (SELECT g.id,
                 min(g.match_zs)                                                          match_zs,
                 min(g.match_en)                                                          match_en,
                 min(g.sport_id)                                                          sport_id,
                 min(g.begin_time)                                                        begin_time,
                 sum(g.bet_amount)                                                        bet_amount,
                 sum(g.profit_amount)                                                     profit_amount,
                 sum(g.pre_profit)                                                        pre_profit,
                 sum(g.rolling_profit)                                                    rolling_profit,
                 (sum(g.pre_profit) + sum(g.rolling_profit)) / sum(g.bet_amount) * 100 AS profit_rate
          FROM (SELECT s.id,
                       s.sport_id,
                       CONCAT(l.zs, ' vs ', l1.zs)              match_zs,
                       CONCAT(l.en, ' vs ', l1.en)              match_en,
                       s.begin_time,
                       sum(o.order_amount_total / 100 * r.rate) bet_amount,
                       sum(case when t.profit_amount > 0 then t.profit_amount + o.order_amount_total else 0 end) *
                       rate / 100                               profit_amount,
                       sum(CASE d.match_type
                               WHEN 1 THEN t.profit_amount * - 1 * r.rate / 100
                               ELSE 0 END) AS                   pre_profit,
                       sum(CASE d.match_type
                               WHEN 2 THEN t.profit_amount * - 1 * r.rate / 100
                               ELSE 0 END) AS                   rolling_profit
                FROM tybss_merchant_common.t_order_detail d
                         LEFT JOIN tybss_merchant_common.t_order o ON o.order_no = d.order_no
                         LEFT JOIN tybss_merchant_common.bs_order_rate r
                                   ON r.START <= o.create_time AND r.END >= o.create_time
                         LEFT JOIN tybss_merchant_common.t_settle t ON t.order_no = d.order_no AND t.last_settle = 1
                         LEFT JOIN tybss_merchant_common.s_match_info s ON s.id = d.match_id
                         LEFT JOIN tybss_merchant_common.s_language l ON l.name_code = s.home_name_code
                         LEFT JOIN tybss_merchant_common.s_language l1 ON l1.name_code = s.away_name_code
                WHERE o.series_type = 1
                  and d.odds_data_sourse = 'BC'
                  AND o.order_status = 1
                  and d.create_time > 1590940800000
                  AND s.begin_time >= UNIX_TIMESTAMP(executetimeL) * 1000
                  AND s.begin_time <= unix_timestamp(date_add(executetimeL, INTERVAL 1 DAY)) * 1000 - 1
                GROUP BY s.id, r.rate) g
          GROUP BY g.id
         ) s;
    /*sql结束*/

/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_bc_match_bet_info success!", execute_date);

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;