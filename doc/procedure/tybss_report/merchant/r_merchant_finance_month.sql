DROP PROCEDURE if EXISTS p_r_merchant_finance_month;
DELIMITER //
CREATE PROCEDURE p_r_merchant_finance_month(in str_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 12;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;
    DECLARE executeMonth VARCHAR(15);
    DECLARE executeTimeL BIGINT(18);
    DECLARE total INT(10) DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, '错误码：', result_code, ' p_r_merchant_finance_month错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');
    set executeTimeL = unix_timestamp(date_sub(date_sub(curdate(), interval day(curdate()) - 1 day),
                                               interval 1 month)) * 1000 + (12 * 60 * 60 * 1000);
    set executeMonth = date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH), '%Y-%m');
    #多币种和去0的关系  不适用REPLACE
/*    DELETE
    FROM tybss_report.r_merchant_finance_month
    WHERE finance_date = executeMonth;*/
    REPLACE INTO tybss_report.r_merchant_finance_month
    SELECT concat(left(a.finance_date, 7), '-', a.merchant_code) as id,
           left(a.finance_date, 7)                               as finance_date,
           executeTimeL                                          as finance_time,
           a.merchant_id                                         as merchant_id,
           a.merchant_code                                       as merchant_code,
           a.parent_id                                           as parent_id,
           b.merchant_num                                        as merchant_num,
           a.merchant_name                                       as merchant_name,
           a.agent_level                                         as agent_level,
           a.currency                                            as currency,
           IFNULL(IF((case computing_standard
                          when 1 then b.settle_order_amount_total * a.execute_rate / 100
                          when 2 then b.profit_amount * a.execute_rate / 100
                          else 0 end) < 0, 0, (case computing_standard
                                                   when 1 then b.settle_order_amount_total * a.execute_rate / 100
                                                   when 2 then b.profit_amount * a.execute_rate / 100
                                                   else 0 end)) +
                  case IFNULL(a.vip_payment_cycle, 1)
                      when 1 then IFNULL(a.vip_amount, 0)
                      when 2 then (case
                                       WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then IFNULL(a.vip_amount, 0)
                                       else 0 end)
                      when 3 then (case WHEN month(CURRENT_DATE) = 1 then IFNULL(a.vip_amount, 0) else 0 end)
                      else 0 end +
                  case IFNULL(a.technique_payment_cycle, 1)
                      when 1 then IFNULL(a.technique_amount, 0)
                      when 2 then (case
                                       WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then IFNULL(a.technique_amount, 0)
                                       else 0 end)
                      when 3 then (case WHEN month(CURRENT_DATE) = 1 then IFNULL(a.technique_amount, 0) else 0 end)
                      else 0 end, 0)
                                                                 as bill_amount,
           IF((case computing_standard
                   when 1 then b.settle_order_amount_total * a.execute_rate / 100
                   when 2 then b.profit_amount * a.execute_rate / 100
                   else 0 end) < 0, 0, (case computing_standard
                                            when 1 then b.settle_order_amount_total * a.execute_rate / 100
                                            when 2 then b.profit_amount * a.execute_rate / 100
                                            else 0 end))
                                                                 as order_payment_amount,
           b.settle_order_amount_total                           as order_amount_total,
           b.profit_amount                                       as profit_amount,
           a.computing_standard                                  as computing_standard,
           a.terrace_rate                                        as terrace_rate,
           a.execute_rate                                        as execute_rate,
           0                                                     as adjust_amount,
           IFNULL(a.vip_amount, 0)                               as vip_amount,
           IFNULL(a.technique_amount, 0)                         as technique_amount,
           'admin'                                               as create_user,
           unix_timestamp(now())                                 as create_time,
           'admin'                                               as modify_user,
           unix_timestamp(now())                                 as modify_time,
           IFNULL(a.payment_cycle, 1)                            as payment_cycle,
           IFNULL(a.vip_payment_cycle, 1)                        as vip_payment_cycle,
           IFNULL(a.technique_payment_cycle, 1)                  as technique_payment_cycle,
           executeTimeL                                          as elasticsearch_id,
           b.order_num                                           as order_num,
           ''                                                    as adjust_cause,
        TRUNCATE(b.order_valid_bet_money,2)                        as order_valid_bet_money,
        TRUNCATE(b.settle_valid_bet_money,2)                       as settle_valid_bet_money,

           b.cashout_amount                                      as cashout_amount,
           b.cashout_profit                                      as cashout_profit

    FROM (SELECT *
          FROM tybss_report.r_merchant_finance_day
          WHERE finance_date like CONCAT(executeMonth, '%')
            and id in (select MAX(id)
                       FROM tybss_report.r_merchant_finance_day
                       where finance_date like CONCAT(executeMonth, '%')
                         and currency = '1'
                       GROUP BY merchant_code)) as a
             left JOIN (SELECT d.merchant_id                                                         as merchant_id,
                               d.merchant_num                                                        as merchant_num,
                               sum(d.settle_order_amount_total * (SELECT rate
                                                                  FROM tybss_merchant_common.t_currency_rate
                                                                  WHERE currency_code = d.currency)) as settle_order_amount_total,
                               sum(d.profit_amount * (SELECT rate
                                                      FROM tybss_merchant_common.t_currency_rate
                                                      WHERE currency_code = d.currency))             as profit_amount,
                               sum(order_num)                                                        as order_num,
                               sum(order_valid_bet_money)                                            as order_valid_bet_money,
                               sum(settle_valid_bet_money)                                           as settle_valid_bet_money,

                               sum(cashout_amount)                                                   as cashout_amount,
                               sum(cashout_profit)                                                   as cashout_profit

                        FROM (select mf.merchant_id                                      as merchant_id,
                                     (SELECT count(DISTINCT merchant_id)
                                      FROM r_merchant_finance_day
                                      WHERE parent_id = mf.merchant_id
                                        and finance_date like CONCAT(executeMonth, '%')) as merchant_num,
                                     mf.currency                                         as currency,
                                     IFNULL(sum(mf.settle_order_amount_total), 0)        as settle_order_amount_total,
                                     IFNULL(sum(mf.settle_platform_profit), 0)           as profit_amount,
                                     IFNULL(sum(mf.settle_order_num), 0)                 as order_num,

                                     IFNULL(sum(order_valid_bet_money), 0)               as order_valid_bet_money,
                                     IFNULL(sum(settle_valid_bet_money), 0)              as settle_valid_bet_money,

                                     IFNULL(sum(settle_cashout_amount), 0)               as cashout_amount,
                                     IFNULL(sum(settle_cashout_profit), 0)               as cashout_profit

                              FROM tybss_report.r_merchant_finance_day mf
                              where mf.finance_date like CONCAT(executeMonth, '%')
                              GROUP BY mf.merchant_id, mf.currency) as d
                        GROUP BY merchant_id) as b on a.merchant_id = b.merchant_id;

