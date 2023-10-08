DROP PROCEDURE IF EXISTS p_r_user_order_day_big;
DELIMITER //
CREATE PROCEDURE p_r_user_order_day_big(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 112;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_user_order_day_big成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);
    DECLARE yearL BIGINT(10);

    DECLARE endTimeL BIGINT(16);
    DECLARE startTimeL BIGINT(16);
    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);
    DECLARE total INT(10) DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, execute_date, ' p_r_user_order_day_big错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    set executetimeL = DATE_FORMAT(execute_date, '%Y%m%d');
    set yearL = DATE_FORMAT(execute_date, '%Y');
    set startTimeL = unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000;
    set endTimeL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1);

    set startTimeUTCL = unix_timestamp(execute_date) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1;


    delete from tybss_report.r_user_order_day_utc8 where time = executetimeL;
    REPLACE INTO tybss_report.r_user_order_day_utc8
    SELECT Concat(w.uid, executetimeL)                AS id
         , w.uid
         , d.username
         , d.currency_code
         , (CASE WHEN a.rmb_exchange_rate is null THEN c.rmb_exchange_rate else a.rmb_exchange_rate END)
                                                         exchange_rate
         , d.merchant_code
         , d.merchant_name                            AS merchantName
         , executetimeL                               AS time
         , IFNULL(a.profit, 0)                           profit
         , IFNULL(a.settleAmount, 0)                     returnAmount
         , 0                                          AS profit_rate
         , IFNULL(a.betAmount, 0)                        betAmount
         , IFNULL(a.validBetAmount, 0)                   validBetAmount
         , IFNULL(a.totalTickets, 0)                     totalTickets
         , IFNULL(a.validTickets, 0)                     validTickets
         , IFNULL(a.failed_bet_amount, 0)                failed_bet_amount
         , IFNULL(a.failed_tickets, 0)                   failed_tickets
         , IFNULL(a.betAmountSettled, 0)                 betAmountSettled
         , IFNULL(a.tickedSettled, 0)                    ticketSettled
         , IFNULL(c.settleOrderAmount, 0)                settleTotalTickets
         , IFNULL(c.settleBetAmount, 0)                  settleBetAmount
         , IFNULL(c.settleProfit, 0)                  AS settleProfit
         , IFNULL(c.settleAmount, 0)                  AS settleReturn
         , 0                                          AS settleProfitRate
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))       AS updatedTime
         , yearL                                      AS 'year'
         , d.parent_code
         , d.parent_name
         , IFNULL(c.settleExpectProfit, 0)            as settleExpectProfit
         , IFNULL(c.settle_series_amount, 0)          as settle_series_amount
         , IFNULL(c.settle_series_tickets, 0)         as settle_series_tickets
         , IFNULL(c.settle_series_profit, 0)          as settle_series_profit
         , floor(IFNULL(a.order_valid_bet_money, 0))  as order_valid_bet_money
         , floor(IFNULL(c.settle_valid_bet_money, 0)) as settle_valid_bet_money
         , floor(IFNULL(c.order_valid_bet_count, 0))  as order_valid_bet_count
         , floor(IFNULL(a.original_order_valid_bet_money, 0))  as original_order_valid_bet_money
         , floor(IFNULL(c.original_settle_valid_bet_money, 0)) as original_settle_valid_bet_money
         , floor(IFNULL(c.original_settle_order_amount, 0))    as original_settle_order_amount
         , floor(IFNULL(c.original_settle_profit, 0))          as original_settle_profit
         , floor(IFNULL(a.original_profit, 0))                 as original_profit
         , floor(IFNULL(a.original_valid_bet_amount, 0))       as original_valid_bet_amount
    FROM (SELECT o.uid
          FROM tybss_report.t_ticket o
          WHERE o.create_time >= startTimeUTCL
            AND o.create_time <= endTimeUTCL
          UNION
          SELECT s.uid
          FROM tybss_report.t_ticket s
          WHERE s.settle_time >= startTimeUTCL
            AND s.settle_time <= endTimeUTCL) w
             LEFT JOIN (SELECT o.uid
                             , COUNT(o.order_no)                                                     AS totalTickets
                             , SUM(o.order_amount_total)                                             AS betAmount
                             , 1                                                                     AS rmb_exchange_rate
                             , SUM(o.settle_amount)                                                  AS settleAmount
                             , SUM(o.profit_amount)                                                  AS profit
                             , SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) AS validBetAmount
                             , SUM(CASE WHEN o.order_status = 1 THEN o.order_amount_total END)       AS betAmountSettled
                             , Count(CASE WHEN o.order_status = 1 THEN o.order_no END)               AS tickedSettled
                             , Count(CASE WHEN o.order_status in (0, 1) THEN o.order_no END)         AS validTickets
                             , SUM(CASE WHEN o.order_status = 4 THEN o.order_amount_total END)       AS failed_bet_amount
                             , Count(CASE WHEN o.order_status = 4 THEN o.order_no END)               AS failed_tickets
                            , SUM(CASE
                            WHEN o.out_come in (3, 4, 5, 6) and
                            ABS(o.original_order_amount_total) = ABS(o.original_profit_amount)
                            THEN ABS(o.original_profit_amount / 100)
                            WHEN o.out_come in (3, 4, 5, 6) and
                            ABS(o.original_order_amount_total) > ABS(o.original_profit_amount)
                            THEN ABS(o.original_profit_amount / 100)
                            WHEN o.out_come in (3, 4, 5, 6) and
                            ABS(o.original_order_amount_total) < ABS(o.original_profit_amount)
                            THEN ABS(o.original_order_amount_total / 100) END)                 as original_order_valid_bet_money
                                , SUM(o.original_profit_amount)                             AS original_profit
                             , SUM(CASE
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(o.profit_amount)
                                           THEN ABS(o.profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(o.profit_amount)
                                           THEN ABS(o.profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(o.profit_amount)
                                           THEN ABS(o.order_amount_total / 100) END)                 as order_valid_bet_money
                               , SUM(o.original_order_amount_total)                                 AS original_valid_bet_amount
                        FROM tybss_report.t_ticket o
                        WHERE o.create_time >= startTimeUTCL
                          AND o.create_time <= endTimeUTCL
                        GROUP BY o.uid) a ON w.uid = a.uid
             LEFT JOIN (SELECT s.uid
                             , COUNT(s.order_no)                                               AS settleOrderAmount
                             , SUM(s.settle_amount)                                            AS settleAmount
                             , SUM(s.profit_amount)                                            AS settleProfit
                             , SUM(s.order_amount_total)                                       AS settleBetAmount
                             , 0                                                               as settleExpectProfit
                             , 1                                                               AS rmb_exchange_rate
                             , SUM(CASE WHEN s.series_type <> 1 THEN s.order_amount_total END) as settle_series_amount
                             , COUNT(CASE WHEN s.series_type <> 1 THEN s.order_no END)         as settle_series_tickets
                             , SUM(CASE WHEN s.series_type <> 1 THEN s.profit_amount END)      as settle_series_profit
                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.order_amount_total) = ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.order_amount_total) > ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(s.order_amount_total / 100) END)           as settle_valid_bet_money
                             , Count(CASE WHEN s.out_come in (3, 4, 5, 6) THEN s.order_no END) AS order_valid_bet_count
                            , SUM(CASE
                            WHEN s.out_come in (3, 4, 5, 6) and
                            ABS(s.original_order_amount_total) = ABS(s.original_profit_amount)
                            THEN ABS(s.original_profit_amount / 100)
                            WHEN s.out_come in (3, 4, 5, 6) and
                            ABS(s.original_order_amount_total) > ABS(s.original_profit_amount)
                            THEN ABS(s.original_profit_amount / 100)
                            WHEN s.out_come in (3, 4, 5, 6) and
                            ABS(s.original_order_amount_total) < ABS(s.original_profit_amount)
                            THEN ABS(s.original_order_amount_total / 100) END)                 as original_settle_valid_bet_money
                            ,SUM(s.original_order_amount_total)                                  AS original_settle_order_amount
                            ,SUM(s.original_profit_amount)                               AS original_settle_profit
                        FROM tybss_report.t_ticket s
                        WHERE s.settle_time >= startTimeUTCL
                          AND s.settle_time <= endTimeUTCL
                        GROUP BY s.uid) c ON w.uid = c.uid
             LEFT JOIN (SELECT u.merchant_code
                             , m.merchant_name
                             , m.`level`        AS merchantLevel
                             , u.create_time
                             , u.username
                             , m1.merchant_code as parent_code
                             , m1.merchant_name as parent_name
                             , u.uid
                             , u.currency_code
                        FROM tybss_merchant_common.t_user u
                                 LEFT JOIN tybss_merchant_common.t_merchant m ON u.merchant_code = m.merchant_code
                                 left join tybss_merchant_common.t_merchant m1 on m1.id = m.parent_id) d
                       ON w.uid = d.uid;
    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_date, ',p_r_user_order_day_big:(2),success!');
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;