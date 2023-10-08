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