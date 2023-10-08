/*任务执行记录日志*/
DROP PROCEDURE IF EXISTS p_add_task_event_log;
DELIMITER //
CREATE PROCEDURE p_add_task_event_log(IN task_type INT(2),
                                      IN start_time VARCHAR(30),
                                      IN end_time VARCHAR(30),
                                      IN result INT(2),
                                      IN exce_msg VARCHAR(512))
BEGIN
    INSERT INTO res_all_task_event_log(task_type, start_time, end_time, result, exce_msg)
    VALUES (task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

/*删除前30天的任务执行日志*/

DROP PROCEDURE IF EXISTS p_del_before_5d_log;
DELIMITER //
CREATE PROCEDURE p_del_before_5d_log()
BEGIN
    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 20;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, " 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    DELETE FROM res_all_task_event_log WHERE DATE_SUB(CURDATE(), INTERVAL 30 DAY) >= DATE(start_time);

/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;