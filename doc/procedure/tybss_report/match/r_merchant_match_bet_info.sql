DROP PROCEDURE IF EXISTS `p_r_merchant_match_bet_info`;

DELIMITER //
CREATE PROCEDURE `p_r_merchant_match_bet_info`(in execute_date varchar(100))
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
CALL tybss_report.p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END;

    REPLACE INTO tybss_report.r_merchant_match_bet_info
SELECT CONCAT(d.match_id, d.merchant_code)                              id,
       d.match_id,
      concat(a.homeName, ' v ', a.awayName)                                         matchInfo,
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
       a .tournament_level                                                           tournamentLevel,
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
       0                                                                                 parlay_profit_rate,
       d.s_match_type                                                                    match_type,


       IFNULL(SUM(
                      case WHEN d.series_type > 1 then 0
                           WHEN s.out_come in (3,4,5,6) and ABS(d.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                           WHEN s.out_come in (3,4,5,6) and ABS(d.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                           WHEN s.out_come in (3,4,5,6) and ABS(d.order_amount_total) < ABS(s.profit_amount) THEN ABS(d.order_amount_total/100)
                          END),0)                                                         as settle_valid_bet_money,
    IFNULL(SUM(case WHEN s.out_come in (3,4,5,6) and d.series_type = 1 and d.order_status in (0, 1) THEN 1  END),0)          as settle_valid_order_count,
       IFNULL(SUM(
                      case WHEN d.series_type > 1 then 0
                           WHEN s.out_come in (3,4,5,6) and ABS(d.original_order_amount_total) = ABS(s.original_settle_amount) THEN ABS(s.original_settle_amount/100)
                           WHEN s.out_come in (3,4,5,6) and ABS(d.original_order_amount_total) > ABS(s.original_settle_amount) THEN ABS(s.original_settle_amount/100)
                           WHEN s.out_come in (3,4,5,6) and ABS(d.original_order_amount_total) < ABS(s.original_settle_amount) THEN ABS(s.original_settle_amount/100)
                          END),0)                                                         as original_order_valid_bet_acount,
       IFNULL(SUM(CASE WHEN d.series_type = 1 then d.original_order_amount_total else 0 end) / 100, 0)    original_order_amount_total,
       IFNULL(SUM(CASE WHEN d.series_type = 1 then s.original_profit_amount else 0 end) / 100, 0)          as original_profit,
       IFNULL(SUM(case when d.series_type = 1 then s.original_settle_amount else 0 end) / 100, 0)      as original_settle_amount,
       IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.original_order_amount_total / 100 END), 0) as original_un_settle_amount
FROM tybss_report.view_r_order_match_detail d
         LEFT JOIN tybss_report.view_r_settle_full_detail s ON d.order_no = s.order_no
        left join  (select id,
                 sport_id,
                 begin_time,
                 match_status,
                 (select zs from tybss_merchant_common.s_language where name_code = home_name_code)       homeName,
                 (select zs from tybss_merchant_common.s_language where name_code = away_name_code)       awayName,
                 tournament_id,
                 tournament_level,
                 (select zs from tybss_merchant_common.s_language where name_code = tournament_name_code) tourNamentName
          from tybss_merchant_common.s_match_info mi
          where mi.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
            AND mi.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
           ) a  on a.id = d.match_id
WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
  AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
  AND d.order_status in (0, 1)
GROUP BY d.match_id, d.merchant_code;
SET exce_msg = CONCAT(execute_date, " p_r_merchant_match_bet_info success!");
CALL tybss_report.p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

/*sql结束*/
/*执行成功，添加日志*/
SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_match_bet_info2 success!");

CALL tybss_report.p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
