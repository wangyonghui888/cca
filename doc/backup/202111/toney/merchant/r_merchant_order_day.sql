DROP PROCEDURE IF EXISTS p_r_merchant_order_day;

DELIMITER //
CREATE
    PROCEDURE `p_r_merchant_order_day`(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 13;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_merchant_order_day成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);
    DECLARE endTimeL BIGINT(16);
    DECLARE startTimeL BIGINT(16);
    DECLARE total INT(10) DEFAULT 0;
    DECLARE yearL BIGINT(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, execute_date, ' p_r_merchant_order_day错误信息：', msg);
        SET result = 2;
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END;

    set executetimeL = DATE_FORMAT(execute_date, '%Y%m%d');
    set startTimeL = unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000;
    set endTimeL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1);
    set yearL = DATE_FORMAT(execute_date, '%Y');

    REPLACE INTO r_merchant_order_day
SELECT Concat(a.merchant_code, executetimeL)                                                    AS id
     , a.merchant_code
     , executetimeL                                                                             AS time
         , d.merchant_name                                                                          AS merchantName
         , d.merchantLevel
         , b.newUser
         , b.allUser
         , IFNULL(Round(a.betUsers / b.allUser, 4), 0)                                              AS bet_user_rate
         , IFNULL(a.profit, 0)                                                                         profit
         , IFNULL(Round(a.profit / IF(a.betAmountSettled = '0', NULL, a.betAmountSettled), 4), 0)   AS profit_rate
         , IFNULL(a.returnAmount, 0)                                                                AS betSettleAmount
         , IFNULL(Round(a.returnAmount / IF(a.validBetAmount = '0', NULL, a.validBetAmount), 4), 0) AS return_rate
         , IFNULL(a.totalBetAmount, 0)                                                              as totalBetAmount
         , IFNULL(a.validBetAmount, 0)                                                              as validBetAmount
         , IFNULL(a.totalBetAmount, 0) - IFNULL(a.validBetAmount, 0)                                as filedBetAmount

         , IFNULL(a.betAmountSettled, 0)                                                            as betAmountSettled
         , IFNULL(a.tickedSettled, 0)                                                               as tickedSettled

         , IFNULL(a.totalTickets, 0)                                                                   betTotalTickets
         , IFNULL(a.validTickets, 0)                                                                   validTickets
         , IFNULL(a.totalTickets, 0) - IFNULL(a.validTickets, 0)                                       filedTickets
         , ifNull(a.betUsers, 0)                                                                    as totalBetUser
         , ifNull(a.validUser, 0)                                                                   as validUser
         , ifNull(e.firstBetUsers, 0)                                                                  firstBetUsers
         , IFNULL(Round(a.settleUserAmount / b.allUser, 4), 0)                                      AS settle_user_rate
         , IFNULL(a.settleProfit, 0)                                                                AS settle_profit
         , IFNULL(a.settleAmount, 0)                                                                AS settle_return
         , IFNULL(Round(a.settleAmount / IF(a.settleBetAmount = '0', NULL, a.settleBetAmount), 4),
                  0)                                                                                AS settle_return_rate
         , IFNULL(a.settleBetAmount, 0)                                                                settleBetAmount
         , IFNULL(a.settleTotalTickets, 0)                                                             settleTotalTickets
         , UNIX_TIMESTAMP(current_timestamp(3))                                                     AS elasticsearch_id
         , yearL                                                                                    AS 'year'
         , IFNULL(a.activeUser, 0)                                                                  as active_user
         , d.parent_code
         , d.parent_name
         , IFNULL(a.settleUserAmount, 0)                                                            AS settleUsers
         , IFNULL(a.betSettleUsers, 0)                                                                 betSettleUsers
         , d.agentLevel                                                                                agent_level
         , IFNULL(a.betUnsettleUsers, 0)                                                               betUnsettleUsers
         , IFNULL(a.betFailedUsers, 0)                                                                 betFailedUsers
         , IFNULL(a.settleExpectProfit, 0)                                                          as settleExpectProfit
         , IFNULL(a.settle_series_amount, 0)                                                        as settle_series_amount
         , IFNULL(a.settle_series_tickets, 0)                                                       as settle_series_tickets
         , IFNULL(a.settle_series_users, 0)                                                         as settle_series_users
         , IFNULL(a.settle_series_profit, 0)                                                        as settle_series_profit

         , IFNULL(a.order_valid_bet_money, 0)                                                       as order_valid_bet_money
         , IFNULL(a.settle_valid_bet_money, 0)                                                      as settle_valid_bet_money
