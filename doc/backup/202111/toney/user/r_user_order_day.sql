DROP PROCEDURE IF EXISTS p_r_user_order_day;
DELIMITER //
CREATE PROCEDURE p_r_user_order_day(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 30;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_user_order_day成功';
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
        SET exce_msg = CONCAT('错误码：', result_code, ' p_r_user_order_day错误信息：', msg);
        SET result = 2;
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END;
    set executetimeL = DATE_FORMAT(execute_date, '%Y%m%d');
    set yearL = DATE_FORMAT(execute_date, '%Y');
    set startTimeL = unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000;
    set endTimeL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1);

    set startTimeUTCL = unix_timestamp(execute_date) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1;


delete from tybss_report.r_user_order_day where time = executetimeL;
REPLACE INTO tybss_report.r_user_order_day
SELECT Concat(w.uid, executetimeL)                                                      AS id
     , w.uid                                                                               uid
     , d.username
     , d.currency_code
     , (CASE WHEN a.rmb_exchange_rate is null THEN c.rmb_exchange_rate else a.rmb_exchange_rate END)
                                                                                           exchange_rate
     , d.merchant_code                                                                     merchantCode
     , d.merchant_name                                                                  AS merchantName
     , executetimeL                                                                     AS time
         , IFNULL(a.profit, 0)                                                                 profit
         , IFNULL(a.settleAmount, 0)                                                           returnAmount
         , IFNULL(a.profit / IF(a.validBetAmount = '0', NULL, a.validBetAmount), 0)         AS profit_rate
         , IFNULL(a.betAmount, 0)                                                              betAmount
         , IFNULL(a.validBetAmount, 0)                                                         validBetAmount
         , IFNULL(a.totalTickets, 0)                                                           totalTickets
         , IFNULL(a.validTickets, 0)                                                           validTickets
         , IFNULL(a.betAmountSettled, 0)                                                       betAmountSettled
         , IFNULL(a.tickedSettled, 0)                                                          ticketSettled
         , IFNULL(c.settleOrderAmount, 0)                                                      settleTotalTickets
         , IFNULL(c.settleBetAmount, 0)                                                        settleBetAmount
         , IFNULL(c.settleProfit, 0)                                                        AS settle_profit
         , IFNULL(c.settleAmount, 0)                                                        AS settle_return
         , IFNULL(c.settleProfit / IF(c.settleBetAmount = '0', NULL, c.settleBetAmount), 0) AS settle_profit_rate
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))                                             AS updatedTime
         , yearL                                                                            AS 'year'
         , d.parent_code
         , d.parent_name
         , IFNULL(c.settleExpectProfit, 0)                                                  as settleExpectProfit
         , IFNULL(c.settle_series_amount, 0)                                                as settle_series_amount
         , IFNULL(c.settle_series_tickets, 0)                                               as settle_series_tickets
         , IFNULL(c.settle_series_profit, 0)                                                as settle_series_profit
         , IFNULL(a.order_valid_bet_money,0)                                                as order_valid_bet_money
         , IFNULL(c.settle_valid_bet_money,0)                                               as settle_valid_bet_money

