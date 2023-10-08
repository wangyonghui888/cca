DROP PROCEDURE IF EXISTS p_r_device_type_day;
DELIMITER //
CREATE PROCEDURE p_r_device_type_day(in execute_time varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 109;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_device_type_day成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);

    DECLARE yearL BIGINT(10);
    DECLARE monthL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);
    DECLARE endTimeUTCL BIGINT(16);


    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_time, " p_r_device_type_day错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set yearL = DATE_FORMAT(execute_time, '%Y');
    set monthL = DATE_FORMAT(execute_time, '%Y%m');
	set executetimeL = DATE_FORMAT(execute_time, '%Y%m%d');

   set startTimeUTCL = unix_timestamp(execute_time) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_time, INTERVAL 1 DAY)) * 1000 - 1;


     REPLACE INTO tybss_report.r_device_type_day
	  select
	   concat(o.device_type,executetimeL)                           id,
	   o.device_type,
	  count(DISTINCT o.order_no) ,
	   executetimeL																			 time,
		   yearL                                                                                year,
		   monthL																					 mouth
	  from tybss_new.t_order o
            left join tybss_new.t_order_detail od on od.order_no = o.order_no
            left join tybss_new.s_match_info s on s.id = od.match_id
            where s.begin_time >= startTimeUTCL  and s.begin_time <= endTimeUTCL
                    and o.series_type = 1
                        AND o.order_status =1
                            and o.manager_code < 3
            group by o.device_type;


	 SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',p_r_device_type_day,success!', executetimeL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;
