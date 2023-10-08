DROP PROCEDURE if EXISTS tybss_report.p_user_order_all;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE tybss_report.p_user_order_all(in str_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 99;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;
    DECLARE endTimeL BIGINT(13);
    DECLARE startTimeL BIGINT(13);
    DECLARE executetimeL BIGINT(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('1', '错误码：', result_code, ' p_user_order_all 错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, str_date, end_time, result, exce_msg);
    END;

    SET execute_date = str_to_date(str_date, '%Y-%m-%d');

    set startTimeL = unix_timestamp(str_date) * 1000;

    set endTimeL = unix_timestamp(str_date) * 1000 + 24 * 60 * 60 * 1000 - 1;

    set executetimeL = DATE_FORMAT(execute_date, '%Y%m%d');
    INSERT INTO tybss_report.user_order_all
    SELECT u.uid                               user_id,
           str_date                            last_update,
           u.username                          user_name,
           u.currency_code                     currency_code,
           1                                   exchange_rate,
           u.merchant_code                     merchant_code,
           0                                   order_amount,
           0                                   valid_bet_amount,
           0                                   bet_amount,
           0                                   profit,
           0                                   profit_rate,
           Ifnull(l.login_time, u.modify_time) last_login,
           u.last_bet_time                     last_bet,
           m.merchant_name                     merchant_name,
           u.create_time                       register_time,
           0                                   profit_order_rate,
           0                                   balance,
           u.user_level                        level_id,
           0                                   settle_amount,
           0                                   valid_order_num,
           null                                first_bet_date,
           p.merchant_code                     parent_code,
           p.merchant_name                     parent_name
    FROM tybss_merchant_common.t_user u
             left join tybss_merchant_common.t_merchant m on m.merchant_code = u.merchant_code
             left join tybss_merchant_common.t_merchant p on m.parent_id = p.id
             left join tybss_merchant_common.t_log_history l on l.uid = u.uid
    where u.create_time >= startTimeL
      and u.create_time <= endTimeL
      and u.merchant_code not in ('oubao', '719919', '105627', '472028', '553452', '2N12', '2I859', '2G325', '2M175')
    ON DUPLICATE KEY UPDATE last_update=values(last_update),
                            last_login = values(last_login);

    INSERT INTO tybss_report.user_order_all
    SELECT tu.user_id                          user_id,
           str_date                            last_update,
           tu.user_name                        user_name,
           u.currency_code                     currency_code,
           1                                   exchange_rate,
           tu.merchant_code                    merchant_code,
           Ifnull(tu.tickets, 0)               order_amount,
           Ifnull(tu.settle_order_amount, 0)   valid_bet_amount,
           Ifnull(tu.bet_amount, 0)            bet_amount,
           Ifnull(tu.settle_profit, 0)         profit,
           0                                   profit_rate,
           Ifnull(l.login_time, u.modify_time) last_login,
           u.last_bet_time                     last_bet,
           tu.merchant_name                    merchant_name,
           u.create_time                       register_time,
           0                                   profit_order_rate,
           0                                   balance,
           u.user_level                        level_id,
           Ifnull(tu.settle_return, 0)         settle_amount,
           Ifnull(tu.settle_order_num, 0)      valid_order_num,
           null                                first_bet_date,
           tu.merchant_code                    parent_code,
           tu.merchant_name                    parent_name
    FROM (select user_id,
                 min(user_name)           user_name,
                 min(merchant_code)       merchant_code,
                 sum(bet_num)             tickets,
                 sum(settle_order_num)    settle_order_num,
                 sum(settle_order_amount) settle_order_amount,
                 sum(bet_amount)          bet_amount,
                 sum(settle_profit)       settle_profit,
                 sum(settle_return)       settle_return,
                 min(merchant_name)       merchant_name,
                 min(parent_code)         parent_code,
                 min(parent_name)         parent_name
          from r_user_order_month_utc8
          where user_id in (select user_id from r_user_order_day_utc8 where time = executetimeL)
            and merchant_code not in
                ('oubao', '719919', '105627', '472028', '553452', '2N12', '2I859', '2G325', '2M175')
          group by user_id) tu
             left join tybss_merchant_common.t_user u on tu.user_id = u.uid
             left join tybss_merchant_common.t_log_history l on l.uid = u.uid
    ON DUPLICATE KEY UPDATE last_update=values(last_update),
                            last_login = values(last_login),
                            order_amount = values(order_amount),
                            valid_bet_amount = values(valid_bet_amount),
                            bet_amount = values(bet_amount),
                            profit = values(profit),
                            last_bet = values(last_bet),
                            level_id = values(level_id),
                            settle_amount = values(settle_amount),
                            valid_order_num = values(valid_order_num);

    update user_order_all tu inner join (select a.user_id, a.timeL
                                         from (select ou.user_id, min(ou.time) timeL
                                               from r_user_order_day_utc8 ou
                                               where ou.user_id in (select user_id
                                                                    from r_user_order_day_utc8 ud
                                                                    where ud.time = executetimeL
                                                                      and ud.user_id in
                                                                          (select user_id from user_order_all where first_bet_date is null))
                                                 and ou.bet_amount > 0
                                               group by ou.user_id) a) b on tu.user_id = b.user_id
    set tu.first_bet_date=str_to_date(b.timeL, '%Y%m%d');


    /*sql结束*/
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(executetimeL, 'p_user_order_all成功,', startTimeL, ',', endTimeL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END
//
DELIMITER ;

/**
    执行语句
    全部全量
CALL  p_user_order_all('2020-05-03');
    每日增量
CALL  p_user_order_all('2020-05-03');
CALL  p_user_order_all('2020-05-03'); 强制执行的意思  --指定日期统计，如果当日统计过会导致累加2次的情况

比如18号，定时器传进去的是跑的时间是2020-06-17，计算的是2020-06-16 12:00:00到2020-06-17 11:59:59的数据
*/
##此存储过程禁止改动!!!!!!!!!
#CALL p_user_order_all('2022-07-19');