FROM (SELECT o.uid
    FROM tybss_new.t_order o
    WHERE (o.create_time >= startTimeL AND o.create_time <= endTimeL)
    UNION
    SELECT s.uid
    FROM tybss_new.t_settle s
    WHERE (s.create_time >= startTimeL AND s.create_time <= endTimeL)) w
    LEFT JOIN (SELECT o.uid
        , COUNT(o.order_no)                                                     AS totalTickets
        , SUM(o.order_amount_total)                                             AS betAmount
        , min(o.rmb_exchange_rate)                                              AS rmb_exchange_rate
        , SUM(s.settle_amount)                                                  AS settleAmount
        , SUM(s.profit_amount)                                                  AS profit
        , SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) AS validBetAmount
        , SUM(CASE WHEN o.order_status = 1 THEN o.order_amount_total END)       AS betAmountSettled
        , Count(CASE WHEN o.order_status = 1 THEN o.order_no END)               AS tickedSettled
        , Count(CASE WHEN o.order_status in (0, 1) THEN o.order_no END)         AS validTickets

        , SUM(CASE  WHEN s.out_come in (3,4,5,6) and ABS(o.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                    WHEN s.out_come in (3,4,5,6) and ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                    WHEN s.out_come in (3,4,5,6) and ABS(o.order_amount_total) < ABS(s.profit_amount) THEN ABS(o.order_amount_total/100)
            END)  as order_valid_bet_money

    FROM tybss_new.t_order o
    LEFT JOIN tybss_new.t_settle s ON o.order_no = s.order_no AND s.last_settle = 1
    WHERE o.create_time >= startTimeL
    AND o.create_time <= endTimeL
    GROUP BY o.uid) a ON w.uid = a.uid
    LEFT JOIN (SELECT s.uid
    , COUNT(s.order_no)                                               AS settleOrderAmount
    , SUM(s.settle_amount)                                            AS settleAmount
    , SUM(s.profit_amount)                                            AS settleProfit
    , SUM(s.bet_amount)                                               AS settleBetAmount
    , sum(r.expect_profit)                                            as settleExpectProfit
    , min(r.rmb_exchange_rate)                                        AS rmb_exchange_rate
    , SUM(CASE WHEN r.series_type <> 1 THEN r.order_amount_total END) as settle_series_amount
    , COUNT(CASE WHEN r.series_type <> 1 THEN r.order_no END)         as settle_series_tickets
    , SUM(CASE WHEN r.series_type <> 1 THEN s.profit_amount END)      as settle_series_profit
    , SUM(CASE WHEN s.out_come in (3,4,5,6) and ABS(r.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                WHEN s.out_come in (3,4,5,6) and ABS(r.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                WHEN s.out_come in (3,4,5,6) and ABS(r.order_amount_total) < ABS(s.profit_amount) THEN ABS(r.order_amount_total/100)
             END)  as settle_valid_bet_money
    FROM tybss_new.t_settle s
    left join tybss_new.t_order r on r.order_no = s.order_no
    WHERE s.create_time >= startTimeL
    AND s.create_time <= endTimeL
    AND s.last_settle = 1
    and s.bet_amount > 0
    and r.order_status = 1
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
    FROM tybss_new.t_user u
    LEFT JOIN tybss_new.t_merchant m ON u.merchant_code = m.merchant_code
    left join tybss_new.t_merchant m1 on m1.id = m.parent_id) d ON w.uid = d.uid;
commit;

SET end_time = get_cur_ymdhms();
SELECT count(*) INTO total FROM tybss_report.r_user_order_day where time = executetimeL;

SET exce_msg = CONCAT(execute_date, ',p_r_user_order_day:(1)执行成功!共:', total);

CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);


delete from tybss_report.r_user_order_day_utc8 where time = executetimeL;
REPLACE INTO tybss_report.r_user_order_day_utc8
SELECT Concat(w.uid, executetimeL)                                                      AS id
     , w.uid
     , d.username
     , d.currency_code
     , (CASE WHEN a.rmb_exchange_rate is null THEN c.rmb_exchange_rate else a.rmb_exchange_rate END)
                                                                                           exchange_rate
     , d.merchant_code
     , d.merchant_name                                                                  AS merchantName
     , executetimeL                                                                     AS time
         , IFNULL(a.profit, 0)                                                                 profit
         , IFNULL(a.settleAmount, 0)                                                           returnAmount
         , IFNULL(a.profit / IF(a.validBetAmount = '0', NULL, a.validBetAmount), 0)         AS profit_rate
         , IFNULL(a.betAmount, 0)                                                              betAmount
         , IFNULL(a.validBetAmount, 0)                                                         validBetAmount
         , IFNULL(a.totalTickets, 0)                                                           totalTickets
         , IFNULL(a.validTickets, 0)                                                           validTickets
         , IFNULL(a.failed_bet_amount, 0)                                                      failed_bet_amount
         , IFNULL(a.failed_tickets, 0)                                                         failed_tickets
         , IFNULL(a.betAmountSettled, 0)                                                       betAmountSettled
         , IFNULL(a.tickedSettled, 0)                                                          ticketSettled
         , IFNULL(c.settleOrderAmount, 0)                                                      settleTotalTickets
         , IFNULL(c.settleBetAmount, 0)                                                        settleBetAmount
         , IFNULL(c.settleProfit, 0)                                                        AS settleProfit
         , IFNULL(c.settleAmount, 0)                                                        AS settleReturn
         , IFNULL(c.settleProfit / IF(c.settleBetAmount = '0', NULL, c.settleBetAmount), 0) AS settleProfitRate
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))                                             AS updatedTime
         , yearL                                                                            AS 'year'
         , d.parent_code
         , d.parent_name
         , IFNULL(c.settleExpectProfit, 0)                                                  as settleExpectProfit
         , IFNULL(c.settle_series_amount, 0)                                                as settle_series_amount
         , IFNULL(c.settle_series_tickets, 0)                                               as settle_series_tickets
         , IFNULL(c.settle_series_profit, 0)                                                as settle_series_profit
         , IFNULL(a.order_valid_bet_money,0)                                                as order_valid_bet_money
         , IFNULL(c.settle_valid_bet_money,0)                                               as settle_valid_bet_money
