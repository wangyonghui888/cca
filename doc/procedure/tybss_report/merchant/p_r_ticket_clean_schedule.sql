DROP PROCEDURE IF EXISTS p_r_ticket_clean_schedule;
DELIMITER //

CREATE PROCEDURE `p_r_ticket_clean_schedule`(in execute_time varchar(100))

BEGIN
    DECLARE task_type INT(2) DEFAULT 96; -- 任务类型
    DECLARE result_code CHAR(5) DEFAULT '0'; -- sql异常返回的错误编码
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms(); -- 事件开始时间
    DECLARE end_time VARCHAR(30); -- 事件结束事件
    DECLARE result INT(2) DEFAULT 1; -- 事件结果
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_ticket_clean_schedule清理成功';
    DECLARE msg TEXT;

    DECLARE endTimeUTCL BIGINT(16);

    DECLARE startTimeUTCL BIGINT(16);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, 'p_r_ticket_clean_schedule错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set endTimeUTCL = unix_timestamp(execute_time) * 1000 + 999;

    set startTimeUTCL = endTimeUTCL - 1000 * 60 * 10 - 999;

    delete
    from t_ticket_detail td
    where modify_time < endTimeUTCL
      and modify_time >= startTimeUTCL
      and order_no in (select order_no
                       from t_ticket
                       where modify_time < endTimeUTCL
                         and modify_time >= startTimeUTCL
                         and order_status in (1, 2, 4, 5));
    COMMIT;


    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_ticket_clean_schedule,ticket1 done!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);


    delete
    from t_ticket_detail
    where modify_time <= (unix_timestamp(now()) * 1000 - 100 * 24 * 60 * 60 * 1000)
      and modify_time >= (unix_timestamp(now()) * 1000 - 100 * 24 * 60 * 60 * 1000 - 1000 * 60 * 15)
      and bet_status in (1, 2, 3, 4, 5);
    COMMIT;

    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_ticket_clean_schedule,ticket2 done!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    select sleep(5);


    delete
    FROM `t_ticket`
    WHERE modify_time < endTimeUTCL
      and modify_time >= startTimeUTCL
      and order_status in (1, 2, 4, 5);
    COMMIT;

    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_ticket_clean_schedule,ticket3 done!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

    delete
    from t_ticket
    where modify_time <= (unix_timestamp(now()) * 1000 - 100 * 24 * 60 * 60 * 1000)
      and order_status in (1, 2, 4, 5)
      and modify_time >= (unix_timestamp(now()) * 1000 - 100 * 24 * 60 * 60 * 1000 - 1000 * 60 * 15);
    COMMIT;

    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_ticket_clean_schedule,ticket4 done!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    select sleep(5);

    delete
    from t_ticket_detail
    where modify_time < (unix_timestamp(now()) * 1000 - 360 * 24 * 60 * 60 * 1000);
    COMMIT;


    delete
    from t_ticket
    where modify_time < (unix_timestamp(now()) * 1000 - 400 * 24 * 60 * 60 * 1000);
    COMMIT;


    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_ticket_clean_schedule,ticket5 done!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    select sleep(5);

    delete
    from r_market_bet_info
    where updated_time < (unix_timestamp(now()) - 360 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 360 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete
    from r_market_bet_info
    where updated_time < (unix_timestamp(now()) - 180 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 180 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete
    from r_market_bet_info
    where updated_time < (unix_timestamp(now()) - 90 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 90 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete from r_market_bet_info where updated_time < (unix_timestamp(now()) - 360 * 24 * 60 * 60);
    COMMIT;


    delete
    from r_merchant_market_bet_info
    where updated_time < (unix_timestamp(now()) - 360 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 360 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete
    from r_merchant_market_bet_info
    where updated_time < (unix_timestamp(now()) - 180 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 180 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete
    from r_merchant_market_bet_info
    where updated_time < (unix_timestamp(now()) - 90 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 90 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete from r_merchant_market_bet_info where updated_time < (unix_timestamp(now()) - 360 * 24 * 60 * 60);
    COMMIT;

    delete
    from r_merchant_match_bet_info
    where updated_time < (unix_timestamp(now()) - 360 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 360 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete
    from r_merchant_match_bet_info
    where updated_time < (unix_timestamp(now()) - 180 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 180 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete
    from r_merchant_match_bet_info
    where updated_time < (unix_timestamp(now()) - 90 * 24 * 60 * 60)
      and updated_time > (unix_timestamp(now()) - 90 * 24 * 60 * 60 - 60 * 60);
    COMMIT;

    delete from r_merchant_match_bet_info where updated_time < (unix_timestamp(now()) - 360 * 24 * 60 * 60);
    COMMIT;

    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_ticket_clean_schedule,success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;