FROM (select ru.merchant_code,
    sum(ru.bet_num)                                                     totalTickets,
    sum(ru.valid_tickets)                                               validTickets,
    sum(ru.bet_amount_settled)                                       AS betAmountSettled,
    sum(ru.ticket_settled)                                           AS tickedSettled,
    Count(CASE WHEN ru.bet_amount > 0 THEN ru.user_id END)           AS betUsers,
    Count(CASE WHEN ru.valid_bet_amount > 0 THEN ru.user_id END)     AS validUser,
    Count(CASE WHEN ru.bet_amount_settled > 0 THEN ru.user_id END)   AS betSettleUsers,
    Count(CASE
    WHEN (ru.valid_bet_amount - ru.bet_amount_settled) > 0
    THEN ru.user_id END)                               AS betUnsettleUsers,
    Count(CASE
    WHEN (ru.bet_amount - ru.valid_bet_amount) > 0
    THEN ru.user_id END)                               AS betFailedUsers,
    sum(ru.bet_amount)                                                  totalBetAmount,
    sum(ru.valid_bet_amount)                                            validBetAmount,
    sum(ru.return_amount)                                               returnAmount,
    sum(ru.profit) * -1                                                 profit,
    sum(ru.settle_order_num)                                         AS settleTotalTickets,
    COUNT(CASE WHEN ru.settle_order_amount > 0 THEN ru.user_id END)  AS settleUserAmount,
    SUM(ru.settle_return)                                            AS settleAmount,
    SUM(ru.settle_profit) * -1                                       AS settleProfit,
    SUM(ru.settle_order_amount)                                      AS settleBetAmount,
    COUNT(CASE WHEN ru.bet_amount > 500000 THEN ru.user_id END)      AS activeUser,
    sum(ru.settle_expect_profit)                                     as settleExpectProfit,
    sum(ru.settle_series_amount)                                     as settle_series_amount,
    sum(ru.settle_series_tickets)                                    as settle_series_tickets,
    sum(ru.settle_series_profit)                                     as settle_series_profit,
    Count(CASE WHEN ru.settle_series_amount > 0 THEN ru.user_id END) as settle_series_users,

    sum(ru.order_valid_bet_money)                                    as order_valid_bet_money,
    sum(ru.settle_valid_bet_money)                                   as settle_valid_bet_money

    from tybss_report.r_user_order_day ru
    where ru.time = executetimeL
    and ru.merchant_code is not null
    group by ru.merchant_code) a
    left join (select COUNT(CASE WHEN u.register_time <= endTimeL THEN u.register_time END) AS allUser,
    COUNT(CASE
    WHEN u.register_time >= startTimeL AND u.register_time <= endTimeL
    THEN u.register_time END)                               AS newUser,
    u.merchant_code
    from tybss_report.user_order_all u
    GROUP BY u.merchant_code) b ON a.merchant_code = b.merchant_code
    LEFT JOIN (SELECT m.merchant_code
    , m.merchant_name
    , pm.merchant_name   parent_name
    , pm.merchant_code   parent_code
    , MIN(m.`level`) AS  merchantLevel
    , MIN(m.agent_level) agentLevel
    FROM tybss_new.t_merchant m
    left join tybss_new.t_merchant pm on pm.id = m.parent_id
    GROUP BY m.merchant_code) d ON a.merchant_code = d.merchant_code
    LEFT JOIN (SELECT COUNT(h.user_id) AS firstBetUsers, h.merchant_code
    FROM (SELECT ru.user_id, ru.merchant_code
    FROM tybss_report.r_user_order_day ru
    where ru.time = executetimeL) h
    WHERE (h.user_id, h.merchant_code) NOT IN
    (SELECT uo.user_id, uo.merchant_code
    FROM tybss_report.user_order_all uo
    WHERE uo.first_bet_date < execute_date)
    GROUP BY h.merchant_code) e ON a.merchant_code = e.merchant_code;

