DROP PROCEDURE IF EXISTS p_r_merchant_order_month;

DELIMITER //
CREATE PROCEDURE `p_r_merchant_order_month`(in str_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 14;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_merchant_order_month成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;
    DECLARE executetimeL BIGINT(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, 'p_r_merchant_order_month错误信息:', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');
    set executetimeL = DATE_FORMAT(execute_date, '%Y%m');


    REPLACE INTO r_merchant_order_month
    SELECT a.id,
           a.merchant_code,
           a.time,
           b.betUser / a.register_total_user_sum,
           a.profit,
           a.register_total_user_sum,
           a.return_rate,
           a.return_amount,
           a.merchant_level,
           a.bet_amount,
           IFNULL(a.valid_bet_amount, 0)      valid_bet_amount,
           IFNULL(a.filed_bet_amount, 0)      filed_bet_amount,
           a.order_sum,
           IFNULL(a.valid_tickets, 0)         valid_tickets,
           IFNULL(a.filed_tickets, 0)         filed_tickets,
           a.first_bet_user_sum,
           b.betUser,
           IFNULL(b.valid_bet_users, 0),
           b.settleUser / a.register_total_user_sum,
           a.settle_profit,
           a.settle_return,
           a.settle_return_rate,
           a.settle_bet_amount,
           a.settle_order_num,
           a.merchant_name,
           a.add_user,
           UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
           a.parent_code,
           a.parent_name,
           IFNULL(b.settleUser, 0)         AS settleUsers,
           a.agent_level                   as agent_level,
           a.bet_amount_settled            as bet_amount_settled,
           a.ticket_settled                as ticket_settled,
           b.bet_settled_users             as bet_settled_users,
           a.profit / IF(a.bet_amount_settled = '0', NULL, a.bet_amount_settled),
           a.settleExpectProfit            as settleExpectProfit,
           a.settle_series_amount          as settle_series_amount,
           a.settle_series_tickets         as settle_series_tickets,
           b.settle_series_users           as settle_series_users,
           a.settle_series_profit          as settle_series_profit,

           TRUNCATE(a.order_valid_bet_money,2)  as order_valid_bet_money,
           TRUNCATE(a.settle_valid_bet_money,2) as settle_valid_bet_money
    FROM (SELECT Concat(t.merchant_code, t.MONTH) id,
                 t.merchant_code,
                 t.MONTH                          time,
                 SUM(t.profit)                    profit,
                 max(t.register_total_user_sum)   register_total_user_sum,
                 ifnull(SUM(t.return_amount) / IF(SUM(t.bet_amount) = '0', NULL, SUM(t.bet_amount)),
                        0)                     AS return_rate,
                 SUM(t.return_amount)             return_amount,
                 MIN(t.merchant_level)            merchant_level,
                 SUM(t.bet_amount)                bet_amount,
                 SUM(t.bet_amount_settled)        bet_amount_settled,
                 SUM(t.ticket_settled)            ticket_settled,
                 SUM(t.valid_bet_amount)          valid_bet_amount,
                 SUM(t.filed_bet_amount)          filed_bet_amount,
                 SUM(t.order_sum)                 order_sum,
                 SUM(t.valid_tickets)             valid_tickets,
                 SUM(t.filed_tickets)             filed_tickets,
                 SUM(t.first_bet_user_sum)        first_bet_user_sum,
                 SUM(t.settle_profit)             settle_profit,
                 SUM(t.settle_return)             settle_return,
                 ifnull(SUM(t.settle_return) / IF(SUM(t.settle_bet_amount) = '0', NULL, SUM(t.settle_bet_amount)),
                        0)                     AS settle_return_rate,
                 SUM(t.settle_bet_amount)         settle_bet_amount,
                 SUM(t.settle_order_num)          settle_order_num,
                 MIN(t.merchant_name)             merchant_name,
                 SUM(t.add_user)                  add_user,
                 max(t.parent_code)            as parent_code,
                 max(t.parent_name)            as parent_name,
                 MIN(t.agent_level)               agent_level,
                 SUM(t.settle_expect_profit)   as settleExpectProfit,
                 SUM(t.settle_series_amount)   as settle_series_amount,
                 SUM(t.settle_series_tickets)  as settle_series_tickets,
                 SUM(t.settle_series_profit)   as settle_series_profit,

                 SUM(t.order_valid_bet_money)  as order_valid_bet_money,
                 SUM(t.settle_valid_bet_money) as settle_valid_bet_money
          FROM (SELECT *,
                       substring(time, 1, 6) AS 'month'
                FROM r_merchant_order_day
                WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
                  AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')) t
          GROUP BY t.merchant_code, t.MONTH) a
             LEFT JOIN (SELECT merchant_code,
                               time,
                               COUNT(CASE WHEN bet_amount > 0 THEN user_id END)           AS betUser,
                               COUNT(CASE WHEN valid_bet_amount > 0 THEN user_id END)     AS valid_bet_users,
                               COUNT(CASE WHEN settle_order_amount > 0 THEN user_id END)  AS settleUser,
                               Count(CASE WHEN bet_amount_settled > 0 THEN user_id END)   AS bet_settled_users,
                               Count(CASE WHEN settle_series_amount > 0 THEN user_id END) AS settle_series_users
                        FROM r_user_order_month
                        WHERE time = executetimeL
                        GROUP BY merchant_code, time) b ON a.merchant_code = b.merchant_code AND a.time = b.time;


    REPLACE INTO r_merchant_order_month
    SELECT Concat(parent_code, time)                                                           id,
           parent_code,
           time,
           sum(bet_user_sum) / sum(register_total_user_sum)                                    bet_user_rate,
           sum(profit)                                                                         profit,
           sum(register_total_user_sum)                                                        register_total_user_sum,
           sum(return_amount) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))   return_rate,
           sum(return_amount)                                                                  return_amount,
           0,
           sum(bet_amount)                                                                     bet_amount,
           sum(valid_bet_amount)                                                               valid_bet_amount,
           sum(filed_bet_amount)                                                               filed_bet_amount,
           sum(order_sum)                                                                      total_tickets,
           sum(valid_tickets)                                                                  valid_tickets,
           sum(filed_tickets)                                                                  filed_tickets,
           sum(first_bet_user_sum)                                                             first_bet_user_sum,
           sum(bet_user_sum)                                                                   bet_users,
           sum(valid_bet_users)                                                                valid_bet_users,
           sum(settle_users) / sum(register_total_user_sum)                                    settle_user_rate,
           sum(settle_profit)                                                                  settle_profit,
           sum(settle_return)                                                                  settle_return,
           sum(settle_return) / IF(sum(settle_bet_amount) = '0', NULL, sum(settle_bet_amount)) settle_return_rate,
           sum(settle_bet_amount)                                                              settle_bet_amount,
           sum(settle_order_num)                                                               settle_order_num,
           min(parent_name),
           sum(add_user)                                                                       add_user,
           UNIX_TIMESTAMP(current_timestamp(3)),
           null,
           null,
           sum(settle_users)                                                                   settle_users,
           1,
           sum(bet_amount_settled)                                                             bet_amount_settled,
           sum(ticket_settled)                                                                 ticket_settled,
           sum(bet_settled_users)                                                              bet_settled_users,
           sum(profit) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))          profit_rate,
           SUM(settle_expect_profit)          as                                               settleExpectProfit,
           SUM(settle_series_amount)          as                                               settle_series_amount,
           SUM(settle_series_tickets)         as                                               settle_series_tickets,
           SUM(settle_series_users)           as                                               settle_series_users,
           SUM(settle_series_profit)          as                                               settle_series_profit,
           SUM(order_valid_bet_money)  as                                               order_valid_bet_money,
           SUM(settle_valid_bet_money) as                                               settle_valid_bet_money
    FROM `r_merchant_order_month`
    where parent_code is not null
      and time = executetimeL
    group by parent_code, time;

    REPLACE INTO r_merchant_order_month_utc8
    SELECT a.id,
           a.merchant_code,
           a.time,
           a.merchant_name,
           a.merchant_level,
           a.add_user,
           a.register_total_user_sum,
           b.betUser / a.register_total_user_sum,
           a.profit,
           a.profit / IF(a.bet_amount_settled = '0', NULL, a.bet_amount_settled),
           a.return_amount,
           a.return_rate,
           a.bet_amount,
           a.valid_bet_amount,
           a.filed_bet_amount,
           a.total_tickets,
           a.valid_tickets,
           a.filed_tickets,
           b.betUser,
           b.valid_bet_users,
           a.first_bet_user_sum,
           b.settleUser / a.register_total_user_sum,
           a.settle_profit,
           a.settle_return,
           a.settle_return_rate,
           a.settle_bet_amount,
           a.settle_order_num,
           UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
           a.parent_code,
           a.parent_name,
           IFNULL(b.settleUser, 0)         AS settleUsers,
           a.agent_level                   as agent_level,
           a.bet_amount_settled            as bet_amount_settled,
           a.ticket_settled                as ticket_settled,
           b.bet_settled_users             as bet_settled_users,
           a.settleExpectProfit            as settleExpectProfit,
           a.settle_series_amount          as settle_series_amount,
           a.settle_series_tickets         as settle_series_tickets,
           b.settle_series_users           as settle_series_users,
           a.settle_series_profit          as settle_series_profit,

           TRUNCATE(a.order_valid_bet_money,2)  as order_valid_bet_money,
           TRUNCATE(a.settle_valid_bet_money,2) as settle_valid_bet_money
    FROM (SELECT Concat(t.merchant_code, t.MONTH) id,
                 t.merchant_code,
                 t.MONTH                          time,
                 SUM(t.profit)                    profit,
                 max(t.register_total_user_sum)   register_total_user_sum,
                 ifnull(SUM(t.return_amount) / IF(SUM(t.bet_amount) = '0', NULL, SUM(t.bet_amount)),
                        0)                     AS return_rate,
                 SUM(t.return_amount)             return_amount,
                 MIN(t.merchant_level)            merchant_level,
                 SUM(t.bet_amount)                bet_amount,
                 SUM(t.bet_amount_settled)        bet_amount_settled,
                 SUM(t.ticket_settled)            ticket_settled,
                 SUM(t.valid_bet_amount)          valid_bet_amount,
                 SUM(t.filed_bet_amount)          filed_bet_amount,
                 SUM(t.total_tickets)             total_tickets,
                 SUM(t.valid_tickets)             valid_tickets,
                 SUM(t.filed_tickets)             filed_tickets,
                 SUM(t.first_bet_user_sum)        first_bet_user_sum,
                 SUM(t.settle_profit)             settle_profit,
                 SUM(t.settle_return)             settle_return,
                 ifnull(SUM(t.settle_return) / IF(SUM(t.settle_bet_amount) = '0', NULL, SUM(t.settle_bet_amount)),
                        0)                     AS settle_return_rate,
                 SUM(t.settle_bet_amount)         settle_bet_amount,
                 SUM(t.settle_order_num)          settle_order_num,
                 MIN(t.merchant_name)             merchant_name,
                 SUM(t.add_user)                  add_user,
                 max(t.parent_code)            as parent_code,
                 max(t.parent_name)            as parent_name,
                 MIN(t.agent_level)               agent_level,
                 SUM(t.settle_expect_profit)   as settleExpectProfit,
                 SUM(t.settle_series_amount)   as settle_series_amount,
                 SUM(t.settle_series_tickets)  as settle_series_tickets,
                 SUM(t.settle_series_profit)   as settle_series_profit,

                 SUM(t.order_valid_bet_money)  as order_valid_bet_money,
                 SUM(t.settle_valid_bet_money) as settle_valid_bet_money
          FROM (SELECT *,
                       substring(time, 1, 6) AS 'month'
                FROM r_merchant_order_day_utc8
                WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
                  AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')) t
          GROUP BY t.merchant_code,
                   t.MONTH) a
             LEFT JOIN (SELECT merchant_code,
                               time,
                               COUNT(CASE WHEN bet_amount > 0 THEN user_id END)           AS betUser,
                               COUNT(CASE WHEN valid_bet_amount > 0 THEN user_id END)     AS valid_bet_users,
                               COUNT(CASE WHEN settle_order_amount > 0 THEN user_id END)  AS settleUser,
                               Count(CASE WHEN bet_amount_settled > 0 THEN user_id END)   AS bet_settled_users,
                               Count(CASE WHEN settle_series_amount > 0 THEN user_id END) AS settle_series_users
                        FROM r_user_order_month_utc8
                        WHERE time = executetimeL
                        GROUP BY merchant_code, time) b ON a.merchant_code = b.merchant_code AND a.time = b.time;

    REPLACE INTO r_merchant_order_month_utc8
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
           null,
           null,
           sum(settle_users)                                                                   settle_users,
           1,
           sum(bet_amount_settled)                                                             bet_amount_settled,
           sum(ticket_settled)                                                                 ticket_settled,
           sum(bet_settled_users)                                                              bet_settled_users,
           SUM(settle_expect_profit)          as                                               settleExpectProfit,
           SUM(settle_series_amount)          as                                               settle_series_amount,
           SUM(settle_series_tickets)         as                                               settle_series_tickets,
           SUM(settle_series_users)           as                                               settle_series_users,
           SUM(settle_series_profit)          as                                               settle_series_profit,

           TRUNCATE(SUM(order_valid_bet_money),2)  as                                               order_valid_bet_money,
           TRUNCATE(SUM(settle_valid_bet_money),2) as                                               settle_valid_bet_money
    FROM `r_merchant_order_month_utc8`
    where parent_code is not null
      and time = executetimeL
    group by parent_code, time;

    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, ' p_r_merchant_order_month success!');

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
