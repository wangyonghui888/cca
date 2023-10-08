ALTER TABLE `tybss_report`.`r_user_sport_order_day`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`r_user_sport_order_day_utc8`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`r_user_order_day`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`r_user_order_day_utc8`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`r_user_order_month`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`r_user_order_month_utc8`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`r_user_sport_order_month`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`r_user_sport_order_month_utc8`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`r_user_order_hour`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';

ALTER TABLE `tybss_report`.`user_order_all`
    ADD COLUMN `order_valid_bet_count` int(6) NULL DEFAULT 0 COMMENT '投注有效笔数';


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
        SET exce_msg = CONCAT('错误码：', result_code, execute_date, ' p_r_user_order_day错误信息：', msg);
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
    SELECT Concat(w.uid, executetimeL)                AS id
         , w.uid                                         uid
         , d.username
         , d.currency_code
         , (CASE WHEN a.rmb_exchange_rate is null THEN c.rmb_exchange_rate else a.rmb_exchange_rate END)
                                                         exchange_rate
         , d.merchant_code                               merchantCode
         , d.merchant_name                            AS merchantName
         , executetimeL                               AS time
         , IFNULL(a.profit, 0)                           profit
         , IFNULL(a.settleAmount, 0)                     returnAmount
         , 0                                          AS profit_rate
         , IFNULL(a.betAmount, 0)                        betAmount
         , IFNULL(a.validBetAmount, 0)                   validBetAmount
         , IFNULL(a.totalTickets, 0)                     totalTickets
         , IFNULL(a.validTickets, 0)                     validTickets
         , IFNULL(a.betAmountSettled, 0)                 betAmountSettled
         , IFNULL(a.tickedSettled, 0)                    ticketSettled
         , IFNULL(c.settleOrderAmount, 0)                settleTotalTickets
         , IFNULL(c.settleBetAmount, 0)                  settleBetAmount
         , IFNULL(c.settleProfit, 0)                  AS settle_profit
         , IFNULL(c.settleAmount, 0)                  AS settle_return
         , 0                                          AS settle_profit_rate
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
        , floor(IFNULL(c.order_valid_bet_count, 0))   as order_valid_bet_count
    FROM (SELECT o.uid
          FROM tybss_merchant_common.t_order o
          WHERE (o.create_time >= startTimeL AND o.create_time <= endTimeL)
          UNION
          SELECT s.uid
          FROM tybss_merchant_common.t_settle s
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
                                           ON o.order_no = s.order_no AND s.last_settle = 1
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
                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(r.order_amount_total) = ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(r.order_amount_total) > ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(r.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(r.order_amount_total / 100) END)           as settle_valid_bet_money
                                                  , Count(CASE WHEN s.out_come in (3, 4, 5, 6) THEN s.order_no END)       AS order_valid_bet_count
                        FROM tybss_merchant_common.t_settle s
                                 left join tybss_merchant_common.t_order r on r.order_no = s.order_no
                        WHERE s.create_time >= startTimeL
                          AND s.create_time <= endTimeL
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
                        FROM tybss_merchant_common.t_user u
                                 LEFT JOIN tybss_merchant_common.t_merchant m ON u.merchant_code = m.merchant_code
                                 left join tybss_merchant_common.t_merchant m1 on m1.id = m.parent_id) d
                       ON w.uid = d.uid;
    commit;

    SET end_time = get_cur_ymdhms();
    SELECT count(*) INTO total FROM tybss_report.r_user_order_day where time = executetimeL;

    SET exce_msg = CONCAT(execute_date, ',p_r_user_order_day:(1)执行成功!共:', total);

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);


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
         , floor(IFNULL(c.order_valid_bet_count, 0)) as  order_valid_bet_count
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
                             , min(o.rmb_exchange_rate)                                              AS rmb_exchange_rate
                             , SUM(s.settle_amount)                                                  AS settleAmount
                             , SUM(s.profit_amount)                                                  AS profit
                             , SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END) AS validBetAmount
                             , SUM(CASE WHEN o.order_status = 1 THEN o.order_amount_total END)       AS betAmountSettled
                             , Count(CASE WHEN o.order_status = 1 THEN o.order_no END)               AS tickedSettled
                             , Count(CASE WHEN o.order_status in (0, 1) THEN o.order_no END)         AS validTickets
                             , SUM(CASE WHEN o.order_status = 4 THEN o.order_amount_total END)       AS failed_bet_amount
                             , Count(CASE WHEN o.order_status = 4 THEN o.order_no END)               AS failed_tickets
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
                             , SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(r.order_amount_total) = ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(r.order_amount_total) > ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(r.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(r.order_amount_total / 100) END)           as settle_valid_bet_money
                                        , Count(CASE WHEN s.out_come in (3, 4, 5, 6) THEN s.order_no END)       AS order_valid_bet_count
                        FROM tybss_merchant_common.t_settle s
                                 left join tybss_merchant_common.t_order r on r.order_no = s.order_no
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
                        FROM tybss_merchant_common.t_user u
                                 LEFT JOIN tybss_merchant_common.t_merchant m ON u.merchant_code = m.merchant_code
                                 left join tybss_merchant_common.t_merchant m1 on m1.id = m.parent_id) d
                       ON w.uid = d.uid;
    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_date, ',p_r_user_order_day:(2),success!');
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

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

