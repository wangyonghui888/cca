DROP PROCEDURE IF EXISTS p_r_user_order_hour;
DELIMITER //
CREATE PROCEDURE p_r_user_order_hour(in execute_time varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 39;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_user_order_hour成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);
    DECLARE yearL BIGINT(10);
    DECLARE dayL BIGINT(16);
    DECLARE monthL BIGINT(16);
    DECLARE total INT(10) DEFAULT 0;

    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, 'p_r_user_order_hour错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    set executetimeL = DATE_FORMAT(execute_time, '%Y%m%d%H');

    set yearL = DATE_FORMAT(execute_time, '%Y');
    set dayL = DATE_FORMAT(execute_time, '%Y%m%d');
    set monthL = DATE_FORMAT(execute_time, '%Y%m');

    set startTimeUTCL = unix_timestamp(DATE_FORMAT(execute_time, '%Y-%m-%d %H:00:00')) * 1000;
    set endTimeUTCL = startTimeUTCL + 1000 * 60 * 60 - 1;

    REPLACE INTO tybss_report.r_user_order_hour
    SELECT Concat(w.uid, executetimeL)          AS    id
         , w.uid
         , d.username
         , d.merchant_code
         , d.merchant_name                      AS    merchantName
         , d.parent_code
         , d.parent_name
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) AS    updatedTime
         , executetimeL                         AS    time
         , IFNULL(a.profit, 0)                        profit
         , IFNULL(a.settleAmount, 0)                  returnAmount
         , 0                                    AS    profit_rate
         , IFNULL(a.betAmount, 0)                     betAmount
         , IFNULL(a.validBetAmount, 0)                validBetAmount
         , IFNULL(a.totalTickets, 0)                  totalTickets
         , IFNULL(a.validTickets, 0)                  validTickets
         , IFNULL(a.failed_bet_amount, 0)             failed_bet_amount
         , IFNULL(a.failed_tickets, 0)                failed_tickets
         , IFNULL(a.betAmountSettled, 0)              betAmountSettled
         , IFNULL(a.tickedSettled, 0)                 ticketSettled
         , IFNULL(c.settleTickets, 0)                 settleTickets
         , IFNULL(c.settleBetAmount, 0)               settleBetAmount
         , IFNULL(c.settleProfit, 0)            AS    settleProfit
         , IFNULL(c.settleAmount, 0)            AS    settleReturn
         , 0                                    AS    settleProfitRate

         , IFNULL(c.settleExpectProfit, 0)      as    settleExpectProfit
         , IFNULL(a.seriesBetTickets, 0)              seriesBetTickets
         , IFNULL(a.seriesOrderAmountTotal, 0)        seriesBetAmount
         , IFNULL(a.seriesValid_tickets, 0)           seriesValidTickets
         , IFNULL(a.seriesValidBetAmount, 0)          seriesValidBetAmount
         , IFNULL(a.seriesProfit, 0)                  seriesProfit
         , IFNULL(a.seriesReturn_amount, 0)           seriesReturnAmount
         , IFNULL(a.seriesBetAmountSettled, 0)        seriesBetAmountSettled
         , IFNULL(a.seriesTickedSettled, 0)           seriesTicketSettled
         , IFNULL(a.seriesFailed_bet_amount, 0)       seriesFailedBetAmount
         , IFNULL(a.seriesFailed_tickets, 0)          seriesFailedTickets
         , IFNULL(c.seriesSettleTickets, 0)     as    settle_series_tickets
         , IFNULL(c.seriesSettleBetAmount, 0)   as    seriesSettleBetAmount
         , IFNULL(c.seriesSettleProfit, 0)      as    settle_series_profit
         , IFNULL(c.seriesSettleAmount, 0)      as    settle_series_amount

         , floor(IFNULL(a.order_valid_bet_money, 0))  order_valid_bet_money
         , floor(IFNULL(c.settle_valid_bet_money, 0)) settle_valid_bet_money
        , floor(IFNULL(c.order_valid_bet_count, 0)) order_valid_bet_count
    FROM (SELECT o.uid
          FROM tybss_merchant_common.t_order o
          WHERE o.create_time >= startTimeUTCL
            AND o.create_time <= endTimeUTCL
          UNION
          SELECT s.uid
          FROM tybss_merchant_common.t_settle s
          WHERE s.create_time >= startTimeUTCL
            AND s.create_time <= endTimeUTCL
            AND s.last_settle = 1
            and s.bet_amount > 0) w
             LEFT JOIN (SELECT o.uid
                             , COUNT(o.order_no)                                                     AS totalTickets
                             , SUM(o.order_amount_total)                                             AS betAmount
                             , SUM(s.settle_amount)                                                  AS settleAmount
                             , SUM(s.profit_amount)                                                  AS profit
                             , SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) AS validBetAmount
                             , SUM(CASE WHEN o.order_status = 1 THEN o.order_amount_total END)       AS betAmountSettled
                             , SUM(CASE WHEN o.order_status = 4 THEN o.order_amount_total END)       AS failed_bet_amount
                             , Count(CASE WHEN o.order_status = 4 THEN o.order_no END)               AS failed_tickets
                             , Count(CASE WHEN o.order_status = 1 THEN o.order_no END)               AS tickedSettled
                             , Count(CASE WHEN o.order_status in (0, 1) THEN o.order_no END)         AS validTickets

                             , count(case when o.series_type > 1 then o.order_no end)                AS seriesBetTickets
                             , SUM(case when o.series_type > 1 then o.order_amount_total end)        AS seriesOrderAmountTotal
                             , count(case when o.order_status in (0, 1) and o.series_type > 1 then o.order_no end)
                                                                                                        seriesValid_tickets
                             , SUM(case
                                       when o.order_status in (0, 1) and o.series_type > 1
                                           then o.order_amount_total end)                               seriesValidBetAmount
                             , SUM(case when o.series_type > 1 then s.profit_amount end)             AS seriesProfit
                             , SUM(case when o.series_type > 1 then s.settle_amount end)             AS seriesReturn_amount
                             , SUM(CASE
                                       WHEN o.order_status = 1 and o.series_type > 1
                                           THEN o.order_amount_total END)                            AS seriesBetAmountSettled
                             , Count(CASE WHEN o.order_status = 1 and o.series_type > 1 THEN o.order_no END)
                                                                                                     AS seriesTickedSettled
                             , SUM(CASE
                                       WHEN o.order_status = 4 and o.series_type > 1
                                           THEN o.order_amount_total END)                            AS seriesFailed_bet_amount
                             , Count(CASE WHEN o.order_status = 4 and o.series_type > 1 THEN o.order_no END)
                                                                                                     AS seriesFailed_tickets
                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(o.order_amount_total / 100) END)                 as order_valid_bet_money
                        FROM tybss_merchant_common.t_order o
                                 LEFT JOIN tybss_merchant_common.t_settle s
                                           ON o.order_no = s.order_no AND s.last_settle = 1 and s.bet_amount > 0 and
                                              s.out_come in (2, 3, 4, 5, 6)
                        WHERE o.create_time >= startTimeUTCL
                          AND o.create_time <= endTimeUTCL
                        GROUP BY o.uid) a ON w.uid = a.uid
             LEFT JOIN (SELECT s.uid
                             , COUNT(s.order_no)                                         AS settleTickets
                             , SUM(s.settle_amount)                                      AS settleAmount
                             , SUM(s.profit_amount)                                      AS settleProfit
                             , SUM(s.bet_amount)                                         AS settleBetAmount
                             , sum(o.expect_profit)                                      as settleExpectProfit
                             , COUNT(case when o.series_type > 1 then s.order_no end)    AS seriesSettleTickets
                             , SUM(case when o.series_type > 1 then s.settle_amount end) AS seriesSettleAmount
                             , SUM(case when o.series_type > 1 then s.profit_amount end) AS seriesSettleProfit
                             , SUM(case when o.series_type > 1 then s.bet_amount end)    AS seriesSettleBetAmount
                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(o.order_amount_total / 100) END)     as settle_valid_bet_money
                                                         , Count(CASE WHEN s.out_come in (3, 4, 5, 6) THEN s.order_no END)       AS order_valid_bet_count
                        FROM tybss_merchant_common.t_settle s
                                 left join tybss_merchant_common.t_order o on o.order_no = s.order_no
                        WHERE s.create_time >= startTimeUTCL
                          AND s.create_time <= endTimeUTCL
                          AND s.last_settle = 1
                          and s.bet_amount > 0
                          and s.out_come in (2, 3, 4, 5, 6)
                        GROUP BY s.uid) c ON w.uid = c.uid
             LEFT JOIN (SELECT u.merchant_code
                             , m.merchant_name
                             , m.`level`        AS merchantLevel
                             , u.create_time
                             , u.username
                             , m1.merchant_code as parent_code
                             , m1.merchant_name as parent_name
                             , u.uid
                        FROM tybss_merchant_common.t_user u
                                 LEFT JOIN tybss_merchant_common.t_merchant m ON u.merchant_code = m.merchant_code
                                 left join tybss_merchant_common.t_merchant m1 on m1.id = m.parent_id) d
                       ON w.uid = d.uid
    where a.totalTickets > 0
       or c.settleTickets > 0;

    SET end_time = get_cur_ymdhms();
    SELECT count(*) INTO total FROM tybss_report.r_user_order_hour where time = executetimeL;

    SET exce_msg = CONCAT(execute_time, startTimeUTCL, endTimeUTCL, ',r_user_order_hour:(1)执行成功!共:', total);

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);


    REPLACE INTO tybss_report.r_user_order_day_utc8