FROM (SELECT o.uid
    FROM tybss_new.t_order o
    WHERE o.create_time >= startTimeUTCL
    AND o.create_time <= endTimeUTCL
    UNION
    SELECT s.uid
    FROM tybss_new.t_settle s
    WHERE s.create_time >= startTimeUTCL
    AND s.create_time <= endTimeUTCL
    AND s.last_settle = 1
    and s.bet_amount > 0) w
    LEFT JOIN (SELECT o.uid
        , COUNT(o.order_no)                                                     AS totalTickets
        , SUM(o.order_amount_total)                                             AS betAmount
        , min(o.rmb_exchange_rate)                                              AS rmb_exchange_rate
        , SUM(s.settle_amount)                                                  AS settleAmount
        , SUM(s.profit_amount)                                                  AS profit
        , SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) AS validBetAmount
        , SUM(CASE WHEN o.order_status = 1 THEN o.order_amount_total END)       AS betAmountSettled
        , Count(CASE WHEN o.order_status = 1 THEN o.order_no END)               AS tickedSettled
        , Count(CASE WHEN o.order_status in (0, 1) THEN o.order_no END)         AS validTickets
        , SUM(CASE WHEN o.order_status = 4 THEN o.order_amount_total END)       AS failed_bet_amount
        , Count(CASE WHEN o.order_status = 4 THEN o.order_no END)               AS failed_tickets
        , SUM(CASE  WHEN s.out_come in (3,4,5,6) and ABS(o.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                    WHEN s.out_come in (3,4,5,6) and ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                    WHEN s.out_come in (3,4,5,6) and ABS(o.order_amount_total) < ABS(s.profit_amount) THEN ABS(o.order_amount_total/100)
              END)  as order_valid_bet_money
    FROM tybss_new.t_order o
    LEFT JOIN tybss_new.t_settle s
    ON o.order_no = s.order_no AND s.last_settle = 1 and s.bet_amount > 0
    WHERE o.create_time >= startTimeUTCL
    AND o.create_time <= endTimeUTCL
    GROUP BY o.uid) a ON w.uid = a.uid
    LEFT JOIN (SELECT s.uid
    , COUNT(s.order_no)                                               AS settleOrderAmount
    , SUM(s.settle_amount)                                            AS settleAmount
    , SUM(s.profit_amount)                                            AS settleProfit
    , SUM(s.bet_amount)                                               AS settleBetAmount
    , sum(r.expect_profit)                                            as settleExpectProfit
    , min(r.rmb_exchange_rate)                                        AS rmb_exchange_rate
    , SUM(CASE WHEN r.series_type <> 1 THEN r.order_amount_total END) as settle_series_amount
    , COUNT(CASE WHEN r.series_type <> 1 THEN r.order_no END)         as settle_series_tickets
    , SUM(CASE WHEN r.series_type <> 1 THEN s.profit_amount END)      as settle_series_profit
    , SUM(CASE  WHEN s.out_come in (3,4,5,6) and ABS(r.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                WHEN s.out_come in (3,4,5,6) and ABS(r.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                WHEN s.out_come in (3,4,5,6) and ABS(r.order_amount_total) < ABS(s.profit_amount) THEN ABS(r.order_amount_total/100)
            END)  as settle_valid_bet_money
    FROM tybss_new.t_settle s
    left join tybss_new.t_order r on r.order_no = s.order_no
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
    , u.currency_code
    FROM tybss_new.t_user u
    LEFT JOIN tybss_new.t_merchant m ON u.merchant_code = m.merchant_code
    left join tybss_new.t_merchant m1 on m1.id = m.parent_id) d ON w.uid = d.uid;
SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_date, ',p_r_user_order_day:(2),success!');
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