REPLACE INTO r_merchant_order_day
SELECT Concat(parent_code, time)                                                           id,
       parent_code,
    time,
    min(parent_name),
    0,
    sum(add_user)                                                                       add_user,
    sum(register_total_user_sum)                                                        register_total_user_sum,
    sum(bet_user_sum) / sum(register_total_user_sum)                                    bet_user_rate,
    sum(profit)                                                                         profit,
    sum(profit) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))          profit_rate,
    sum(return_amount)                                                                  return_amount,
    sum(return_amount) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))   return_rate,
    sum(bet_amount)                                                                     bet_amount,
    sum(valid_bet_amount)                                                               valid_bet_amount,
    sum(filed_bet_amount)                                                               filed_bet_amount,
    sum(bet_amount_settled)                                                             bet_amount_settled,
    sum(ticket_settled)                                                                 ticket_settled,
    sum(order_sum)                                                                      total_tickets,
    sum(valid_tickets)                                                                  valid_tickets,
    sum(filed_tickets)                                                                  filed_tickets,
    sum(bet_user_sum)                                                                   bet_users,
    sum(valid_bet_users)                                                                valid_bet_users,
    sum(first_bet_user_sum)                                                             first_bet_user_sum,
    sum(settle_users) / sum(register_total_user_sum)                                    settle_user_rate,
    sum(settle_profit)                                                                  settle_profit,
    sum(settle_return)                                                                  settle_return,
    sum(settle_return) / IF(sum(settle_bet_amount) = '0', NULL, sum(settle_bet_amount)) settle_return_rate,
    sum(settle_bet_amount)                                                              settle_bet_amount,
    sum(settle_order_num)                                                               settle_order_num,
    UNIX_TIMESTAMP(current_timestamp(3)),
    yearL,
    sum(active_user)                                                                    active_user,
    null,
    null,
    sum(settle_users)                                                                   settle_users,
    sum(bet_settled_users)                                                              bet_settled_users,
    1,
    sum(bet_unsettled_users)                                                            bet_unsettled_users,
    sum(bet_failed_users)                                                               bet_failed_users,
    sum(settle_expect_profit)  as                                                       settleExpectProfit,
    sum(settle_series_amount)  as                                                       settle_series_amount,
    sum(settle_series_tickets) as                                                       settle_series_tickets,
    sum(settle_series_users)   as                                                       settle_series_users,
    sum(settle_series_profit)  as                                                       settle_series_profit,
    sum(order_valid_bet_money)                                                       as order_valid_bet_money,
    sum(settle_valid_bet_money)                                                      as settle_valid_bet_money
FROM `r_merchant_order_day`
where parent_code is not null
  and time = executetimeL
group by parent_code, time;
SET end_time = get_cur_ymdhms();
SELECT count(*) INTO total FROM tybss_report.r_merchant_order_day where time = executetimeL;

SET exce_msg = CONCAT(execute_date, ',p_r_merchant_order_day(1)执行成功!共:', total);

CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);


