DROP PROCEDURE if EXISTS tybss_report.p_user_hour_clean;
DELIMITER //
CREATE PROCEDURE tybss_report.p_user_hour_clean()
BEGIN
    DECLARE task_type INT(2) DEFAULT 97;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_user_hour_clean成功';
    DECLARE msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, 'p_user_hour_clean错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    truncate table tybss_report.r_user_order_hour;
    truncate table tybss_report.r_user_order_hour_sub;
    truncate table tybss_report.r_user_sport_order_hour;
    truncate table tybss_report.r_user_sport_order_hour_sub;
    truncate table tybss_report.ac_user_order_hour;

    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(current_date, ',p_user_hour_clean,success!');
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END
//
DELIMITER ;
