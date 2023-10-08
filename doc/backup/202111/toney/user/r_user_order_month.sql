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
           SUM(t.order_valid_bet_money)                          as                                    order_valid_bet_money,
           SUM(t.settle_valid_bet_money)                        as                                     order_valid_bet_money
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
           SUM(t.order_valid_bet_money)                          as                                    order_valid_bet_money,
           SUM(t.settle_valid_bet_money)                        as                                     order_valid_bet_money
    FROM r_user_order_day_utc8 t
    WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
      AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')
      AND user_id in (select user_id
                      from r_user_order_day_utc8
                      where time = executetimeL
                        and (valid_bet_amount > 0 or settle_profit <> 0))
    GROUP BY t.user_id;


    REPLACE INTO tybss_report.r_user_order_month_utc8_sub
    SELECT Concat(t.user_id, monthL) id,
           t.user_id,
           min(t.merchant_code),
           min(t.parent_code),
           monthL                    'month',
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
           IFNULL(SUM(t.order_valid_bet_money),0)     as                                    order_valid_bet_money,
           IFNULL(SUM(t.settle_valid_bet_money),0)                       as                 order_valid_bet_money
    FROM r_user_order_day_utc8_sub t
    WHERE t.time like concat(monthL, '%')
      and t.user_id in (select user_id
                        from r_user_order_day_utc8
                        where time = executetimeL)
    GROUP BY t.user_id;

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


/*    UPDATE tybss_new.t_user tu inner join (select user_id,
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