REPLACE INTO r_merchant_order_day_utc8
SELECT Concat(a.merchant_code, executetimeL)                                                    AS id
     , a.merchant_code
     , executetimeL                                                                             AS time
         , d.merchant_name                                                                          AS merchantName
         , d.merchantLevel
         , b.newUser
         , b.allUser
         , IFNULL(Round(a.userAmount / b.allUser, 4), 0)                                            AS bet_user_rate
         , IFNULL(a.profit, 0)                                                                         profit
         , IFNULL(Round(a.profit / IF(a.betAmountSettled = '0', NULL, a.betAmountSettled), 4), 0)   AS profit_rate
         , IFNULL(a.settleAmount, 0)                                                                AS betSettleAmount
         , IFNULL(Round(a.settleAmount / IF(a.totalBetAmount = '0', NULL, a.totalBetAmount), 4), 0) AS return_rate
         , IFNULL(a.totalBetAmount, 0)                                                              as totalBetAmount
         , IFNULL(a.validBetAmount, 0)                                                              as validBetAmount
         , IFNULL(a.failed_bet_amount, 0)                                                           as filedBetAmount
         , IFNULL(a.betAmountSettled, 0)                                                            as betAmountSettled
         , IFNULL(a.tickedSettled, 0)                                                               as tickedSettled
         , IFNULL(a.totalTickets, 0)                                                                as betTotalTickets
         , IFNULL(a.validTickets, 0)                                                                as validTickets
         , IFNULL(a.failed_tickets, 0)                                                              as filedTickets
         , ifNull(a.userAmount, 0)                                                                  as totalBetUser
         , ifNull(a.validUser, 0)                                                                   as validUser
         , ifNull(e.firstBetUsers, 0)                                                               as firstBetUsers
         , IFNULL(Round(a.settleUserAmount / b.allUser, 4), 0)                                      AS settle_user_rate
         , IFNULL(a.settleProfit, 0)                                                                AS settle_profit
         , IFNULL(a.settleAmount, 0)                                                                AS settle_return
         , IFNULL(Round(a.settleAmount / IF(a.settleBetAmount = '0', NULL, a.settleBetAmount), 4),
                  0)                                                                                AS settle_return_rate
         , IFNULL(a.settleBetAmount, 0)                                                             as settleBetAmount
         , IFNULL(a.settleTotalTickets, 0)                                                          as settleTotalTickets
         , UNIX_TIMESTAMP(current_timestamp(3))                                                     AS elasticsearch_id
         , yearL                                                                                    AS 'year'
         , IFNULL(a.activeUser, 0)                                                                  as active_user
         , d.parent_code
         , d.parent_name
         , IFNULL(a.settleUserAmount, 0)                                                            AS settleUsers
         , IFNULL(a.betSettleUsers, 0)                                                                 betSettleUsers
         , d.agentLevel                                                                                agentLevel
         , IFNULL(a.betUnsettleUsers, 0)                                                               betUnsettleUsers
         , IFNULL(a.betFailedUsers, 0)                                                                 betFailedUsers
         , IFNULL(a.settleExpectProfit, 0)                                                          as settleExpectProfit
         , IFNULL(a.settle_series_amount, 0)                                                        as settle_series_amount
         , IFNULL(a.settle_series_tickets, 0)                                                       as settle_series_tickets
         , IFNULL(a.settle_series_users, 0)                                                         as settle_series_users
         , IFNULL(a.settle_series_profit, 0)                                                        as settle_series_profit


         , IFNULL(a.order_valid_bet_money, 0)                                                       as order_valid_bet_money
         , IFNULL(a.settle_valid_bet_money, 0)                                                      as settle_valid_bet_money
