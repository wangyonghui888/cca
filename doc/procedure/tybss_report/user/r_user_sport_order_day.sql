DROP PROCEDURE IF EXISTS p_r_user_sport_order_day;
DELIMITER //
CREATE PROCEDURE p_r_user_sport_order_day(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 35;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);
    DECLARE endTimeL BIGINT(16);
    DECLARE startTimeL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);
    DECLARE endTimeUTCL BIGINT(16);
    DECLARE total INT(10) DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('p_r_user_sport_order_day错误码：', result_code, ' 错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set startTimeL = unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000;
    set endTimeL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1);
    set executetimeL = DATE_FORMAT(execute_date, '%Y%m%d');

    set startTimeUTCL = unix_timestamp(execute_date) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1;

    REPLACE INTO tybss_report.r_user_sport_order_day
    SELECT Concat(d.uid, a.sport_id, executetimeL)                                          AS id
         , a.sport_id
         , d.uid
         , d.username
         , d.currency_code
         , (CASE WHEN a.rmb_exchange_rate is null THEN c.rmb_exchange_rate else a.rmb_exchange_rate END)
                                                                                               exchange_rate
         , d.merchant_code
         , d.merchant_name                                                                  AS merchantName
         , executetimeL                                                                     AS time
         , IFNULL(a.orderAmount, 0)
         , IFNULL(a.orderAmountTotal, 0)
         , IFNULL(a.profit, 0)                                                                 profit
         , IFNULL(a.settleAmount, 0)                                                           returnAmount
         , IFNULL(a.profit / IF(a.orderAmountTotal = '0', NULL, a.orderAmountTotal), 0)     AS profit_rate
         , IFNULL(c.settleOrderAmount, 0)
         , IFNULL(c.settleBetAmount, 0)
         , IFNULL(c.settleProfit, 0)                                                        AS settle_profit
         , IFNULL(c.settleProfit / IF(c.settleBetAmount = '0', NULL, c.settleBetAmount), 0) AS settle_profit_rate
         , IFNULL(c.settleAmount, 0)                                                        AS settle_return
         , UNIX_TIMESTAMP(current_timestamp(3))                                             AS elasticsearch_id
         , DATE_FORMAT(execute_date, '%Y')                                                  AS 'year'
         , d.parent_code
         , d.parent_name
         , IFNULL(a.betAmountSettled, 0)                                                       betAmountSettled
         , IFNULL(a.tickedSettled, 0)                                                          ticketSettled
         , floor(IFNULL(a.order_valid_bet_money, 0))                                        as order_valid_bet_money
         , floor(IFNULL(c.settle_valid_bet_money, 0))                                       as settle_valid_bet_money
         , floor(IFNULL(c.order_valid_bet_count, 0))                                       as order_valid_bet_count
    FROM (SELECT od.uid
               , od.sport_id
               , COUNT(o.order_no)                                               AS orderAmount
               , min(o.rmb_exchange_rate)                                        AS rmb_exchange_rate
               , SUM(o.order_amount_total)                                       AS orderAmountTotal
               , SUM(s.settle_amount)                                            AS settleAmount
               , SUM(s.profit_amount)                                            AS profit
               , SUM(s.bet_amount)                                               AS validBetAmount
               , SUM(CASE WHEN o.order_status = 1 THEN o.order_amount_total END) AS betAmountSettled
               , Count(CASE WHEN o.order_status = 1 THEN o.order_no END)         AS tickedSettled
               , SUM(CASE
                         WHEN s.out_come in (3, 4, 5, 6) and ABS(o.order_amount_total) = ABS(s.profit_amount)
                             THEN ABS(s.profit_amount)
                         WHEN s.out_come in (3, 4, 5, 6) and ABS(o.order_amount_total) > ABS(s.profit_amount)
                             THEN ABS(s.profit_amount)
                         WHEN s.out_come in (3, 4, 5, 6) and ABS(o.order_amount_total) < ABS(s.profit_amount)
                             THEN ABS(o.order_amount_total) END)                 as order_valid_bet_money

          FROM tybss_merchant_common.t_order o
                   LEFT JOIN tybss_merchant_common.t_order_detail od ON o.order_no = od.order_no
                   LEFT JOIN tybss_merchant_common.t_settle s ON o.order_no = s.order_no AND s.last_settle = 1
          WHERE o.create_time >= startTimeL
            AND o.create_time <= endTimeL
            AND o.order_status in (0, 1)
            AND o.series_type = 1
          GROUP BY od.uid, od.sport_id) a
             LEFT JOIN (SELECT s.uid
                             , od.sport_id
                             , COUNT(s.order_no)                               AS settleOrderAmount
                             , min(o.rmb_exchange_rate)                        AS rmb_exchange_rate
                             , COUNT(DISTINCT s.uid)                           AS settleUserAmount
                             , SUM(s.settle_amount)                            AS settleAmount
                             , SUM(s.profit_amount)                            AS settleProfit
                             , SUM(s.bet_amount)                               AS settleBetAmount
                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(o.order_amount_total) END) as settle_valid_bet_money
                                             , Count(CASE WHEN s.out_come in (3, 4, 5, 6) THEN s.order_no END)      as order_valid_bet_count
                        FROM tybss_merchant_common.t_settle s
                                 LEFT JOIN tybss_merchant_common.t_order_detail od ON s.order_no = od.order_no
                                 LEFT JOIN tybss_merchant_common.t_order o ON s.order_no = o.order_no
                        WHERE s.create_time >= startTimeL
                          AND s.create_time <= endTimeL
                          AND o.series_type = 1
                          AND s.last_settle = 1
                          AND s.bet_amount > 0
                          and s.out_come in (2, 3, 4, 5, 6)
                        GROUP BY s.uid, od.sport_id) c ON a.uid = c.uid and a.sport_id = c.sport_id
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
                       ON a.uid = d.uid;
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();
    SELECT count(*) INTO total FROM tybss_report.r_user_sport_order_day where time = executetimeL;

    SET exce_msg = CONCAT(execute_date, ',p_r_user_sport_order_day:r_user_sport_order_day执行成功!共:', total);

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

    REPLACE INTO tybss_report.r_user_sport_order_day_utc8
    SELECT Concat(a.uid, a.sport_id, executetimeL)                                          AS id
         , a.sport_id                                                                          sportId
         , d.uid                                                                               userId
         , d.username                                                                          userName
         , d.currency_code
         , (CASE WHEN a.rmb_exchange_rate is null THEN c.rmb_exchange_rate else a.rmb_exchange_rate END)
                                                                                               exchange_rate
         , d.merchant_code                                                                     merchantCode
         , d.merchant_name                                                                  AS merchantName
         , executetimeL                                                                     AS time
         , IFNULL(a.orderAmount, 0)                                                            betTickets
         , IFNULL(a.orderAmountTotal, 0)                                                       betAmount
         , IFNULL(a.valid_tickets, 0)                                                          validTickets
         , IFNULL(a.validBetAmount, 0)                                                         validBetAmount
         , IFNULL(a.profit, 0)                                                                 profit
         , IFNULL(a.settleAmount, 0)                                                           returnAmount
         , IFNULL(a.profit / IF(a.orderAmountTotal = '0', NULL, a.orderAmountTotal), 0)     AS profit_rate
         , IFNULL(c.settleOrderAmount, 0)                                                      settleTickets
         , IFNULL(c.settleBetAmount, 0)                                                        settleBetAmount
         , IFNULL(c.settleProfit, 0)                                                        AS settle_profit
         , IFNULL(c.settleProfit / IF(c.settleBetAmount = '0', NULL, c.settleBetAmount), 0) AS settle_profit_rate
         , IFNULL(c.settleAmount, 0)                                                        AS settle_return
         , UNIX_TIMESTAMP(current_timestamp(3))                                             AS elasticsearch_id
         , DATE_FORMAT(execute_date, '%Y')                                                  AS 'year'
         , d.parent_code                                                                       parentCode
         , d.parent_name                                                                       parentName
         , IFNULL(a.betAmountSettled, 0)                                                       betAmountSettled
         , IFNULL(a.tickedSettled, 0)                                                          ticketSettled
         , IFNULL(a.failed_bet_amount, 0)                                                      failedBetAmount
         , IFNULL(a.failed_tickets, 0)                                                         failedTickets
         , floor(IFNULL(a.order_valid_bet_money, 0))                                        as order_valid_bet_money
         , floor(IFNULL(c.settle_valid_bet_money, 0))                                       as settle_valid_bet_money
         , floor(IFNULL(c.order_valid_bet_count, 0))                                       as order_valid_bet_count
    FROM (SELECT od.uid
               , od.sport_id
               , min(o.rmb_exchange_rate)                                              AS rmb_exchange_rate
               , COUNT(o.order_no)                                                     AS orderAmount
               , SUM(o.order_amount_total)                                             AS orderAmountTotal
               , count(case when o.order_status in (0, 1) then o.order_no end)            valid_tickets
               , SUM(case when o.order_status in (0, 1) then o.order_amount_total end) AS validBetAmount
               , SUM(s.profit_amount)                                                  AS profit
               , SUM(s.settle_amount)                                                  AS settleAmount
               , SUM(CASE WHEN o.order_status = 1 THEN o.order_amount_total END)       AS betAmountSettled
               , Count(CASE WHEN o.order_status = 1 THEN o.order_no END)               AS tickedSettled
               , SUM(CASE WHEN o.order_status = 4 THEN o.order_amount_total END)       AS failed_bet_amount
               , Count(CASE WHEN o.order_status = 4 THEN o.order_no END)               AS failed_tickets
               , SUM(CASE
                         WHEN s.out_come in (3, 4, 5, 6) and ABS(o.order_amount_total) = ABS(s.profit_amount)
                             THEN ABS(s.profit_amount)
                         WHEN s.out_come in (3, 4, 5, 6) and ABS(o.order_amount_total) > ABS(s.profit_amount)
                             THEN ABS(s.profit_amount)
                         WHEN s.out_come in (3, 4, 5, 6) and ABS(o.order_amount_total) < ABS(s.profit_amount)
                             THEN ABS(o.order_amount_total) END)                       as order_valid_bet_money

          FROM tybss_merchant_common.t_order o
                   LEFT JOIN tybss_merchant_common.t_order_detail od ON o.order_no = od.order_no
                   LEFT JOIN tybss_merchant_common.t_settle s ON o.order_no = s.order_no AND s.last_settle = 1
          WHERE o.create_time >= startTimeUTCL
            AND o.create_time <= endTimeUTCL
            AND o.series_type = 1
          GROUP BY od.uid, od.sport_id) a
             LEFT JOIN (SELECT s.uid
                             , od.sport_id
                             , min(o.rmb_exchange_rate)                        AS rmb_exchange_rate
                             , COUNT(s.order_no)                               AS settleOrderAmount
                             , SUM(s.bet_amount)                               AS settleBetAmount
                             , SUM(s.profit_amount)                            AS settleProfit
                             , SUM(s.settle_amount)                            AS settleAmount
                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(o.order_amount_total) END) as settle_valid_bet_money
                                             , Count(CASE WHEN s.out_come in (3, 4, 5, 6) THEN s.order_no END)      as order_valid_bet_count
                        FROM tybss_merchant_common.t_settle s
                                 LEFT JOIN tybss_merchant_common.t_order_detail od ON s.order_no = od.order_no
                                 LEFT JOIN tybss_merchant_common.t_order o ON s.order_no = o.order_no
                        WHERE s.create_time >= startTimeUTCL
                          AND s.create_time <= endTimeUTCL
                          AND o.series_type = 1
                          AND s.last_settle = 1
                          AND s.bet_amount > 0
                          and s.out_come in (2, 3, 4, 5, 6)
                        GROUP BY s.uid, od.sport_id) c ON a.uid = c.uid and a.sport_id = c.sport_id
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
                       ON a.uid = d.uid;


/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_date, ',p_r_user_sport_order_day:r_user_sport_order_day_utc8,success!');
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;
