DELIMITER $$
CREATE FUNCTION `get_cur_ymdhms`() RETURNS varchar(30) CHARSET utf8mb4 COLLATE utf8mb4_0900_as_cs
BEGIN
    DECLARE res VARCHAR(30);
    SET res = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s');
    RETURN res;
END$$
DELIMITER ;