FROM (select ru.merchant_code,
                sum(ru.bet_num)                                                  AS totalTickets,
                sum(ru.valid_tickets)                                            AS validTickets,
                sum(ru.bet_amount_settled)                                       AS betAmountSettled,
                sum(ru.ticket_settled)                                           AS tickedSettled,
                Count(CASE WHEN ru.bet_amount > 0 THEN ru.user_id END)           AS userAmount,
                Count(CASE WHEN ru.valid_bet_amount > 0 THEN ru.user_id END)     AS validUser,
                Count(CASE WHEN ru.settle_order_amount > 0 THEN ru.user_id END)   AS betSettleUsers,
                Count(CASE
                WHEN (ru.valid_bet_amount - ru.bet_amount_settled) > 0
                THEN ru.user_id END)                               AS betUnsettleUsers,
                Count(CASE
                WHEN (ru.bet_amount - ru.valid_bet_amount) > 0
                THEN ru.user_id END)                               AS betFailedUsers,
                sum(ru.bet_amount)                                               AS totalBetAmount,
                sum(ru.valid_bet_amount)                                         AS validBetAmount,
                sum(ru.failed_bet_amount)                                        AS failed_bet_amount,
                sum(ru.failed_tickets)                                           AS failed_tickets,
                sum(ru.return_amount)                                            AS returnAmount,
                sum(ru.profit) * -1                                              AS profit,
                sum(ru.settle_order_num)                                         AS settleTotalTickets,
                COUNT(CASE WHEN ru.settle_order_amount > 0 THEN ru.user_id END)  AS settleUserAmount,
                SUM(ru.settle_return)                                            AS settleAmount,
                SUM(ru.settle_profit) * -1                                       AS settleProfit,
                SUM(ru.settle_order_amount)                                      AS settleBetAmount,
                COUNT(CASE WHEN ru.bet_amount > 500000 THEN ru.user_id END)      AS activeUser,
                sum(ru.settle_expect_profit)                                     as settleExpectProfit,
                sum(ru.settle_series_amount)                                     as settle_series_amount,
                sum(ru.settle_series_tickets)                                    as settle_series_tickets,
                sum(ru.settle_series_profit)                                     as settle_series_profit,
                Count(CASE WHEN ru.settle_series_amount > 0 THEN ru.user_id END) as settle_series_users,
                sum(ru.order_valid_bet_money)                                    as order_valid_bet_money,
                sum(ru.settle_valid_bet_money)                                   as settle_valid_bet_money
    from tybss_report.r_user_order_day_utc8 ru
    where ru.time = executetimeL
    and ru.merchant_code is not null
    group by ru.merchant_code) a
    left join (select COUNT(CASE WHEN u.register_time <= endTimeL THEN u.register_time END) AS allUser,
    COUNT(CASE
    WHEN u.register_time >= startTimeL AND u.register_time <= endTimeL
    THEN u.register_time END)                               AS newUser,
    u.merchant_code
    from user_order_all u
    GROUP BY u.merchant_code) b ON a.merchant_code = b.merchant_code
    LEFT JOIN (SELECT m.merchant_code
    , m.merchant_name
    , pm.merchant_name   parent_name
    , pm.merchant_code   parent_code
    , MIN(m.`level`) AS  merchantLevel
    , MIN(m.agent_level) agentLevel
    FROM tybss_new.t_merchant m
    left join tybss_new.t_merchant pm on pm.id = m.parent_id
    GROUP BY m.merchant_code) d ON a.merchant_code = d.merchant_code
    LEFT JOIN (SELECT COUNT(h.user_id) AS firstBetUsers, h.merchant_code
    FROM (SELECT ru.user_id, ru.merchant_code
    FROM tybss_report.r_user_order_day_utc8 ru
    where ru.time = executetimeL) h
    WHERE (h.user_id, h.merchant_code) NOT IN
    (SELECT uo.user_id, uo.merchant_code
    FROM tybss_report.user_order_all uo
    WHERE uo.first_bet_date < execute_date)
    GROUP BY h.merchant_code) e ON a.merchant_code = e.merchant_code;