SELECT Concat(h.user_id, dayL)                         AS id
     , h.user_id
     , min(h.user_name)
     , u.currency_code
     , 1.0
     , min(h.merchant_code)
     , min(h.merchant_name)                            AS merchantName
     , dayL                                          AS time
         , IFNULL(sum(h.profit), 0)                           profit
         , IFNULL(sum(h.return_amount), 0)                    returnAmount
         , 0                                             AS profit_rate
         , IFNULL(sum(h.bet_amount), 0)                       betAmount
         , IFNULL(sum(h.valid_bet_amount), 0)                 validBetAmount
         , IFNULL(sum(h.bet_tickets), 0)                      totalTickets
         , IFNULL(sum(h.valid_tickets), 0)                    validTickets
         , IFNULL(sum(h.failed_bet_amount), 0)                failed_bet_amount
         , IFNULL(sum(h.failed_tickets), 0)                   failed_tickets
         , IFNULL(sum(h.bet_amount_settled), 0)               betAmountSettled
         , IFNULL(sum(h.ticket_settled), 0)                   ticketSettled
         , IFNULL(sum(h.settle_tickets), 0)                   settleTotalTickets
         , IFNULL(sum(h.settle_bet_amount), 0)                settleBetAmount
         , IFNULL(sum(h.settle_profit), 0)                 AS settleProfit
         , IFNULL(sum(h.settle_return), 0)                 AS settleReturn
         , sum(h.settle_profit) / IF(sum(h.settle_bet_amount) = '0', NULL, sum(h.settle_bet_amount))
                                                         AS settleProfitRate
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))          AS updatedTime
         , yearL                                         AS 'year'
         , min(h.parent_code)
         , min(h.parent_name)
         , IFNULL(sum(h.settle_expect_profit), 0)          as settleExpectProfit
         , IFNULL(sum(h.series_settle_bet_amount), 0)      as settle_series_amount
         , IFNULL(sum(h.series_settle_tickets), 0)         as settle_series_tickets
         , IFNULL(sum(h.series_settle_profit), 0)          as settle_series_profit

         , floor(IFNULL(sum(h.order_valid_bet_money), 0))  as order_valid_bet_money
         , floor(IFNULL(sum(h.settle_valid_bet_money), 0)) as settle_valid_bet_money
		 , floor(IFNULL(sum(order_valid_bet_count), 0)) as order_valid_bet_count
from tybss_report.r_user_order_hour h left join  tybss_merchant_common.t_user u on u.uid = h.user_id
where h.time like concat(dayL, '%')
  and h.user_id in (select user_id from tybss_report.r_user_order_hour where time = executetimeL)
group by h.user_id;


    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',r_user_order_hour(2),success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

CALL p_r_user_order_hour(now());
CALL p_r_user_order_hour(ADDDATE(now(), INTERVAL -1 hour));
