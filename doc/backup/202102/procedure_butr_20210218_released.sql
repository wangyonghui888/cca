DROP PROCEDURE if EXISTS tybss_report.p_user_seven_order;
DELIMITER //
CREATE PROCEDURE tybss_report.p_user_seven_order(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 98;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_user_seven_order成功';
    DECLARE msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, ' p_user_seven_order错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    UPDATE tybss_new.t_user tu inner join (
        select user_id, sum(valid_bet_amount) valid_bet_amount, sum(settle_profit) profit_amount
        from r_user_order_day_utc8
        where time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL -6 DAY), '%Y%m%d')
        group by user_id) uf on tu.uid = uf.user_id
    set tu.seven_day_profit_amount=uf.profit_amount,
        tu.seven_day_bet_amount   =uf.valid_bet_amount;

    UPDATE tybss_new.t_user tu
    set tu.seven_day_bet_amount=0,
        tu.seven_day_profit_amount =0
    where tu.last_bet_time < unix_timestamp(DATE_ADD(execute_date, INTERVAL -6 DAY)) * 1000 - 1 or tu.last_bet_time is null;

    /*sql结束*/
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_date, ',p_user_seven_order,success!');
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END
//
DELIMITER ;