REPLACE INTO r_merchant_order_day_utc8
SELECT Concat(parent_code, time)                                                           id,
       parent_code,
    time,
    min(parent_name),
    0,
    sum(add_user)                                                                       add_user,
    sum(register_total_user_sum)                                                        register_total_user_sum,
    sum(bet_users) / sum(register_total_user_sum)                                       bet_user_rate,
    sum(profit)                                                                         profit,
    sum(profit) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))          profit_rate,
    sum(return_amount)                                                                  return_amount,
    sum(return_amount) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))   return_rate,
    sum(bet_amount)                                                                     bet_amount,
    sum(valid_bet_amount)                                                               valid_bet_amount,
    sum(filed_bet_amount)                                                               filed_bet_amount,
    sum(bet_amount_settled)                                                             bet_amount_settled,
    sum(ticket_settled)                                                                 ticket_settled,
    sum(total_tickets)                                                                  total_tickets,
    sum(valid_tickets)                                                                  valid_tickets,
    sum(filed_tickets)                                                                  filed_tickets,
    sum(bet_users)                                                                      bet_users,
    sum(valid_bet_users)                                                                valid_bet_users,
    sum(first_bet_user_sum)                                                             first_bet_user_sum,
    sum(settle_users) / sum(register_total_user_sum)                                    settle_user_rate,
    sum(settle_profit)                                                                  settle_profit,
    sum(settle_return)                                                                  settle_return,
    sum(settle_return) / IF(sum(settle_bet_amount) = '0', NULL, sum(settle_bet_amount)) settle_return_rate,
    sum(settle_bet_amount)                                                              settle_bet_amount,
    sum(settle_order_num)                                                               settle_order_num,
    UNIX_TIMESTAMP(current_timestamp(3)),
    yearL,
    sum(active_user)                                                                    active_user,
    null,
    null,
    sum(settle_users)                                                                   settle_users,
    sum(bet_settled_users)                                                              bet_settled_users,
    1,
    sum(bet_unsettled_users)                                                            bet_unsettled_users,
    sum(bet_failed_users)                                                               bet_failed_users,
    sum(settle_expect_profit)  as                                                       settleExpectProfit,
    sum(settle_series_amount)  as                                                       settle_series_amount,
    sum(settle_series_tickets) as                                                       settle_series_tickets,
    sum(settle_series_users)   as                                                       settle_series_users,
    sum(settle_series_profit)  as                                                       settle_series_profit,
    sum(order_valid_bet_money)                                                       as order_valid_bet_money,
    sum(settle_valid_bet_money)                                                      as settle_valid_bet_money
FROM `r_merchant_order_day_utc8`
where parent_code is not null
  and time = executetimeL
group by parent_code, time;

REPLACE INTO r_merchant_order_day_utc8_sub
SELECT Concat(a.merchant_code, executetimeL) AS id
     , a.merchant_code
     , d.merchant_name                       AS merchantName
     , d.parent_code
     , d.parent_name
     , executetimeL                          AS time
         , d.agentLevel
         , d.merchantLevel
         , a.pre_bet_tickets
         , a.pre_bet_users
         , pra_bet_amount
         , pre_valid_tickets
         , pre_valid_bet_amount
         , pre_profit
         , pre_return_amount
         , pre_bet_amount_settled
         , pre_ticket_settled
         , pre_failed_bet_amount
         , pre_failed_tickets
         , pre_settle_tickets
         , pre_settle_bet_amount
         , pre_settle_profit
         , pre_settle_return
         , a.pre_settle_users
         , a.live_bet_users
         , live_bet_tickets
         , live_bet_amount
         , live_valid_tickets
         , live_valid_bet_amount
         , live_profit
         , live_return_amount
         , live_bet_amount_settled
         , live_ticket_settled
         , live_failed_bet_amount
         , live_failed_tickets
         , live_settle_tickets
         , live_settle_bet_amount
         , live_settle_profit
         , live_settle_return
         , a.live_settle_users
         , a.pa_bet_users
         , pa_bet_tickets
         , pa_bet_amount
         , pa_valid_tickets
         , pa_valid_bet_amount
         , pa_profit
         , pa_return_amount
         , pa_bet_amount_settled
         , pa_ticket_settled
         , pa_failed_bet_amount
         , pa_failed_tickets
         , pa_settle_tickets
         , pa_settle_bet_amount
         , pa_settle_profit
         , pa_settle_return
         , a.pa_settle_users
         , a.mts_bet_users
         , mts_bet_tickets
         , mts_bet_amount
         , mts_valid_tickets
         , mts_valid_bet_amount
         , mts_profit
         , mts_return_amount
         , mts_bet_amount_settled
         , mts_ticket_settled
         , mts_failed_bet_amount
         , mts_failed_tickets
         , mts_settle_tickets
         , mts_settle_bet_amount
         , mts_settle_profit
         , mts_settle_return
         , mts_settle_users
         , a.series_bet_users
         , series_bet_tickets
         , series_bet_amount
         , series_valid_tickets
         , series_valid_bet_amount
         , series_profit
         , series_return_amount
         , series_bet_amount_settled
         , series_ticket_settled
         , series_failed_bet_amount
         , series_failed_tickets
         , series_settle_tickets
         , series_settle_bet_amount
         , series_settle_profit
         , series_settle_return
         , a.series_settle_users
         , a.single_bet_users
         , a.single_settle_users
         , UNIX_TIMESTAMP(current_timestamp(3))
         , order_valid_bet_money
         , settle_valid_bet_money
