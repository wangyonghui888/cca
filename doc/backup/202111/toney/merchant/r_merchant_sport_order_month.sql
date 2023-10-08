/*存储过程*/

DROP PROCEDURE IF EXISTS p_r_merchant_sport_order_month;
DELIMITER //
CREATE PROCEDURE `p_r_merchant_sport_order_month`(in str_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 18;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_merchant_sport_order_month成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;
    DECLARE executetimeL BIGINT(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(str_date, '错误码：', result_code, 'p_r_merchant_sport_order_month错误信息:', msg);
        SET result = 2;
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');
    set executetimeL = DATE_FORMAT(execute_date, '%Y%m');

    REPLACE INTO r_merchant_sport_order_month
SELECT a.id,
       a.sport_id,
       a.merchant_code,
       a.merchant_name,
       a.time,
       a.merchant_level,
       a.add_user,
       a.registerAmount,
       b.betUser / a.registerAmount,
       a.profit,
       a.profit_rate,
       a.return_rate,
       a.return_amount,
       a.bet_amount,
       a.order_sum,
       b.betUser,
       a.first_bet_user_sum,
       b.settleUser / a.registerAmount,
       a.settle_profit,
       a.settle_return,
       a.settle_return_rate,
       a.settle_bet_amount,
       a.settle_order_num,
       UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
       a.parent_code,
       a.parent_name,
       IFNULL(b.settleUser, 0) AS settleUsers,
       a.agent_level           as agent_Level,
       a.bet_amount_settled    as bet_amount_settled,
       a.ticket_settled        as ticket_settled,
       b.bet_settled_users     as bet_settled_users,
       a.order_valid_bet_money  as order_valid_bet_money,
       a.settle_valid_bet_money as settle_valid_bet_money
FROM (SELECT Concat(t.merchant_code, t.sport_id, t.MONTH) AS id,
             t.sport_id,
             t.merchant_code,
             min(t.merchant_name)                            merchant_name,
             t.MONTH                                      AS time,
                 min(t.merchant_level)                           merchant_level,
                 sum(t.add_user)                                 add_user,
                 max(t.register_total_user_sum)                  registerAmount,
                 sum(t.profit)                                   profit,
                 Round(ifnull(SUM(t.profit) / IF(SUM(t.bet_amount) = '0', NULL, SUM(t.bet_amount)),
                              0), 4)                          AS profit_rate,
                 Round(ifnull(SUM(t.return_amount) / IF(SUM(t.bet_amount) = '0', NULL, SUM(t.bet_amount)),
                              0), 4)                          AS return_rate,
                 sum(t.return_amount)                         AS return_amount,
                 sum(t.bet_amount)                               bet_amount,
                 sum(t.order_sum)                                order_sum,
                 sum(t.first_bet_user_sum)                       first_bet_user_sum,
                 sum(t.settle_profit)                         AS settle_profit,
                 sum(t.settle_return)                         AS settle_return,
                 ifnull(SUM(t.settle_return) / IF(SUM(t.settle_bet_amount) = '0', NULL, SUM(t.settle_bet_amount)),
                        0)                                    AS settle_return_rate,
                 sum(t.settle_bet_amount)                        settle_bet_amount,
                 sum(t.settle_order_num)                         settle_order_num,
                 max(t.parent_code)                           as parent_code,
                 max(t.parent_name)                           as parent_name,
                 MIN(t.agent_level)                           as agent_level,
                 SUM(t.bet_amount_settled)                       bet_amount_settled,
                 SUM(t.ticket_settled)                           ticket_settled,
                 SUM(t.order_valid_bet_money)                    order_valid_bet_money,
                 SUM(t.settle_valid_bet_money)                   settle_valid_bet_money
      FROM (SELECT *,
          substring(time, 1, 6) AS 'month'
          FROM r_merchant_sport_order_day
          WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
          AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')) t
      GROUP BY t.merchant_code, t.sport_id, t.MONTH) a
         LEFT JOIN (SELECT merchant_code,
                           sport_id,
                        time,
                        COUNT(CASE WHEN bet_amount > 0 THEN user_id END)          AS betUser,
                        COUNT(CASE WHEN settle_order_amount > 0 THEN user_id END) AS settleUser,
                        Count(CASE WHEN bet_amount_settled > 0 THEN user_id END)  AS bet_settled_users
                    FROM r_user_sport_order_month
                    WHERE time = executetimeL
                    GROUP BY merchant_code, time, sport_id) b
                   ON a.merchant_code = b.merchant_code AND a.time = b.time AND a.sport_id = b.sport_id;





REPLACE INTO r_merchant_sport_order_month
SELECT Concat(parent_code, sport_id, time)                                                 id,
       sport_id,
       parent_code,
       min(parent_name),
    time,
    0,
    sum(add_user)                                                                       add_user,
    sum(register_total_user_sum)                                                        register_total_user_sum,
    sum(bet_user_sum) / sum(register_total_user_sum)                                    bet_user_rate,
    sum(profit)                                                                         profit,
    sum(profit) / IF(sum(bet_amount) = '0', NULL, sum(bet_amount))                      profit_rate,
    sum(return_amount) / IF(sum(bet_amount) = '0', NULL, sum(bet_amount))               return_rate,
    sum(return_amount)                                                                  return_amount,
    sum(bet_amount)                                                                     bet_amount,
    sum(order_sum)                                                                      total_tickets,
    sum(bet_user_sum)                                                                   bet_users,
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
    sum(order_valid_bet_money)                                                          order_valid_bet_money,
    sum(settle_valid_bet_money)                                                         settle_valid_bet_money
FROM `r_merchant_sport_order_month`
where parent_code is not null
  and time = executetimeL
group by parent_code, sport_id, time;
SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, ' p_r_merchant_sport_order_month(1)success!');

CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);




REPLACE INTO r_merchant_sport_order_month_utc8
SELECT a.id,
       a.sport_id,
       a.merchant_code,
       a.merchant_name,
       a.time,
       a.merchant_level,
       a.add_user,
       a.registerAmount,
       b.betUser / a.registerAmount,
       a.profit,
       a.profit_rate,
       a.return_rate,
       a.return_amount,
       a.bet_amount,
       a.order_sum,
       a.valid_bet_amount,
       a.valid_tickets,
       a.failed_bet_amount,
       a.failed_tickets,
       b.betUser,
       a.first_bet_user_sum,
       b.settleUser / a.registerAmount,
       a.settle_profit,
       a.settle_return,
       a.settle_return_rate,
       a.settle_bet_amount,
       a.settle_order_num,
       UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
       a.parent_code,
       a.parent_name,
       IFNULL(b.settleUser, 0) AS settleUsers,
       agent_level             as agent_Level,
       a.bet_amount_settled    as bet_amount_settled,
       a.ticket_settled        as ticket_settled,
       b.bet_settled_users     as bet_settled_users,
       a.order_valid_bet_money as order_valid_bet_money,
       a.settle_valid_bet_money as settle_valid_bet_money
FROM (SELECT Concat(t.merchant_code, t.sport_id, t.MONTH) AS id,
             t.sport_id,
             t.merchant_code,
             min(t.merchant_name)                            merchant_name,
             t.MONTH                                      AS time,
                 min(t.merchant_level)                           merchant_level,
                 sum(t.add_user)                                 add_user,
                 max(t.register_total_user_sum)                  registerAmount,
                 sum(t.profit)                                   profit,
                 ifnull(SUM(t.profit) / IF(SUM(t.bet_amount) = '0', NULL, SUM(t.bet_amount)),
                        0)                                    AS profit_rate,
                 ifnull(SUM(t.return_amount) / IF(SUM(t.bet_amount) = '0', NULL, SUM(t.bet_amount)),
                        0)                                    AS return_rate,
                 sum(t.return_amount)                         AS return_amount,
                 sum(t.bet_amount)                               bet_amount,
                 sum(t.order_sum)                                order_sum,
                 sum(t.valid_bet_amount)                         valid_bet_amount,
                 sum(t.valid_tickets)                            valid_tickets,
                 sum(t.failed_bet_amount)                        failed_bet_amount,
                 sum(t.failed_tickets)                           failed_tickets,
                 sum(t.first_bet_user_sum)                       first_bet_user_sum,
                 sum(t.settle_profit)                         AS settle_profit,
                 sum(t.settle_return)                         AS settle_return,
                 ifnull(SUM(t.settle_return) / IF(SUM(t.settle_bet_amount) = '0', NULL, SUM(t.settle_bet_amount)),
                        0)                                    AS settle_return_rate,
                 sum(t.settle_bet_amount)                        settle_bet_amount,
                 sum(t.settle_order_num)                         settle_order_num,
                 max(t.parent_code)                           as parent_code,
                 max(t.parent_name)                           as parent_name,
                 MIN(t.agent_level)                           as agent_level,
                 SUM(t.bet_amount_settled)                       bet_amount_settled,
                 SUM(t.ticket_settled)                           ticket_settled,
                 sum(t.order_valid_bet_money)                as order_valid_bet_money,
                 sum(t.settle_valid_bet_money)               as settle_valid_bet_money
      FROM (SELECT *,
          substring(time, 1, 6) AS 'month'
          FROM r_merchant_sport_order_day_utc8
          WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
          AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')) t
      GROUP BY t.merchant_code, t.sport_id, t.MONTH) a
         LEFT JOIN (SELECT merchant_code,
                           sport_id,
                        time,
                        COUNT(CASE WHEN bet_amount > 0 THEN user_id END)          AS betUser,
                        COUNT(CASE WHEN settle_order_amount > 0 THEN user_id END) AS settleUser,
                        Count(CASE WHEN bet_amount_settled > 0 THEN user_id END)  AS bet_settled_users
                    FROM r_user_sport_order_month_utc8
                    WHERE time = executetimeL
                    GROUP BY merchant_code, time, sport_id) b
                   ON a.merchant_code = b.merchant_code AND a.time = b.time AND a.sport_id = b.sport_id;



REPLACE INTO r_merchant_sport_order_month_utc8
SELECT Concat(parent_code, sport_id, time)                                                 id,
       sport_id,
       parent_code,
       min(parent_name),
    time,
    0,
    sum(add_user)                                                                       add_user,
    sum(register_total_user_sum)                                                        register_total_user_sum,
    sum(bet_user_sum) / sum(register_total_user_sum)                                    bet_user_rate,
    sum(profit)                                                                         profit,
    sum(profit) / IF(sum(bet_amount) = '0', NULL, sum(bet_amount))                      profit_rate,
    sum(return_amount) / IF(sum(bet_amount) = '0', NULL, sum(bet_amount))               return_rate,
    sum(return_amount)                                                                  return_amount,
    sum(bet_amount)                                                                     bet_amount,
    sum(order_sum)                                                                      total_tickets,
    sum(valid_bet_amount)                                                               valid_bet_amount,
    sum(valid_tickets)                                                                  valid_tickets,
    sum(failed_bet_amount)                                                              failed_bet_amount,
    sum(failed_tickets)                                                                 failed_tickets,
    sum(bet_user_sum)                                                                   bet_users,
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
    sum(order_valid_bet_money)                                                          order_valid_bet_money,
    sum(settle_valid_bet_money)                                                         settle_valid_bet_money
FROM `r_merchant_sport_order_month_utc8`
where parent_code is not null
  and time = executetimeL
group by parent_code, sport_id, time;



REPLACE INTO r_merchant_sport_order_month_utc8_sub
SELECT Concat(a.merchant_code, a.sport_id, executetimeL) AS id
     , a.sport_id
     , a.merchant_code
     , a.merchant_name                                   AS merchantName
     , a.parent_code
     , a.parent_name                                     AS parent_name
     , executetimeL                                      AS time
         , a.agent_level
         , a.merchant_level
         , a.pre_bet_tickets
         , b.prebetUser
         , pre_bet_amount
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
         , b.presettleUser
         , b.livebetUser
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
         , b.livesettleUser
         , b.pabetUser
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
         , b.pasettleUser
         , b.mtsbetUser
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
         , b.mtssettleUser
         , UNIX_TIMESTAMP(current_timestamp(3))
         , order_valid_bet_money
         , settle_valid_bet_money
FROM (SELECT merchant_code,
    min(merchant_name)           merchant_name,
    min(parent_code)             parent_code,
    min(parent_name)             parent_name,
    min(agent_level)             agent_level,
    min(merchant_level)          merchant_level,
    sport_id,
    sum(pre_bet_tickets)         pre_bet_tickets,

    t.MONTH                      time,
    sum(pre_bet_amount)          pre_bet_amount,
    sum(pre_valid_tickets)       pre_valid_tickets,
    sum(pre_valid_bet_amount)    pre_valid_bet_amount,
    sum(pre_profit)              pre_profit,
    sum(pre_return_amount)       pre_return_amount,
    sum(pre_bet_amount_settled)  pre_bet_amount_settled,
    sum(pre_ticket_settled)      pre_ticket_settled,
    sum(pre_failed_bet_amount)   pre_failed_bet_amount,
    sum(pre_failed_tickets)      pre_failed_tickets,
    sum(pre_settle_tickets)      pre_settle_tickets,
    sum(pre_settle_bet_amount)   pre_settle_bet_amount,
    sum(pre_settle_profit)       pre_settle_profit,
    sum(pre_settle_return)       pre_settle_return,
    sum(live_bet_tickets)        live_bet_tickets,
    sum(live_bet_amount)         live_bet_amount,
    sum(live_valid_tickets)      live_valid_tickets,
    sum(live_valid_bet_amount)   live_valid_bet_amount,
    sum(live_profit)             live_profit,
    sum(live_return_amount)      live_return_amount,
    sum(live_bet_amount_settled) live_bet_amount_settled,
    sum(live_ticket_settled)     live_ticket_settled,
    sum(live_failed_bet_amount)  live_failed_bet_amount,
    sum(live_failed_tickets)     live_failed_tickets,
    sum(live_settle_tickets)     live_settle_tickets,
    sum(live_settle_bet_amount)  live_settle_bet_amount,
    sum(live_settle_profit)      live_settle_profit,
    sum(live_settle_return)      live_settle_return,
    sum(pa_bet_tickets)          pa_bet_tickets,
    sum(pa_bet_amount)           pa_bet_amount,
    sum(pa_valid_tickets)        pa_valid_tickets,
    sum(pa_valid_bet_amount)     pa_valid_bet_amount,
    sum(pa_profit)               pa_profit,
    sum(pa_return_amount)        pa_return_amount,
    sum(pa_bet_amount_settled)   pa_bet_amount_settled,
    sum(pa_ticket_settled)       pa_ticket_settled,
    sum(pa_failed_bet_amount)    pa_failed_bet_amount,
    sum(pa_failed_tickets)       pa_failed_tickets,
    sum(pa_settle_tickets)       pa_settle_tickets,
    sum(pa_settle_bet_amount)    pa_settle_bet_amount,
    sum(pa_settle_profit)        pa_settle_profit,
    sum(pa_settle_return)        pa_settle_return,
    sum(mts_bet_tickets)         mts_bet_tickets,
    sum(mts_bet_amount)          mts_bet_amount,
    sum(mts_valid_tickets)       mts_valid_tickets,
    sum(mts_valid_bet_amount)    mts_valid_bet_amount,
    sum(mts_profit)              mts_profit,
    sum(mts_return_amount)       mts_return_amount,
    sum(mts_bet_amount_settled)  mts_bet_amount_settled,
    sum(mts_ticket_settled)      mts_ticket_settled,
    sum(mts_failed_bet_amount)   mts_failed_bet_amount,
    sum(mts_failed_tickets)      mts_failed_tickets,
    sum(mts_settle_tickets)      mts_settle_tickets,
    sum(mts_settle_bet_amount)   mts_settle_bet_amount,
    sum(mts_settle_profit)       mts_settle_profit,
    sum(mts_settle_return)       mts_settle_return,
    sum(order_valid_bet_money)   order_valid_bet_money,
    sum(settle_valid_bet_money)  settle_valid_bet_money
    FROM (SELECT *,
    substring(time, 1, 6) AS 'month'
    FROM r_merchant_sport_order_day_utc8_sub
    WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
    AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')) t
    GROUP BY t.merchant_code, t.sport_id, t.MONTH) a
    LEFT JOIN (SELECT merchant_code,
    sport_id,
    time,
    COUNT(CASE WHEN pre_bet_amount > 0 THEN user_id END)          AS prebetUser,
    COUNT(CASE WHEN pre_settle_bet_amount > 0 THEN user_id END)   AS presettleUser,
    Count(CASE WHEN pre_bet_amount_settled > 0 THEN user_id END)  AS pre_bet_settled_users,

    COUNT(CASE WHEN live_bet_amount > 0 THEN user_id END)         AS livebetUser,
    COUNT(CASE WHEN live_settle_bet_amount > 0 THEN user_id END)  AS livesettleUser,
    Count(CASE WHEN live_bet_amount_settled > 0 THEN user_id END) AS live_bet_settled_users,

    COUNT(CASE WHEN pa_bet_amount > 0 THEN user_id END)           AS pabetUser,
    COUNT(CASE WHEN pa_settle_bet_amount > 0 THEN user_id END)    AS pasettleUser,
    Count(CASE WHEN pa_bet_amount_settled > 0 THEN user_id END)   AS pa_bet_settled_users,

    COUNT(CASE WHEN mts_bet_amount > 0 THEN user_id END)          AS mtsbetUser,
    COUNT(CASE WHEN mts_settle_bet_amount > 0 THEN user_id END)   AS mtssettleUser,
    Count(CASE WHEN mts_bet_amount_settled > 0 THEN user_id END)  AS mts_bet_settled_users
    FROM r_user_sport_order_month_utc8_sub
    WHERE time = executetimeL
    GROUP BY merchant_code, time, sport_id) b
ON a.merchant_code = b.merchant_code AND a.time = b.time AND a.sport_id = b.sport_id;



SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, 'p_r_merchant_sport_order_month_utc8(2)success!');

CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
