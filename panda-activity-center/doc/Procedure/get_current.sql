/*获取当前时间，格式：年:月:日: 时-分-秒*/
/*如果出现，log日志没有sql提示异常，先执行：SET GLOBAL log_bin_trust_function_creators=TRUE;*/
SET GLOBAL log_bin_trust_function_creators = 1;

DROP FUNCTION IF EXISTS get_cur_ymdhms;

DELIMITER //
CREATE FUNCTION get_cur_ymdhms() RETURNS VARCHAR(30)
BEGIN
    DECLARE res VARCHAR(30);
    SET res = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s');
    RETURN res;
END//
DELIMITER ;