DROP PROCEDURE IF EXISTS p_r_user_order_month;
DELIMITER //
CREATE PROCEDURE p_r_user_order_month(in str_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 31;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_user_order_month成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;
    DECLARE executetimeL BIGINT(12);
    DECLARE currentDateTimeL BIGINT(12);
    DECLARE executetimeHourL BIGINT(12);
    DECLARE monthL BIGINT(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, 'p_r_user_order_month错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');

    set executetimeL = DATE_FORMAT(str_date, '%Y%m%d');
    set executetimeHourL = DATE_FORMAT(str_date, '%Y%m%d%H');

    set currentDateTimeL = DATE_FORMAT(CURRENT_DATE(), '%Y%m%d');


    set monthL = DATE_FORMAT(str_date, '%Y%m');

    REPLACE INTO tybss_report.r_user_order_month
    SELECT Concat(t.user_id, substring(t.time, 1, 6))                                                  id,
           t.user_id,
           min(t.user_name),
           min(t.currency_code),
           min(t.exchange_rate),
           min(t.merchant_code),
           min(t.merchant_name),
           substring(t.time, 1, 6)                                                                     time,
           SUM(t.bet_num)                                                                              bet_num,
           SUM(t.valid_tickets)                                                                        valid_tickets,
           SUM(t.bet_amount)                                                                           bet_amount,
           SUM(t.valid_bet_amount)                                                                     valid_bet_amount,
           SUM(t.profit)                                                                               profit,
           SUM(t.return_amount)                                                                        return_amount,
           ifnull(SUM(t.profit) / IF(SUM(t.valid_bet_amount) = '0', NULL, SUM(t.valid_bet_amount)), 0) profit_rate,
           sum(t.settle_order_num)                                                                     settle_order_num,
           SUM(t.settle_order_amount)                                                                  settle_order_amount,
           SUM(t.settle_profit)                                                                        settle_profit,
           ifnull(SUM(t.settle_profit) / IF(SUM(t.settle_order_amount) = '0', NULL, SUM(t.settle_order_amount)),
                  0)                                                                                   settle_profit_rate,
           SUM(t.settle_return)                                                                        settle_return,
           COUNT(CASE WHEN t.bet_amount > 0 THEN t.user_id END) AS                                     active_days,
           UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
           max(t.parent_code)                                   as                                     parent_code,
           max(t.parent_name)                                   as                                     parent_name,
           SUM(t.bet_amount_settled)                                                                   bet_amount_settled,
           SUM(t.ticket_settled)                                                                       ticket_settled,
           SUM(t.settle_expect_profit)                          as                                     settleExpectProfit,
           SUM(t.settle_series_amount)                          as                                     settle_series_amount,
           SUM(t.settle_series_tickets)                         as                                     settle_series_tickets,
           SUM(t.settle_series_profit)                          as                                     settle_series_profit,
           floor(SUM(t.order_valid_bet_money))                  as                                     order_valid_bet_money,
           floor(SUM(t.settle_valid_bet_money))                 as                                     order_valid_bet_money,
           SUM(t.order_valid_bet_count)     as order_valid_bet_count
    FROM r_user_order_day t
    WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
      AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')
      and user_id in (select user_id
                      from r_user_order_day
                      where time = executetimeL
                        and (valid_bet_amount > 0 or settle_profit <> 0))
    GROUP BY t.user_id;

/*    REPLACE INTO tybss_report.r_user_order_month_utc8
    SELECT Concat(t.user_id, monthL)                                                                   id,
           t.user_id,
           min(t.user_name),
           min(t.merchant_code),
           min(t.merchant_name),
           monthL                                                                                      time,
           SUM(t.profit)                                                                               profit,
           SUM(t.return_amount)                                                                        return_amount,
           ifnull(SUM(t.profit) / IF(SUM(t.valid_bet_amount) = '0', NULL, SUM(t.valid_bet_amount)), 0) profit_rate,
           SUM(t.bet_amount)                                                                           bet_amount,
           SUM(t.valid_bet_amount)                                                                     valid_bet_amount,
           SUM(t.bet_num)                                                                              bet_num,
           SUM(t.valid_tickets)                                                                        valid_tickets,
           SUM(t.failed_bet_amount)                                                                    failed_bet_amount,
           SUM(t.failed_tickets)                                                                       failed_tickets,
           sum(t.settle_order_num)                                                                     settle_order_num,
           SUM(t.settle_order_amount)                                                                  settle_order_amount,
           SUM(t.settle_profit)                                                                        settle_profit,
           SUM(t.settle_return)                                                                        settle_return,
           ifnull(SUM(t.settle_profit) / IF(SUM(t.settle_order_amount) = '0', NULL, SUM(t.settle_order_amount)),
                  0)                                                                                   settle_profit_rate,
           COUNT(CASE WHEN t.bet_amount > 0 THEN t.user_id END) AS                                     active_days,
           UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
           max(t.parent_code)                                   as                                     parent_code,
           max(t.parent_name)                                   as                                     parent_name,
           SUM(t.bet_amount_settled)                                                                   bet_amount_settled,
           SUM(t.ticket_settled)                                                                       ticket_settled,
           SUM(t.settle_expect_profit)                          as                                     settleExpectProfit,
           SUM(t.settle_series_amount)                          as                                     settle_series_amount,
           SUM(t.settle_series_tickets)                         as                                     settle_series_tickets,
           SUM(t.settle_series_profit)                          as                                     settle_series_profit
    FROM r_user_order_day_utc8 t
    where t.user_id in (select user_id from r_user_order_hour where time = executetimeL)
      and t.time like concat(monthL, '%')
    GROUP BY t.user_id;*/

    REPLACE INTO tybss_report.r_user_order_month_utc8
    SELECT Concat(t.user_id, substring(t.time, 1, 6))                                                  id,
           t.user_id,
           min(t.user_name),
           min(t.currency_code),
           min(t.exchange_rate),
           min(t.merchant_code),
           min(t.merchant_name),
           substring(t.time, 1, 6)                                                                     time,
           SUM(t.profit)                                                                               profit,
           SUM(t.return_amount)                                                                        return_amount,
           ifnull(SUM(t.profit) / IF(SUM(t.valid_bet_amount) = '0', NULL, SUM(t.valid_bet_amount)), 0) profit_rate,
           SUM(t.bet_amount)                                                                           bet_amount,
           SUM(t.valid_bet_amount)                                                                     valid_bet_amount,
           SUM(t.bet_num)                                                                              bet_num,
           SUM(t.valid_tickets)                                                                        valid_tickets,
           SUM(t.failed_bet_amount)                                                                    failed_bet_amount,
           SUM(t.failed_tickets)                                                                       failed_tickets,
           SUM(t.settle_order_num)                                                                     settle_order_num,
           SUM(t.settle_order_amount)                                                                  settle_order_amount,
           SUM(t.settle_profit)                                                                        settle_profit,
           SUM(t.settle_return)                                                                        settle_return,
           ifnull(SUM(t.settle_profit) / IF(SUM(t.settle_order_amount) = '0', NULL, SUM(t.settle_order_amount)),
                  0)                                                                                   settle_profit_rate,
           COUNT(CASE WHEN t.bet_amount > 0 THEN t.user_id END) AS                                     active_days,
           UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
           max(t.parent_code)                                   as                                     parent_code,
           max(t.parent_name)                                   as                                     parent_name,
           SUM(t.bet_amount_settled)                                                                   bet_amount_settled,
           SUM(t.ticket_settled)                                                                       ticket_settled,
           SUM(t.settle_expect_profit)                          as                                     settleExpectProfit,
           SUM(t.settle_series_amount)                          as                                     settle_series_amount,
           SUM(t.settle_series_tickets)                         as                                     settle_series_tickets,
           SUM(t.settle_series_profit)                          as                                     settle_series_profit,
           floor(SUM(t.order_valid_bet_money))                  as                                     order_valid_bet_money,
           floor(SUM(t.settle_valid_bet_money))                 as                                     order_valid_bet_money
            , SUM(t.order_valid_bet_count)   as order_valid_bet_count
    FROM r_user_order_day_utc8 t
    WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
      AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')
      AND user_id in (select user_id
                      from r_user_order_day_utc8
                      where time = executetimeL
                        and (valid_bet_amount > 0 or settle_profit <> 0))
    GROUP BY t.user_id;

/*
    REPLACE INTO tybss_report.r_user_order_month_utc8_sub
    SELECT Concat(t.user_id, monthL)                          id,
           t.user_id,
           min(t.merchant_code),
           min(t.parent_code),
           monthL                                             'month',
           IFNULL(sum(t.pre_bet_tickets), 0),
           IFNULL(sum(t.pre_bet_amount), 0),
           IFNULL(sum(t.pre_valid_tickets), 0),
           IFNULL(sum(t.pre_valid_bet_amount), 0),
           IFNULL(sum(t.pre_profit), 0),
           IFNULL(sum(t.pre_return_amount), 0),
           IFNULL(sum(t.pre_bet_amount_settled), 0),
           IFNULL(sum(t.pre_ticket_settled), 0),
           IFNULL(sum(t.pre_failed_bet_amount), 0),
           IFNULL(sum(t.pre_failed_tickets), 0),
           IFNULL(sum(t.pre_settle_tickets), 0),
           IFNULL(sum(t.pre_settle_bet_amount), 0),
           IFNULL(sum(t.pre_settle_profit), 0),
           IFNULL(sum(t.pre_settle_return), 0),
           IFNULL(sum(t.live_bet_tickets), 0),
           IFNULL(sum(t.live_bet_amount), 0),
           IFNULL(sum(t.live_valid_tickets), 0),
           IFNULL(sum(t.live_valid_bet_amount), 0),
           IFNULL(sum(t.live_profit), 0),
           IFNULL(sum(t.live_return_amount), 0),
           IFNULL(sum(t.live_bet_amount_settled), 0),
           IFNULL(sum(t.live_ticket_settled), 0),
           IFNULL(sum(t.live_failed_bet_amount), 0),
           IFNULL(sum(t.live_failed_tickets), 0),
           IFNULL(sum(t.live_settle_tickets), 0),
           IFNULL(sum(t.live_settle_bet_amount), 0),
           IFNULL(sum(t.live_settle_profit), 0),
           IFNULL(sum(t.live_settle_return), 0),
           IFNULL(sum(t.pa_bet_tickets), 0),
           IFNULL(sum(t.pa_bet_amount), 0),
           IFNULL(sum(t.pa_valid_tickets), 0),
           IFNULL(sum(t.pa_valid_bet_amount), 0),
           IFNULL(sum(t.pa_profit), 0),
           IFNULL(sum(t.pa_return_amount), 0),
           IFNULL(sum(t.pa_bet_amount_settled), 0),
           IFNULL(sum(t.pa_ticket_settled), 0),
           IFNULL(sum(t.pa_failed_bet_amount), 0),
           IFNULL(sum(t.pa_failed_tickets), 0),
           IFNULL(sum(t.pa_settle_tickets), 0),
           IFNULL(sum(t.pa_settle_bet_amount), 0),
           IFNULL(sum(t.pa_settle_profit), 0),
           IFNULL(sum(t.pa_settle_return), 0),

           IFNULL(sum(t.mts_bet_tickets), 0),
           IFNULL(sum(t.mts_bet_amount), 0),
           IFNULL(sum(t.mts_valid_tickets), 0),
           IFNULL(sum(t.mts_valid_bet_amount), 0),
           IFNULL(sum(t.mts_profit), 0),
           IFNULL(sum(t.mts_return_amount), 0),
           IFNULL(sum(t.mts_bet_amount_settled), 0),
           IFNULL(sum(t.mts_ticket_settled), 0),
           IFNULL(sum(t.mts_failed_bet_amount), 0),
           IFNULL(sum(t.mts_failed_tickets), 0),
           IFNULL(sum(t.mts_settle_tickets), 0),
           IFNULL(sum(t.mts_settle_bet_amount), 0),
           IFNULL(sum(t.mts_settle_profit), 0),
           IFNULL(sum(t.mts_settle_return), 0),

           IFNULL(sum(t.series_bet_tickets), 0),
           IFNULL(sum(t.series_bet_amount), 0),
           IFNULL(sum(t.series_valid_tickets), 0),
           IFNULL(sum(t.series_valid_bet_amount), 0),
           IFNULL(sum(t.series_profit), 0),
           IFNULL(sum(t.series_return_amount), 0),
           IFNULL(sum(t.series_bet_amount_settled), 0),
           IFNULL(sum(t.series_ticket_settled), 0),
           IFNULL(sum(t.series_failed_bet_amount), 0),
           IFNULL(sum(t.series_failed_tickets), 0),
           IFNULL(sum(t.series_settle_tickets), 0),
           IFNULL(sum(t.series_settle_bet_amount), 0),
           IFNULL(sum(t.series_settle_profit), 0),
           IFNULL(sum(t.series_settle_return), 0),
           UNIX_TIMESTAMP(current_timestamp(3)),
           floor(IFNULL(SUM(t.order_valid_bet_money), 0))  as order_valid_bet_money,
           floor(IFNULL(SUM(t.settle_valid_bet_money), 0)) as order_valid_bet_money
    FROM r_user_order_day_utc8_sub t
    WHERE t.time like concat(monthL, '%')
      and t.user_id in (select user_id
                        from r_user_order_day_utc8
                        where time = executetimeL)
    GROUP BY t.user_id;
*/
    if executetimeL = currentDateTimeL then

        update user_order_all tu inner join (select user_id,
                                                    sum(settle_profit)    settle_profit,
                                                    sum(settle_return)    settle_return,
                                                    sum(valid_bet_amount) valid_bet_amount,
                                                    min(exchange_rate)    exchange_rate,
                                                    sum(valid_tickets)    valid_tickets
                                             from r_user_order_month_utc8
                                             where user_id in (select user_id
                                                               from r_user_order_hour
                                                               where time = (executetimeHourL - 1)
                                                                 and (valid_bet_amount > 0 or settle_profit <> 0))
                                             group by user_id) uo on tu.user_id = uo.user_id
        set tu.valid_bet_amount=uo.valid_bet_amount,
            tu.valid_order_num=uo.valid_tickets,
            tu.profit=uo.settle_profit,
            tu.exchange_rate=uo.exchange_rate,
            tu.settle_amount=uo.settle_return;

        update user_order_all tu inner join (select user_id,
                                                    sum(settle_profit)    settle_profit,
                                                    sum(settle_return)    settle_return,
                                                    sum(valid_bet_amount) valid_bet_amount,
                                                    min(exchange_rate)    exchange_rate,
                                                    sum(valid_tickets)    valid_tickets
                                             from r_user_order_month_utc8
                                             where user_id in (select user_id
                                                               from r_user_order_hour
                                                               where time = executetimeHourL
                                                                 and (valid_bet_amount > 0 or settle_profit <> 0))
                                             group by user_id) uo on tu.user_id = uo.user_id
        set tu.valid_bet_amount=uo.valid_bet_amount,
            tu.valid_order_num=uo.valid_tickets,
            tu.profit=uo.settle_profit,
            tu.exchange_rate=uo.exchange_rate,
            tu.settle_amount=uo.settle_return;
    else
        update user_order_all tu inner join (select user_id,
                                                    sum(settle_profit)    settle_profit,
                                                    sum(settle_return)    settle_return,
                                                    sum(valid_bet_amount) valid_bet_amount,
                                                    min(exchange_rate)    exchange_rate,
                                                    sum(valid_tickets)    valid_tickets
                                             from r_user_order_month_utc8
                                             where user_id in (select user_id
                                                               from r_user_order_day_utc8
                                                               where time = executetimeL
                                                                 and (valid_bet_amount > 0 or settle_profit <> 0))
                                             group by user_id) uo on tu.user_id = uo.user_id
        set tu.valid_bet_amount=uo.valid_bet_amount,
            tu.valid_order_num=uo.valid_tickets,
            tu.profit=uo.settle_profit,
            tu.exchange_rate=uo.exchange_rate,
            tu.settle_amount=uo.settle_return;
    end if;


/*    UPDATE tybss_merchant_common.t_user tu inner join (select user_id,
                                                  sum(settle_profit)       settle_profit,
                                                  sum(valid_bet_amount)    valid_bet_amount,
                                                  sum(valid_tickets)       valid_tickets,
                                                  sum(settle_order_amount) settledBetAmount
                                           from r_user_order_month_utc8
                                           where user_id in (select user_id
                                                             from r_user_order_day_utc8
                                                             where time = executetimeL
                                                               and (valid_bet_amount > 0 or settle_profit <> 0))
                                           group by user_id) uo on tu.uid = uo.user_id
    set tu.bet_amount=uo.valid_bet_amount,
        tu.total_tickets=uo.valid_tickets,
        tu.settled_bet_amount=uo.settledBetAmount,
        tu.profit=uo.settle_profit;*/

    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, 'p_r_user_order_month success!', executetimeL);

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