/*    DELETE
    FROM tybss_report.r_merchant_finance_bill_month
    WHERE concat(left(id, 7)) = executeMonth;*/
    replace into tybss_report.r_merchant_finance_bill_month
    SELECT concat(concat(left(max(finance_date), 7), '-', merchant_code), '-', currency) as id,
           concat(left(max(finance_date), 7), '-', merchant_code)                        as finance_id,
           currency                                                                      as currency,
           SUM(settle_order_amount_total)                                                as bill_order_amount,
           SUM(settle_order_num)                                                         as bill_order_num,
           SUM(settle_platform_profit)                                                   as bill_profit_amount,
           unix_timestamp(now())                                                         as create_time,
           unix_timestamp(now())                                                         as modify_time,
           executeTimeL                                                                  as elasticsearch_id
    FROM tybss_report.r_merchant_finance_day
    where finance_date like CONCAT(executeMonth, '%')
    GROUP BY merchant_code, currency;


    SET end_time = get_cur_ymdhms();
    SELECT count(*) INTO total FROM tybss_report.r_merchant_finance_month where finance_date = executeMonth;
    SET exce_msg = CONCAT(execute_date, ',p_r_merchant_finance_month:(1)执行成功!共:', total);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

/*    DELETE
    FROM tybss_report.r_merchant_finance_month_utc8
    WHERE finance_date = executeMonth;*/
    REPLACE INTO tybss_report.r_merchant_finance_month_utc8
    SELECT concat(left(a.finance_date, 7), '-', a.merchant_code) as id,
           left(a.finance_date, 7)                               as finance_date,
           executeTimeL                                          as finance_time,
           a.merchant_id                                         as merchant_id,
           a.merchant_code                                       as merchant_code,
           a.parent_id                                           as parent_id,
           b.merchant_num                                        as merchant_num,
           a.merchant_name                                       as merchant_name,
           a.agent_level                                         as agent_level,
           a.currency                                            as currency,
           IFNULL(IF((case computing_standard
                          when 1 then b.settle_order_amount_total * a.execute_rate / 100
                          when 2 then b.profit_amount * a.execute_rate / 100
                          else 0 end) < 0, 0, (case computing_standard
                                                   when 1 then b.settle_order_amount_total * a.execute_rate / 100
                                                   when 2 then b.profit_amount * a.execute_rate / 100
                                                   else 0 end)) +
                  case IFNULL(a.vip_payment_cycle, 1)
                      when 1 then IFNULL(a.vip_amount, 0)
                      when 2 then (case
                                       WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then IFNULL(a.vip_amount, 0)
                                       else 0 end)
                      when 3 then (case WHEN month(CURRENT_DATE) = 1 then IFNULL(a.vip_amount, 0) else 0 end)
                      else 0 end +
                  case IFNULL(a.technique_payment_cycle, 1)
                      when 1 then IFNULL(a.technique_amount, 0)
                      when 2 then (case
                                       WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then IFNULL(a.technique_amount, 0)
                                       else 0 end)
                      when 3 then (case WHEN month(CURRENT_DATE) = 1 then IFNULL(a.technique_amount, 0) else 0 end)
                      else 0 end, 0)
                                                                 as bill_amount,
           IF((case computing_standard
                   when 1 then b.settle_order_amount_total * a.execute_rate / 100
                   when 2 then b.profit_amount * a.execute_rate / 100
                   else 0 end) < 0, 0, (case computing_standard
                                            when 1 then b.settle_order_amount_total * a.execute_rate / 100
                                            when 2 then b.profit_amount * a.execute_rate / 100
                                            else 0 end))
                                                                 as order_payment_amount,
           b.settle_order_amount_total                           as order_amount_total,
           b.profit_amount                                       as profit_amount,
           a.computing_standard                                  as computing_standard,
           a.terrace_rate                                        as terrace_rate,
           a.execute_rate                                        as execute_rate,
           0                                                     as adjust_amount,
           IFNULL(a.vip_amount, 0)                               as vip_amount,
           IFNULL(a.technique_amount, 0)                         as technique_amount,
           'admin'                                               as create_user,
           unix_timestamp(now())                                 as create_time,
           'admin'                                               as modify_user,
           unix_timestamp(now())                                 as modify_time,
           IFNULL(a.payment_cycle, 1)                            as payment_cycle,
           IFNULL(a.vip_payment_cycle, 1)                        as vip_payment_cycle,
           IFNULL(a.technique_payment_cycle, 1)                  as technique_payment_cycle,
           executeTimeL                                          as elasticsearch_id,
           b.order_num                                           as order_num,
           ''                                                    as adjust_cause,
           order_valid_bet_money                                 as order_valid_bet_money,
           settle_valid_bet_money                                as settle_valid_bet_money,
           settle_cashout_amount                                 as settle_cashout_amount,
           settle_cashout_profit                                 as settle_cashout_profit
    FROM (SELECT *
          FROM tybss_report.r_merchant_finance_day_utc8
          WHERE finance_date like CONCAT(executeMonth, '%')
            and id in (select MAX(id)
                       FROM tybss_report.r_merchant_finance_day_utc8
                       where finance_date like CONCAT(executeMonth, '%')
                         and currency = '1'
                       GROUP BY merchant_code)) as a
             LEFT JOIN (SELECT d.merchant_id                                                         as merchant_id,
                               d.merchant_num                                                        as merchant_num,
                               sum(d.settle_order_amount_total * (SELECT rate
                                                                  FROM tybss_merchant_common.t_currency_rate
                                                                  WHERE currency_code = d.currency)) as settle_order_amount_total,
                               sum(d.profit_amount * (SELECT rate
                                                      FROM tybss_merchant_common.t_currency_rate
                                                      WHERE currency_code = d.currency))             as profit_amount,
                               sum(order_num)                                                        as order_num,
                               sum(order_valid_bet_money)                                            as order_valid_bet_money,
                               sum(settle_valid_bet_money)                                           as settle_valid_bet_money,
                               sum(settle_cashout_amount)                                            as settle_cashout_amount,
                               sum(settle_cashout_profit)                                            as settle_cashout_profit
                        FROM (select mf.merchant_id                                      as merchant_id,
                                     (SELECT count(DISTINCT merchant_id)
                                      FROM r_merchant_finance_day_utc8
                                      WHERE parent_id = mf.merchant_id
                                        and finance_date like CONCAT(executeMonth, '%')) as merchant_num,
                                     mf.currency                                         as currency,
                                     IFNULL(sum(mf.settle_order_amount_total), 0)        as settle_order_amount_total,
                                     IFNULL(sum(mf.settle_platform_profit), 0)           as profit_amount,
                                     IFNULL(sum(mf.settle_order_num), 0)                 as order_num,
                                     IFNULL(sum(mf.order_valid_bet_money), 0)            as order_valid_bet_money,
                                     IFNULL(sum(mf.settle_valid_bet_money), 0)           as settle_valid_bet_money,
                                     IFNULL(sum(mf.settle_cashout_amount), 0)            as settle_cashout_amount,
                                     IFNULL(sum(mf.settle_cashout_profit), 0)            as settle_cashout_profit

                              FROM tybss_report.r_merchant_finance_day_utc8 mf
                              where mf.finance_date like CONCAT(executeMonth, '%')
                              GROUP BY mf.merchant_id, mf.currency) as d
                        GROUP BY merchant_id) as b on a.merchant_id = b.merchant_id;

/*  DELETE
  FROM tybss_report.r_merchant_finance_bill_month_utc8
  WHERE concat(left(id, 7)) = executeMonth;*/
    REPLACE into tybss_report.r_merchant_finance_bill_month_utc8
    SELECT concat(concat(left(max(finance_date), 7), '-', merchant_code), '-', currency) as id,
           concat(left(max(finance_date), 7), '-', merchant_code)                        as finance_id,
           currency                                                                      as currency,
           SUM(settle_order_amount_total)                                                as bill_order_amount,
           SUM(settle_order_num)                                                         as bill_order_num,
           SUM(settle_platform_profit)                                                   as bill_profit_amount,
           unix_timestamp(now())                                                         as create_time,
           unix_timestamp(now())                                                         as modify_time,
           executeTimeL                                                                  as elasticsearch_id
    FROM tybss_report.r_merchant_finance_day_utc8
    where finance_date like CONCAT(executeMonth, '%')
    GROUP BY merchant_code, currency;

    /*sql结束*/
/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, ',p_r_merchant_finance_month,(2)success!');

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;


/**
CALL p_r_merchant_finance_month('2020-06-01');
CALL p_r_merchant_finance_month('2021-10-31');
都计算：2020-05月份的月数据
*/