FROM (select ru.merchant_code,
        sum(ru.pre_bet_tickets)                                                        pre_bet_tickets,
        Count(CASE WHEN ru.pre_bet_tickets > 0 THEN ru.user_id END)                 AS pre_bet_users,
        sum(ru.pre_bet_amount)                                                         pra_bet_amount,
        sum(ru.pre_valid_tickets)                                                      pre_valid_tickets,
        sum(ru.pre_valid_bet_amount)                                                   pre_valid_bet_amount,
        sum(ru.pre_profit)                                                             pre_profit,
        sum(ru.pre_return_amount)                                                      pre_return_amount,
        sum(ru.pre_bet_amount_settled)                                                 pre_bet_amount_settled,
        sum(ru.pre_ticket_settled)                                                     pre_ticket_settled,
        sum(ru.pre_failed_bet_amount)                                                  pre_failed_bet_amount,
        sum(ru.pre_failed_tickets)                                                     pre_failed_tickets,
        sum(ru.pre_settle_tickets)                                                     pre_settle_tickets,
        sum(ru.pre_settle_bet_amount)                                                  pre_settle_bet_amount,
        sum(ru.pre_settle_profit)                                                      pre_settle_profit,
        sum(ru.pre_settle_return)                                                      pre_settle_return,
        Count(CASE WHEN ru.pre_settle_tickets > 0 THEN ru.user_id END)              AS pre_settle_users,
        Count(CASE WHEN ru.live_bet_tickets > 0 THEN ru.user_id END)                AS live_bet_users,
        sum(live_bet_tickets)                                                          live_bet_tickets,
        sum(live_bet_amount)                                                           live_bet_amount,
        sum(live_valid_tickets)                                                        live_valid_tickets,
        sum(live_valid_bet_amount)                                                     live_valid_bet_amount,
        sum(live_profit)                                                               live_profit,
        sum(live_return_amount)                                                        live_return_amount,
        sum(live_bet_amount_settled)                                                   live_bet_amount_settled,
        sum(live_ticket_settled)                                                       live_ticket_settled,
        sum(live_failed_bet_amount)                                                    live_failed_bet_amount,
        sum(live_failed_tickets)                                                       live_failed_tickets,
        sum(live_settle_tickets)                                                       live_settle_tickets,
        sum(live_settle_bet_amount)                                                    live_settle_bet_amount,
        sum(live_settle_profit)                                                        live_settle_profit,
        sum(live_settle_return)                                                        live_settle_return,
        Count(CASE WHEN ru.live_settle_tickets > 0 THEN ru.user_id END)             AS live_settle_users,
        Count(CASE WHEN ru.pa_bet_tickets > 0 THEN ru.user_id END)                  AS pa_bet_users,
        sum(pa_bet_tickets)                                                            pa_bet_tickets,
        sum(pa_bet_amount)                                                             pa_bet_amount,
        sum(pa_valid_tickets)                                                          pa_valid_tickets,
        sum(pa_valid_bet_amount)                                                       pa_valid_bet_amount,
        sum(pa_profit)                                                                 pa_profit,
        sum(pa_return_amount)                                                          pa_return_amount,
        sum(pa_bet_amount_settled)                                                     pa_bet_amount_settled,
        sum(pa_ticket_settled)                                                         pa_ticket_settled,
        sum(pa_failed_bet_amount)                                                      pa_failed_bet_amount,
        sum(pa_failed_tickets)                                                         pa_failed_tickets,
        sum(pa_settle_tickets)                                                         pa_settle_tickets,
        sum(pa_settle_bet_amount)                                                      pa_settle_bet_amount,
        sum(pa_settle_profit)                                                          pa_settle_profit,
        sum(pa_settle_return)                                                          pa_settle_return,
        Count(CASE WHEN ru.pa_settle_tickets > 0 THEN ru.user_id END)               AS pa_settle_users,
        Count(CASE WHEN ru.mts_bet_tickets > 0 THEN ru.user_id END)                 AS mts_bet_users,
        sum(mts_bet_tickets)                                                           mts_bet_tickets,
        sum(mts_bet_amount)                                                            mts_bet_amount,
        sum(mts_valid_tickets)                                                         mts_valid_tickets,
        sum(mts_valid_bet_amount)                                                      mts_valid_bet_amount,
        sum(mts_profit)                                                                mts_profit,
        sum(mts_return_amount)                                                         mts_return_amount,
        sum(mts_bet_amount_settled)                                                    mts_bet_amount_settled,
        sum(mts_ticket_settled)                                                        mts_ticket_settled,
        sum(mts_failed_bet_amount)                                                     mts_failed_bet_amount,
        sum(mts_failed_tickets)                                                        mts_failed_tickets,
        sum(mts_settle_tickets)                                                        mts_settle_tickets,
        sum(mts_settle_bet_amount)                                                     mts_settle_bet_amount,
        sum(mts_settle_profit)                                                         mts_settle_profit,
        sum(mts_settle_return)                                                         mts_settle_return,
        Count(CASE WHEN ru.mts_settle_tickets > 0 THEN ru.user_id END)              AS mts_settle_users,
        Count(CASE WHEN ru.series_bet_tickets > 0 THEN ru.user_id END)              AS series_bet_users,
        sum(series_bet_tickets)                                                        series_bet_tickets,
        sum(series_bet_amount)                                                         series_bet_amount,
        sum(series_valid_tickets)                                                      series_valid_tickets,
        sum(series_valid_bet_amount)                                                   series_valid_bet_amount,
        sum(series_profit)                                                             series_profit,
        sum(series_return_amount)                                                      series_return_amount,
        sum(series_bet_amount_settled)                                                 series_bet_amount_settled,
        sum(series_ticket_settled)                                                     series_ticket_settled,
        sum(series_failed_bet_amount)                                                  series_failed_bet_amount,
        sum(series_failed_tickets)                                                     series_failed_tickets,
        sum(series_settle_tickets)                                                     series_settle_tickets,
        sum(series_settle_bet_amount)                                                  series_settle_bet_amount,
        sum(series_settle_profit)                                                      series_settle_profit,
        sum(series_settle_return)                                                      series_settle_return,
        Count(CASE WHEN ru.series_settle_tickets > 0 THEN ru.user_id END)           AS series_settle_users,
        Count(CASE WHEN rm.bet_num - ru.series_bet_tickets > 0 THEN ru.user_id END) AS single_bet_users,
        Count(CASE
        WHEN rm.settle_order_num - ru.series_settle_tickets > 0
        THEN ru.user_id END)                                          AS single_settle_users,


        sum(ru.order_valid_bet_money)                                                  as order_valid_bet_money,
        sum(ru.settle_valid_bet_money)                                                 as settle_valid_bet_money
    from tybss_report.r_user_order_day_utc8_sub ru
    left join tybss_report.r_user_order_day_utc8 rm on ru.id = rm.id
    where ru.time = executetimeL
    and ru.merchant_code is not null
    group by ru.merchant_code) a
    LEFT JOIN (SELECT m.merchant_code
        , m.merchant_name
        , pm.merchant_name   parent_name
        , pm.merchant_code   parent_code
        , MIN(m.`level`) AS  merchantLevel
        , MIN(m.agent_level) agentLevel
    FROM tybss_new.t_merchant m
    left join tybss_new.t_merchant pm on pm.id = m.parent_id
    GROUP BY m.merchant_code) d ON a.merchant_code = d.merchant_code;

SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_date, ',p_r_merchant_order_day:(2), success!');
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

##CALL p_r_merchant_order_day(CURRENT_DATE);