DROP PROCEDURE IF EXISTS p_r_user_sport_order_month;
DELIMITER //
CREATE PROCEDURE p_r_user_sport_order_month(in str_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 36;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_user_sport_order_month成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(str_date, '错误码：', result_code, 'p_r_user_sport_order_month错误信息:', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');


    REPLACE INTO tybss_report.r_user_sport_order_month
    SELECT Concat(t.user_id, t.sport_id, t.MONTH)                  id,
           t.sport_id,
           t.user_id,
           min(t.user_name),
           min(t.currency_code),
           min(t.exchange_rate),
           min(t.merchant_code),
           min(t.merchant_name),
           t.MONTH                                                 time,
           SUM(t.bet_num)                                          bet_num,
           SUM(t.bet_amount)                                       bet_amount,
           SUM(t.profit)                                           profit,
           0                                                       profit_rate,
           sum(t.settle_order_num)                                 settle_order_num,
           SUM(t.settle_order_amount)                              settle_order_amount,
           SUM(t.settle_profit)                                    settle_profit,
           0                                                       settle_profit_rate,
           SUM(t.settle_return)                                    settle_return,
           COUNT(CASE WHEN t.bet_amount > 0 THEN t.user_id END) AS active_days,
           UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
           max(t.parent_code)                                   as parent_code,
           max(t.parent_name)                                   as parent_name,
           SUM(t.bet_amount_settled)                               bet_amount_settled,
           SUM(t.ticket_settled)                                   ticket_settled,
           floor(SUM(t.order_valid_bet_money))                  as order_valid_bet_money,
           floor(SUM(t.settle_valid_bet_money))                 as order_valid_bet_money
        , SUM(t.order_valid_bet_count)   as order_valid_bet_count
    FROM (SELECT *,
                 substring(time, 1, 6) AS 'month'
          FROM r_user_sport_order_day
          WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
            AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')
            and user_id is not null) t
    GROUP BY t.user_id, t.MONTH, t.sport_id;


    REPLACE INTO tybss_report.r_user_sport_order_month_utc8
    SELECT Concat(t.user_id, t.sport_id, t.MONTH)                  id,
           t.sport_id,
           t.user_id,
           min(t.user_name),
           min(t.currency_code),
           min(t.exchange_rate),
           min(t.merchant_code),
           min(t.merchant_name),
           t.MONTH                                                 time,
           SUM(t.bet_num)                                          bet_num,
           SUM(t.bet_amount)                                       bet_amount,
           sum(t.valid_tickets)                                    valid_tickets,
           sum(t.valid_bet_amount)                                 valid_bet_amount,
           SUM(t.profit)                                           profit,
           0                                                       profit_rate,
           sum(t.settle_order_num)                                 settle_order_num,
           SUM(t.settle_order_amount)                              settle_order_amount,
           SUM(t.settle_profit)                                    settle_profit,
           0                                                       settle_profit_rate,
           SUM(t.settle_return)                                    settle_return,
           COUNT(CASE WHEN t.bet_amount > 0 THEN t.user_id END) AS active_days,
           UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
           max(t.parent_code)                                   as parent_code,
           max(t.parent_name)                                   as parent_name,
           SUM(t.bet_amount_settled)                               bet_amount_settled,
           SUM(t.ticket_settled)                                   ticket_settled,
           sum(t.failed_bet_amount)                                failed_bet_amount,
           sum(t.failed_tickets)                                   failed_tickets,
           floor(SUM(t.order_valid_bet_money))                  as order_valid_bet_money,
           floor(SUM(t.settle_valid_bet_money))                 as order_valid_bet_money
             , SUM(t.order_valid_bet_count)   as order_valid_bet_count
    FROM (SELECT *,
                 substring(time, 1, 6) AS 'month'
          FROM r_user_sport_order_day_utc8
          WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
            AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')
            and user_id is not null) t
    GROUP BY t.user_id, t.MONTH, t.sport_id;
/*
    REPLACE INTO tybss_report.r_user_sport_order_month_utc8_sub
    SELECT Concat(t.user_id, t.sport_id, t.MONTH) id,
           t.sport_id,
           t.user_id,
           min(t.merchant_code),
           min(t.parent_code),
           t.MONTH AS                             'month',
           IFNULL(sum(t.pre_bet_tickets), 0),
           IFNULL(sum(t.pre_bet_amount), 0),
           IFNULL(sum(t.pre_valid_tickets), 0),
           IFNULL(sum(t.pre_valid_bet_amount), 0),
           IFNULL(sum(t.pre_profit), 0),
           IFNULL(sum(t.pre_return_amount), 0),
           IFNULL(sum(t.pre_bet_amount_settled), 0),
           IFNULL(sum(t.pre_ticket_settled), 0),
           IFNULL(sum(t.pre_failed_bet_amount), 0),
           IFNULL(sum(t.pre_failed_tickets), 0),
           IFNULL(sum(t.pre_settle_tickets), 0),
           IFNULL(sum(t.pre_settle_bet_amount), 0),
           IFNULL(sum(t.pre_settle_profit), 0),
           IFNULL(sum(t.pre_settle_return), 0),
           IFNULL(sum(t.live_bet_tickets), 0),
           IFNULL(sum(t.live_bet_amount), 0),
           IFNULL(sum(t.live_valid_tickets), 0),
           IFNULL(sum(t.live_valid_bet_amount), 0),
           IFNULL(sum(t.live_profit), 0),
           IFNULL(sum(t.live_return_amount), 0),
           IFNULL(sum(t.live_bet_amount_settled), 0),
           IFNULL(sum(t.live_ticket_settled), 0),
           IFNULL(sum(t.live_failed_bet_amount), 0),
           IFNULL(sum(t.live_failed_tickets), 0),
           IFNULL(sum(t.live_settle_tickets), 0),
           IFNULL(sum(t.live_settle_bet_amount), 0),
           IFNULL(sum(t.live_settle_profit), 0),
           IFNULL(sum(t.live_settle_return), 0),
           IFNULL(sum(t.pa_bet_tickets), 0),
           IFNULL(sum(t.pa_bet_amount), 0),
           IFNULL(sum(t.pa_valid_tickets), 0),
           IFNULL(sum(t.pa_valid_bet_amount), 0),
           IFNULL(sum(t.pa_profit), 0),
           IFNULL(sum(t.pa_return_amount), 0),
           IFNULL(sum(t.pa_bet_amount_settled), 0),
           IFNULL(sum(t.pa_ticket_settled), 0),
           IFNULL(sum(t.pa_failed_bet_amount), 0),
           IFNULL(sum(t.pa_failed_tickets), 0),
           IFNULL(sum(t.pa_settle_tickets), 0),
           IFNULL(sum(t.pa_settle_bet_amount), 0),
           IFNULL(sum(t.pa_settle_profit), 0),
           IFNULL(sum(t.pa_settle_return), 0),
           IFNULL(sum(t.mts_bet_tickets), 0),
           IFNULL(sum(t.mts_bet_amount), 0),
           IFNULL(sum(t.mts_valid_tickets), 0),
           IFNULL(sum(t.mts_valid_bet_amount), 0),
           IFNULL(sum(t.mts_profit), 0),
           IFNULL(sum(t.mts_return_amount), 0),
           IFNULL(sum(t.mts_bet_amount_settled), 0),
           IFNULL(sum(t.mts_ticket_settled), 0),
           IFNULL(sum(t.mts_failed_bet_amount), 0),
           IFNULL(sum(t.mts_failed_tickets), 0),
           IFNULL(sum(t.mts_settle_tickets), 0),
           IFNULL(sum(t.mts_settle_bet_amount), 0),
           IFNULL(sum(t.mts_settle_profit), 0),
           IFNULL(sum(t.mts_settle_return), 0),
           UNIX_TIMESTAMP(current_timestamp(3))
    FROM (SELECT *,
                 substring(time, 1, 6) AS 'month'
          FROM r_user_sport_order_day_utc8_sub
          WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
            AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')) t
    GROUP BY t.user_id, t.MONTH, t.sport_id;
*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, 'p_r_user_sport_order_month_utc8 success!');

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
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
    SELECT Concat(user_id, dayL)                         AS id
         , user_id
         , min(user_name)
         , '1'
         , 1.0
         , min(merchant_code)
         , min(merchant_name)                            AS merchantName
         , dayL                                          AS time
         , IFNULL(sum(profit), 0)                           profit
         , IFNULL(sum(return_amount), 0)                    returnAmount
         , 0                                             AS profit_rate
         , IFNULL(sum(bet_amount), 0)                       betAmount
         , IFNULL(sum(valid_bet_amount), 0)                 validBetAmount
         , IFNULL(sum(bet_tickets), 0)                      totalTickets
         , IFNULL(sum(valid_tickets), 0)                    validTickets
         , IFNULL(sum(failed_bet_amount), 0)                failed_bet_amount
         , IFNULL(sum(failed_tickets), 0)                   failed_tickets
         , IFNULL(sum(bet_amount_settled), 0)               betAmountSettled
         , IFNULL(sum(ticket_settled), 0)                   ticketSettled
         , IFNULL(sum(settle_tickets), 0)                   settleTotalTickets
         , IFNULL(sum(settle_bet_amount), 0)                settleBetAmount
         , IFNULL(sum(settle_profit), 0)                 AS settleProfit
         , IFNULL(sum(settle_return), 0)                 AS settleReturn
         , sum(settle_profit) / IF(sum(settle_bet_amount) = '0', NULL, sum(settle_bet_amount))
                                                         AS settleProfitRate
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3))          AS updatedTime
         , yearL                                         AS 'year'
         , min(parent_code)
         , min(parent_name)
         , IFNULL(sum(settle_expect_profit), 0)          as settleExpectProfit
         , IFNULL(sum(series_settle_bet_amount), 0)      as settle_series_amount
         , IFNULL(sum(series_settle_tickets), 0)         as settle_series_tickets
         , IFNULL(sum(series_settle_profit), 0)          as settle_series_profit

         , floor(IFNULL(sum(order_valid_bet_money), 0))  as order_valid_bet_money
         , floor(IFNULL(sum(settle_valid_bet_money), 0)) as settle_valid_bet_money
         , floor(IFNULL(sum(order_valid_bet_count), 0)) as order_valid_bet_count
    from tybss_report.r_user_order_hour
    where time like concat(dayL, '%')
      and user_id in (select user_id from tybss_report.r_user_order_hour where time = executetimeL)
    group by user_id;


    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',r_user_order_hour(2),success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

CALL p_r_user_order_hour(now());
CALL p_r_user_order_hour(ADDDATE(now(), INTERVAL -1 hour));
