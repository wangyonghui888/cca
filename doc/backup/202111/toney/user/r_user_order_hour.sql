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
    SELECT Concat(w.uid, executetimeL)                                                      AS id
         , w.uid
         , d.username
         , d.merchant_code
         , d.merchant_name                                                                  AS merchantName
         , d.parent_code
         , d.parent_name
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))                                             AS updatedTime
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
         , IFNULL(c.settleTickets, 0)                                                          settleTickets
         , IFNULL(c.settleBetAmount, 0)                                                        settleBetAmount
         , IFNULL(c.settleProfit, 0)                                                        AS settleProfit
         , IFNULL(c.settleAmount, 0)                                                        AS settleReturn
         , IFNULL(c.settleProfit / IF(c.settleBetAmount = '0', NULL, c.settleBetAmount), 0) AS settleProfitRate

         , IFNULL(c.settleExpectProfit, 0)                                                  as settleExpectProfit
         , IFNULL(a.seriesBetTickets, 0)                                                       seriesBetTickets
         , IFNULL(a.seriesOrderAmountTotal, 0)                                                 seriesBetAmount
         , IFNULL(a.seriesValid_tickets, 0)                                                    seriesValidTickets
         , IFNULL(a.seriesValidBetAmount, 0)                                                   seriesValidBetAmount
         , IFNULL(a.seriesProfit, 0)                                                           seriesProfit
         , IFNULL(a.seriesReturn_amount, 0)                                                    seriesReturnAmount
         , IFNULL(a.seriesBetAmountSettled, 0)                                                 seriesBetAmountSettled
         , IFNULL(a.seriesTickedSettled, 0)                                                    seriesTicketSettled
         , IFNULL(a.seriesFailed_bet_amount, 0)                                                seriesFailedBetAmount
         , IFNULL(a.seriesFailed_tickets, 0)                                                   seriesFailedTickets
         , IFNULL(c.seriesSettleTickets, 0)                                                 as settle_series_tickets
         , IFNULL(c.seriesSettleBetAmount, 0)                                               as seriesSettleBetAmount
         , IFNULL(c.seriesSettleProfit, 0)                                                  as settle_series_profit
         , IFNULL(c.seriesSettleAmount, 0)                                                  as settle_series_amount

         , IFNULL(a.paBetTickets, 0)                                                           paBetTickets
         , IFNULL(a.paOrderAmountTotal, 0)                                                     paBetAmount
         , IFNULL(a.paValid_tickets, 0)                                                        paValidTickets
         , IFNULL(a.paValidBetAmount, 0)                                                       paValidBetAmount
         , IFNULL(a.paProfit, 0)                                                               paProfit
         , IFNULL(a.paReturn_amount, 0)                                                        paReturnAmount
         , IFNULL(a.paBetAmountSettled, 0)                                                     paBetAmountSettled
         , IFNULL(a.paTickedSettled, 0)                                                        paTicketSettled
         , IFNULL(a.paFailed_bet_amount, 0)                                                    paFailedBetAmount
         , IFNULL(a.paFailed_tickets, 0)                                                       paFailedTickets
         , IFNULL(c.paSettleTickets, 0)                                                        paSettleTickets
         , IFNULL(c.paSettleBetAmount, 0)                                                      paSettleBetAmount
         , IFNULL(c.paSettleProfit, 0)                                                         paSettleProfit
         , IFNULL(c.paSettleAmount, 0)                                                         paSettleReturn

         , IFNULL(a.mtsBetTickets, 0)                                                          mtsBetTickets
         , IFNULL(a.mtsOrderAmountTotal, 0)                                                    mtsBetAmount
         , IFNULL(a.mtsValid_tickets, 0)                                                       mtsValidTickets
         , IFNULL(a.mtsValidBetAmount, 0)                                                      mtsValidBetAmount
         , IFNULL(a.mtsProfit, 0)                                                              mtsProfit
         , IFNULL(a.mtsReturn_amount, 0)                                                       mtsReturnAmount
         , IFNULL(a.mtsBetAmountSettled, 0)                                                    mtsBetAmountSettled
         , IFNULL(a.mtsTickedSettled, 0)                                                       mtsTicketSettled
         , IFNULL(a.mtsFailed_bet_amount, 0)                                                   mtsFailedBetAmount
         , IFNULL(a.mtsFailed_tickets, 0)                                                      mtsFailedTickets
         , IFNULL(c.mtsSettleTickets, 0)                                                       mtsSettleTickets
         , IFNULL(c.mtsSettleBetAmount, 0)                                                     mtsSettleBetAmount
         , IFNULL(c.mtsSettleProfit, 0)                                                        mtsSettleProfit
         , IFNULL(c.mtsSettleAmount, 0)                                                        mtsSettleReturn
         , IFNULL(a.order_valid_bet_money, 0)                                                  order_valid_bet_money
         , IFNULL(c.settle_valid_bet_money, 0)                                                 settle_valid_bet_money


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
                             , SUM(s.settle_amount)                                                  AS settleAmount
                             , SUM(s.profit_amount)                                                  AS profit
                             , SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) AS validBetAmount
                             , SUM(CASE WHEN o.order_status = 1 THEN o.order_amount_total END)       AS betAmountSettled
                             , SUM(CASE WHEN o.order_status = 4 THEN o.order_amount_total END)       AS failed_bet_amount
                             , Count(CASE WHEN o.order_status = 4 THEN o.order_no END)               AS failed_tickets
                             , Count(CASE WHEN o.order_status = 1 THEN o.order_no END)               AS tickedSettled
                             , Count(CASE WHEN o.order_status in (0, 1) THEN o.order_no END)         AS validTickets


                             , COUNT(case when o.manager_code = 1 then o.order_no end)               AS paBetTickets
                             , SUM(case when o.manager_code = 1 then o.order_amount_total end)       AS paOrderAmountTotal
                             , count(case when o.order_status in (0, 1) and o.manager_code = 1 then o.order_no end)
                                                                                                        paValid_tickets
                             , SUM(case
                                       when o.order_status in (0, 1) and o.manager_code = 1
                                           then o.order_amount_total end)                               paValidBetAmount
                             , SUM(case when o.manager_code = 1 then s.profit_amount end)            AS paProfit
                             , SUM(case when o.manager_code = 1 then s.settle_amount end)            AS paReturn_amount
                             , SUM(CASE
                                       WHEN o.order_status = 1 and o.manager_code = 1
                                           THEN o.order_amount_total END)                            AS paBetAmountSettled
                             , Count(CASE WHEN o.order_status = 1 and o.manager_code = 1 THEN o.order_no END)
                                                                                                     AS paTickedSettled
                             , SUM(CASE
                                       WHEN o.order_status = 4 and o.manager_code = 1
                                           THEN o.order_amount_total END)                            AS paFailed_bet_amount
                             , Count(CASE WHEN o.order_status = 4 and o.manager_code = 1 THEN o.order_no END)
                                                                                                     AS paFailed_tickets
                             , COUNT(case when o.manager_code = 2 then o.order_no end)               AS mtsBetTickets
                             , SUM(case when o.manager_code = 2 then o.order_amount_total end)       AS mtsOrderAmountTotal
                             , count(case when o.order_status in (0, 1) and o.manager_code = 2 then o.order_no end)
                                                                                                        mtsValid_tickets
                             , SUM(case
                                       when o.order_status in (0, 1) and o.manager_code = 2
                                           then o.order_amount_total end)                               mtsValidBetAmount
                             , SUM(case when o.manager_code = 2 then s.profit_amount end)            AS mtsProfit
                             , SUM(case when o.manager_code = 2 then s.settle_amount end)            AS mtsReturn_amount
                             , SUM(CASE
                                       WHEN o.order_status = 1 and o.manager_code = 2
                                           THEN o.order_amount_total END)                            AS mtsBetAmountSettled
                             , Count(CASE WHEN o.order_status = 1 and o.manager_code = 2 THEN o.order_no END)
                                                                                                     AS mtsTickedSettled
                             , SUM(CASE
                                       WHEN o.order_status = 4 and o.manager_code = 2
                                           THEN o.order_amount_total END)                            AS mtsFailed_bet_amount
                             , Count(CASE WHEN o.order_status = 4 and o.manager_code = 2 THEN o.order_no END)
                                                                                                     AS mtsFailed_tickets

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
                                            ABS(o.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(o.order_amount_total/100)
            END)                                                                                     as order_valid_bet_money

                        FROM tybss_new.t_order o
                                 LEFT JOIN tybss_new.t_settle s
                                           ON o.order_no = s.order_no AND s.last_settle = 1 and s.bet_amount > 0
                        WHERE o.create_time >= startTimeUTCL
                          AND o.create_time <= endTimeUTCL
                        GROUP BY o.uid) a ON w.uid = a.uid
             LEFT JOIN (SELECT s.uid
                             , COUNT(s.order_no)                                          AS settleTickets
                             , SUM(s.settle_amount)                                       AS settleAmount
                             , SUM(s.profit_amount)                                       AS settleProfit
                             , SUM(s.bet_amount)                                          AS settleBetAmount
                             , sum(o.expect_profit)                                       as settleExpectProfit

                             , COUNT(case when o.manager_code = 1 then s.order_no end)    AS paSettleTickets
                             , SUM(case when o.manager_code = 1 then s.settle_amount end) AS paSettleAmount
                             , SUM(case when o.manager_code = 1 then s.profit_amount end) AS paSettleProfit
                             , SUM(case when o.manager_code = 1 then s.bet_amount end)    AS paSettleBetAmount

                             , COUNT(case when o.manager_code = 2 then s.order_no end)    AS mtsSettleTickets
                             , SUM(case when o.manager_code = 2 then s.settle_amount end) AS mtsSettleAmount
                             , SUM(case when o.manager_code = 2 then s.profit_amount end) AS mtsSettleProfit
                             , SUM(case when o.manager_code = 2 then s.bet_amount end)    AS mtsSettleBetAmount

                             , COUNT(case when o.series_type > 1 then s.order_no end)     AS seriesSettleTickets
                             , SUM(case when o.series_type > 1 then s.settle_amount end)  AS seriesSettleAmount
                             , SUM(case when o.series_type > 1 then s.profit_amount end)  AS seriesSettleProfit
                             , SUM(case when o.series_type > 1 then s.bet_amount end)     AS seriesSettleBetAmount
                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(o.order_amount_total/100)
            END)                                                                          as settle_valid_bet_money
                        FROM tybss_new.t_settle s
                                 left join tybss_new.t_order o on o.order_no = s.order_no
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
                        FROM tybss_new.t_user u
                                 LEFT JOIN tybss_new.t_merchant m ON u.merchant_code = m.merchant_code
                                 left join tybss_new.t_merchant m1 on m1.id = m.parent_id) d ON w.uid = d.uid;

    SET end_time = get_cur_ymdhms();
    SELECT count(*) INTO total FROM tybss_report.r_user_order_hour where time = executetimeL;

    SET exce_msg = CONCAT(execute_time, startTimeUTCL, endTimeUTCL, ',r_user_order_hour:(1)执行成功!共:', total);

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

    REPLACE into tybss_report.r_user_order_hour_sub
    SELECT Concat(w.uid, executetimeL)          AS id
         , w.uid                                   userId
         , c.merchant_code                         merchantCode
         , executetimeL                         AS time
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) AS updatedTime
         , IFNULL(a.preBetTickets, 0)              preBetTickets
         , IFNULL(a.preOrderAmountTotal, 0)        preBetAmount
         , IFNULL(a.preValid_tickets, 0)           preValidTickets
         , IFNULL(a.preValidBetAmount, 0)          preValidBetAmount
         , IFNULL(a.preProfit, 0)                  preProfit
         , IFNULL(a.preReturn_amount, 0)           preReturnAmount
         , IFNULL(a.preBetAmountSettled, 0)        preBetAmountSettled
         , IFNULL(a.preTickedSettled, 0)           preTicketSettled
         , IFNULL(a.preFailed_bet_amount, 0)       preFailedBetAmount
         , IFNULL(a.preFailed_tickets, 0)          preFailedTickets
         , IFNULL(b.preSettleTickets, 0)           preSettleTickets
         , IFNULL(b.preSettleBetAmount, 0)         preSettleBetAmount
         , IFNULL(b.preSettleProfit, 0)            preSettleProfit
         , IFNULL(b.preSettleAmount, 0)            preSettleReturn
         , IFNULL(a.liveBetTickets, 0)             liveBetTickets
         , IFNULL(a.liveOrderAmountTotal, 0)       liveBetAmount
         , IFNULL(a.liveValid_tickets, 0)          liveValidTickets
         , IFNULL(a.liveValidBetAmount, 0)         liveValidBetAmount
         , IFNULL(a.liveProfit, 0)                 liveProfit
         , IFNULL(a.liveReturn_amount, 0)          liveReturnAmount
         , IFNULL(a.liveBetAmountSettled, 0)       liveBetAmountSettled
         , IFNULL(a.liveTickedSettled, 0)          liveTicketSettled
         , IFNULL(a.liveFailed_bet_amount, 0)      liveFailedBetAmount
         , IFNULL(a.liveFailed_tickets, 0)         liveFailedTickets
         , IFNULL(b.liveSettleTickets, 0)          liveSettleTickets
         , IFNULL(b.liveSettleBetAmount, 0)        liveSettleBetAmount
         , IFNULL(b.liveSettleProfit, 0)           liveSettleProfit
         , IFNULL(b.liveSettleAmount, 0)           liveSettleReturn
         , IFNULL(a.order_valid_bet_money, 0)      order_valid_bet_money
         , IFNULL(b.settle_valid_bet_money, 0)     settle_valid_bet_money
    FROM (SELECT o.uid
          FROM tybss_new.t_order o
          WHERE o.create_time >= startTimeUTCL
            AND o.create_time <= endTimeUTCL
            and o.series_type = 1
          UNION
          SELECT s.uid
          FROM tybss_new.t_settle s
          WHERE s.create_time >= startTimeUTCL
            AND s.create_time <= endTimeUTCL
            AND s.last_settle = 1
            and s.bet_amount > 0) w
             left join (SELECT o.uid
                             , COUNT(case when od.match_type = 1 then o.order_no end)         AS preBetTickets
                             , SUM(case when od.match_type = 1 then o.order_amount_total end) AS preOrderAmountTotal
                             , count(case
                                         when o.order_status in (0, 1) and od.match_type = 1 and o.series_type = 1
                                             then o.order_no end)                                preValid_tickets
                             , SUM(case
                                       when o.order_status in (0, 1) and od.match_type = 1 and o.series_type = 1
                                           then o.order_amount_total end)                        preValidBetAmount
                             , SUM(case when od.match_type = 1 then s.profit_amount end)
                                                                                              AS preProfit
                             , SUM(case when od.match_type = 1 then s.settle_amount end)
                                                                                              AS preReturn_amount
                             , SUM(CASE
                                       WHEN o.order_status = 1 and od.match_type = 1
                                           THEN o.order_amount_total END)                     AS preBetAmountSettled
                             , Count(CASE
                                         WHEN o.order_status = 1 and od.match_type = 1
                                             THEN o.order_no END)                             AS preTickedSettled
                             , SUM(CASE
                                       WHEN o.order_status = 4 and od.match_type = 1
                                           THEN o.order_amount_total END)                     AS preFailed_bet_amount
                             , Count(CASE
                                         WHEN o.order_status = 4 and od.match_type = 1
                                             THEN o.order_no END)                             AS preFailed_tickets

                             , COUNT(case when od.match_type = 2 then o.order_no end)
                                                                                              AS liveBetTickets
                             , SUM(case
                                       when od.match_type = 2
                                           then o.order_amount_total end)                     AS liveOrderAmountTotal
                             , count(case
                                         when o.order_status in (0, 1) and od.match_type = 2
                                             then o.order_no end)                                liveValid_tickets
                             , SUM(case
                                       when o.order_status in (0, 1) and od.match_type = 2
                                           then o.order_amount_total end)                        liveValidBetAmount
                             , SUM(case when od.match_type = 2 then s.profit_amount end)
                                                                                              AS liveProfit
                             , SUM(case when od.match_type = 2 then s.settle_amount end)
                                                                                              AS liveReturn_amount
                             , SUM(CASE
                                       WHEN o.order_status = 1 and od.match_type = 2
                                           THEN o.order_amount_total END)                     AS liveBetAmountSettled
                             , Count(CASE
                                         WHEN o.order_status = 1 and od.match_type = 2
                                             THEN o.order_no END)                             AS liveTickedSettled
                             , SUM(CASE
                                       WHEN o.order_status = 4 and od.match_type = 2
                                           THEN o.order_amount_total END)                     AS liveFailed_bet_amount
                             , Count(CASE
                                         WHEN o.order_status = 4 and od.match_type = 2
                                             THEN o.order_no END)                             AS liveFailed_tickets

                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(o.order_amount_total/100)
            END)                                                                              as order_valid_bet_money
                        FROM tybss_new.t_order o
                                 LEFT JOIN tybss_new.t_order_detail od ON o.order_no = od.order_no
                                 LEFT JOIN tybss_new.t_settle s
                                           ON o.order_no = s.order_no AND s.last_settle = 1 and s.bet_amount > 0 and
                                              s.out_come in (2, 3, 4, 5, 6)
                        WHERE o.create_time >= startTimeUTCL
                          AND o.create_time <= endTimeUTCL
                          and o.series_type = 1
                        GROUP BY o.uid) a on w.uid = a.uid
             LEFT JOIN (SELECT s.uid
                             , COUNT(case when od.match_type = 1 then s.order_no end)    AS preSettleTickets
                             , SUM(case when od.match_type = 1 then s.settle_amount end) AS preSettleAmount
                             , SUM(case when od.match_type = 1 then s.profit_amount end) AS preSettleProfit
                             , SUM(case when od.match_type = 1 then s.bet_amount end)    AS preSettleBetAmount
                             , COUNT(case when od.match_type = 2 then s.order_no end)    AS liveSettleTickets
                             , SUM(case when od.match_type = 2 then s.settle_amount end) AS liveSettleAmount
                             , SUM(case when od.match_type = 2 then s.profit_amount end) AS liveSettleProfit
                             , SUM(case when od.match_type = 2 then s.bet_amount end)    AS liveSettleBetAmount

                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(s.profit_amount) THEN ABS(s.profit_amount/100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(o.order_amount_total/100)
            END)                                                                         as settle_valid_bet_money

                        FROM tybss_new.t_settle s
                                 LEFT JOIN tybss_new.t_order_detail od ON s.order_no = od.order_no
                                 LEFT JOIN tybss_new.t_order o ON s.order_no = o.order_no
                        WHERE s.create_time >= startTimeUTCL
                          AND s.create_time <= endTimeUTCL
                          AND s.last_settle = 1
                          AND s.bet_amount > 0
                          and s.out_come in (2, 3, 4, 5, 6)
                          and o.series_type = 1
                        GROUP BY s.uid) b ON w.uid = b.uid
             LEFT JOIN (SELECT u.merchant_code, u.uid
                        FROM tybss_new.t_user u) c ON w.uid = c.uid;


    REPLACE INTO tybss_report.r_user_order_day_utc8
    SELECT Concat(user_id, dayL)                                                      AS id
         , user_id
         , min(user_name)
         , '1'
         , 1.0
         , min(merchant_code)
         , min(merchant_name)                                                         AS merchantName
         , dayL                                                                       AS time
         , IFNULL(sum(profit), 0)                                                        profit
         , IFNULL(sum(return_amount), 0)                                                 returnAmount
         , sum(profit) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount)) AS profit_rate
         , IFNULL(sum(bet_amount), 0)                                                    betAmount
         , IFNULL(sum(valid_bet_amount), 0)                                              validBetAmount
         , IFNULL(sum(bet_tickets), 0)                                                   totalTickets
         , IFNULL(sum(valid_tickets), 0)                                                 validTickets
         , IFNULL(sum(failed_bet_amount), 0)                                             failed_bet_amount
         , IFNULL(sum(failed_tickets), 0)                                                failed_tickets
         , IFNULL(sum(bet_amount_settled), 0)                                            betAmountSettled
         , IFNULL(sum(ticket_settled), 0)                                                ticketSettled
         , IFNULL(sum(settle_tickets), 0)                                                settleTotalTickets
         , IFNULL(sum(settle_bet_amount), 0)                                             settleBetAmount
         , IFNULL(sum(settle_profit), 0)                                              AS settleProfit
         , IFNULL(sum(settle_return), 0)                                              AS settleReturn
         , sum(settle_profit) / IF(sum(settle_bet_amount) = '0', NULL, sum(settle_bet_amount))
                                                                                      AS settleProfitRate
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))                                       AS updatedTime
         , yearL                                                                      AS 'year'
         , min(parent_code)
         , min(parent_name)
         , IFNULL(sum(settle_expect_profit), 0)                                       as settleExpectProfit
         , IFNULL(sum(series_settle_bet_amount), 0)                                   as settle_series_amount
         , IFNULL(sum(series_settle_tickets), 0)                                      as settle_series_tickets
         , IFNULL(sum(series_settle_profit), 0)                                       as settle_series_profit

         , IFNULL(sum(order_valid_bet_money), 0)                                      as order_valid_bet_money
         , IFNULL(sum(settle_valid_bet_money), 0)                                     as settle_valid_bet_money

    from tybss_report.r_user_order_hour
    where time like concat(dayL, '%')
      and user_id in (select user_id from tybss_report.r_user_order_hour where time = executetimeL)
    group by user_id;


    SELECT count(*) INTO total FROM tybss_report.r_user_order_day_utc8 where time = dayL;

    SET exce_msg = CONCAT(execute_time, ',r_user_order_day_utc8:(2)执行成功!共:', total);

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

    REPLACE INTO tybss_report.r_user_order_day_utc8_sub
    select Concat(u.user_id, dayL) AS id,
           u.user_id,
           min(u.merchant_code),
           min(u.parent_code),
           dayL                    AS time,
           IFNULL(sum(s.pre_bet_tickets), 0),
           IFNULL(sum(s.pre_bet_amount), 0),
           IFNULL(sum(s.pre_valid_tickets), 0),
           IFNULL(sum(s.pre_valid_bet_amount), 0),
           IFNULL(sum(s.pre_profit), 0),
           IFNULL(sum(s.pre_return_amount), 0),
           IFNULL(sum(s.pre_bet_amount_settled), 0),
           IFNULL(sum(s.pre_ticket_settled), 0),
           IFNULL(sum(s.pre_failed_bet_amount), 0),
           IFNULL(sum(s.pre_failed_tickets), 0),
           IFNULL(sum(s.pre_settle_tickets), 0),
           IFNULL(sum(s.pre_settle_bet_amount), 0),
           IFNULL(sum(s.pre_settle_profit), 0),
           IFNULL(sum(s.pre_settle_return), 0),
           IFNULL(sum(s.live_bet_tickets), 0),
           IFNULL(sum(s.live_bet_amount), 0),
           IFNULL(sum(s.live_valid_tickets), 0),
           IFNULL(sum(s.live_valid_bet_amount), 0),
           IFNULL(sum(s.live_profit), 0),
           IFNULL(sum(s.live_return_amount), 0),
           IFNULL(sum(s.live_bet_amount_settled), 0),
           IFNULL(sum(s.live_ticket_settled), 0),
           IFNULL(sum(s.live_failed_bet_amount), 0),
           IFNULL(sum(s.live_failed_tickets), 0),
           IFNULL(sum(s.live_settle_tickets), 0),
           IFNULL(sum(s.live_settle_bet_amount), 0),
           IFNULL(sum(s.live_settle_profit), 0),
           IFNULL(sum(s.live_settle_return), 0),
           IFNULL(sum(u.pa_bet_tickets), 0),
           IFNULL(sum(u.pa_bet_amount), 0),
           IFNULL(sum(u.pa_valid_tickets), 0),
           IFNULL(sum(u.pa_valid_bet_amount), 0),
           IFNULL(sum(u.pa_profit), 0),
           IFNULL(sum(u.pa_return_amount), 0),
           IFNULL(sum(u.pa_bet_amount_settled), 0),
           IFNULL(sum(u.pa_ticket_settled), 0),
           IFNULL(sum(u.pa_failed_bet_amount), 0),
           IFNULL(sum(u.pa_failed_tickets), 0),
           IFNULL(sum(u.pa_settle_tickets), 0),
           IFNULL(sum(u.pa_settle_bet_amount), 0),
           IFNULL(sum(u.pa_settle_profit), 0),
           IFNULL(sum(u.pa_settle_return), 0),

           IFNULL(sum(u.mts_bet_tickets), 0),
           IFNULL(sum(u.mts_bet_amount), 0),
           IFNULL(sum(u.mts_valid_tickets), 0),
           IFNULL(sum(u.mts_valid_bet_amount), 0),
           IFNULL(sum(u.mts_profit), 0),
           IFNULL(sum(u.mts_return_amount), 0),
           IFNULL(sum(u.mts_bet_amount_settled), 0),
           IFNULL(sum(u.mts_ticket_settled), 0),
           IFNULL(sum(u.mts_failed_bet_amount), 0),
           IFNULL(sum(u.mts_failed_tickets), 0),
           IFNULL(sum(u.mts_settle_tickets), 0),
           IFNULL(sum(u.mts_settle_bet_amount), 0),
           IFNULL(sum(u.mts_settle_profit), 0),
           IFNULL(sum(u.mts_settle_return), 0),
           IFNULL(sum(u.series_bet_tickets), 0),
           IFNULL(sum(u.series_bet_amount), 0),
           IFNULL(sum(u.series_valid_tickets), 0),
           IFNULL(sum(u.series_valid_bet_amount), 0),
           IFNULL(sum(u.series_profit), 0),
           IFNULL(sum(u.series_return_amount), 0),
           IFNULL(sum(u.series_bet_amount_settled), 0),
           IFNULL(sum(u.series_ticket_settled), 0),
           IFNULL(sum(u.series_failed_bet_amount), 0),
           IFNULL(sum(u.series_failed_tickets), 0),
           IFNULL(sum(u.series_settle_tickets), 0),
           IFNULL(sum(u.series_settle_bet_amount), 0),
           IFNULL(sum(u.series_settle_profit), 0),
           IFNULL(sum(u.series_settle_return), 0),
           UNIX_TIMESTAMP(current_timestamp(3)),
           IFNULL(sum(s.order_valid_bet_money), 0),
           IFNULL(sum(s.settle_valid_bet_money), 0)
    from tybss_report.r_user_order_hour u
             left join tybss_report.r_user_order_hour_sub s on u.user_id = s.user_id and u.time = s.time
    where u.time like concat(dayL, '%')
      and u.user_id in (select user_id
                        from tybss_report.r_user_order_hour
                        where time = executetimeL)
    group by u.user_id;


    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',r_user_order_hour(3),success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

CALL p_r_user_order_hour(now());
CALL p_r_user_order_hour(ADDDATE(now(), INTERVAL -1 hour));
