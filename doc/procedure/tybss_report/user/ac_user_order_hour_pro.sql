DROP PROCEDURE IF EXISTS tybss_report.p_ac_user_order_hour;
DELIMITER //
CREATE PROCEDURE tybss_report.p_ac_user_order_hour(in execute_time varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 59;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_ac_user_order_hour成功';
    DECLARE msg TEXT;
    DECLARE executetimeL BIGINT(10);
    DECLARE yearL BIGINT(10);
    DECLARE dayL BIGINT(16);
    DECLARE monthL BIGINT(16);

    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, 'p_ac_user_order_hour错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    set executetimeL = DATE_FORMAT(execute_time, '%Y%m%d%H');

    set yearL = DATE_FORMAT(execute_time, '%Y');
    set dayL = DATE_FORMAT(execute_time, '%Y%m%d');
    set monthL = DATE_FORMAT(execute_time, '%Y%m');

    set startTimeUTCL = unix_timestamp(DATE_FORMAT(execute_time, '%Y-%m-%d %H:00:00')) * 1000;
    set endTimeUTCL = startTimeUTCL + 1000 * 60 * 60 - 1;

    delete from tybss_report.ac_user_order_hour where time = executetimeL;

    REPLACE INTO tybss_report.ac_user_order_hour
    SELECT Concat(a.uid, executetimeL)                 AS id
         , a.uid
         , executetimeL                                AS time
         , u.username
         , u.merchant_code
         , IFNULL(a.profit, 0)                            profit
         , IFNULL(a.settleAmount, 0)                      returnAmount
         , IFNULL(a.validBetAmount, 0)                    validBetAmount
         , IFNULL(a.validTickets, 0)                      validTickets
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000 AS updatedTime
    FROM (SELECT o.uid
               , SUM(s.settle_amount)      AS settleAmount
               , SUM(s.profit_amount)      AS profit
               , SUM(o.order_amount_total) AS validBetAmount
               , count(o.order_no)         AS validTickets
          FROM tybss_merchant_common.t_settle s
                   LEFT JOIN tybss_merchant_common.t_order o ON o.order_no = s.order_no
                   left join tybss_merchant_common.t_order_detail od on o.order_no = od.order_no
          WHERE s.create_time >= startTimeUTCL
            AND s.create_time <= endTimeUTCL
            AND s.last_settle = 1
            AND od.odds_value > 150000
            and s.bet_amount > 0
            and o.series_type = 1
            and s.out_come in (3, 4, 5, 6)
          GROUP BY o.uid) a
             LEFT JOIN tybss_merchant_common.t_user u ON a.uid = u.uid
             left join tybss_merchant_common.t_merchant_config mc on mc.merchant_code = u.merchant_code
             left join tybss_report.user_order_all uoa on uoa.merchant_code = u.merchant_code
    where mc.merchant_tag != 1;

    REPLACE INTO tybss_report.ac_user_order_day
    SELECT Concat(uid, dayL)                           AS id
         , uid
         , dayL                                        AS time
         , min(user_name)
         , min(merchant_code)
         , IFNULL(sum(profit), 0)                         profit
         , IFNULL(sum(return_amount), 0)                  returnAmount
         , IFNULL(sum(valid_bet_amount), 0)               validBetAmount
         , IFNULL(sum(valid_tickets), 0)                  valid_tickets
         , UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000 AS updatedTime
    from ac_user_order_hour
    where time like concat(dayL, '%')
      and uid in (select uid from ac_user_order_hour where time = executetimeL)
    group by uid;
    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_time, ',ac_user_order_hour,success!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

drop event if EXISTS e_ac_user_order_hour;
drop event if EXISTS e_ac_user_order_hour_3days;

CREATE EVENT e_ac_user_order_hour ON SCHEDULE EVERY 30 minute STARTS '2021-05-18 00:05:10' ON COMPLETION PRESERVE ENABLE DO CALL p_ac_user_order_hour(ADDDATE(now(), INTERVAL -1 hour));

CREATE EVENT e_ac_user_order_hour_3days ON SCHEDULE EVERY 1 hour STARTS '2021-05-18 00:00:10' ON COMPLETION PRESERVE ENABLE DO CALL p_ac_user_order_hour(ADDDATE(now(), INTERVAL -48 hour));

