
DELIMITER $$
CREATE PROCEDURE `p_add_task_event_log`(IN task_type INT(2),
    IN start_time VARCHAR(30),
    IN end_time VARCHAR(30),
    IN result INT(2),
    IN exce_msg VARCHAR(512))
BEGIN
INSERT INTO tybss_report.res_all_task_event_log(task_type, start_time, end_time, result, exce_msg)
VALUES (task_type, start_time, end_time, result, exce_msg);
END$$
DELIMITER ;