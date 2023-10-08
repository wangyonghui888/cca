/*获取当前时间，格式：年:月:日: 时-分-秒*/
/*如果出现，log日志没有sql提示异常，先执行：SET GLOBAL log_bin_trust_function_creators=TRUE;*/
SET GLOBAL log_bin_trust_function_creators = 1;

/*日期函数*/
DROP FUNCTION if EXISTS get_cur_ymdhms;
DELIMITER //
CREATE FUNCTION get_cur_ymdhms()RETURNS VARCHAR(30)
BEGIN
DECLARE res VARCHAR(30);
SET res = DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i:%s');
RETURN res;
END//
DELIMITER ;

/*生成32为uuid*/
DROP FUNCTION if EXISTS uuid_32;
DELIMITER //
CREATE FUNCTION uuid_32() RETURNS char(50)
BEGIN
 RETURN replace(uuid(), '-', '');
END//
DELIMITER ;

/*日志表*/
DROP TABLE if EXISTS res_all_task_event_log;
CREATE TABLE `res_all_task_event_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自动编号',
  `task_type` int(2) NOT NULL DEFAULT '0' COMMENT '任务类型：1：market_bet_info，2：match_bet_info，3：merchant_order_day，4：merchant_order_month，5：merchant_order_week，6：merchant_sport_order_day ，7：merchant_sport_order_month，8：merchant_sport_order_week，9：order_full_detail，10：order_match_detail，11：settle_full_detail，12：user_full_info，13：user_order_day，14：user_order_month 15：user_order_week，16：user_sport_order_day，17：user_sport_order_month，18：user_sport_order_week，19：rcs_order_statistic_date,20：日志本身删除5天之前日志',
  `start_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '任务执行开始时间',
  `end_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '任务执行结束时间',
  `result` int(2) NOT NULL DEFAULT '0' COMMENT '成功：1，失败：2',
  `exce_msg` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '异常信息，result为2时才有',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37143 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='所有任务时间执行结果表';

/*日志函数*/
DROP PROCEDURE if EXISTS p_add_task_event_log;
DELIMITER //
CREATE PROCEDURE p_add_task_event_log(
IN task_type INT(2),
IN start_time VARCHAR(30),
IN end_time VARCHAR(30),
IN result INT(2),
IN exce_msg VARCHAR(512)
)
BEGIN
INSERT INTO res_all_task_event_log(task_type,start_time,end_time,result,exce_msg) VALUES(task_type,start_time,end_time,result,exce_msg);
END//
DELIMITER ;

/*主存储过程*/
DROP PROCEDURE if EXISTS p_user_order_all;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_user_order_all(in str_date varchar(100),start_int int(1))
BEGIN
    DECLARE task_type INT(2) DEFAULT 1;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;
    DECLARE start_date  BIGINT(13);
    DECLARE flag_date  date;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("1", "错误码：", result_code, " p_user_order_all 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');
    IF start_int = 1
    THEN  SET start_date = 0;
              DELETE FROM user_order_all;
    ELSE  SET start_date = unix_timestamp(date_sub(execute_date, interval 1 day))*1000;
	END IF;
	SELECT MAX(last_update) INTO flag_date FROM user_order_all;

	IF flag_date < date_sub(execute_date, interval 1 day) or flag_date is null
	THEN
    INSERT INTO merchant.user_order_all
--             (user_id,last_update,user_name,merchant_code,order_amount,valid_bet_amount,bet_amount,
--             profit,last_login,last_bet,merchant_name,register_time,profit_order_num,refuse_order_num,
--             refuse_order_amount,cancel_order_num,cancel_order_amount,all_num,greterThan2_num,
--             lessThan2_num,soccer_num,basketball_num,others_num,series_num,basketball_handicap_num,
--             basketball_overunder_num,balance,level_id,settle_amount,valid_order_num,first_bet_date)
            SELECT
                u.uid                                                            user_id,
                date_sub(execute_date, interval 1 day)                           last_update,
                tu.username                                                      user_name,
                tu.merchant_code                                                 merchant_code,
                Ifnull(b.order_amount, 0)                                        order_amount,
                Ifnull(a.valid_bet_amount, 0)                                    valid_bet_amount,
                Ifnull(b.bet_amount, 0)                                          bet_amount,
                Ifnull(a.profit, 0)                                              profit,
                0                                                                profit_rate,
                h.last_login                                                     last_login,
                b.last_bet                                                       last_bet,
                m.merchant_name                                                  merchant_name,
                tu.create_time                                                   register_time,
                0                                                                order_limit_20per,
                0                                                                order_limit_20to50,
                0                                                                order_limit_50to80,
                0                                                                order_limit_80per,
                Ifnull(a.profit_order_num, 0)                                    profit_order_num,
                0                                                                profit_order_rate,
                0                                                                achieve_amount_num,
                0                                                                achieve_amount_rate,
                Ifnull(b.refuse_order_num, 0)                                    refuse_order_num,
                Ifnull(b.refuse_order_amount, 0)                                 refuse_order_amount,
                Ifnull(b.cancel_order_num, 0)                                    cancel_order_num,
                Ifnull(b.cancel_order_amount, 0)                                 cancel_order_amount,
                Ifnull(od1.all_num, 0)                                           all_num,
                Ifnull(od1.greterThan2_num, 0)                                   greterThan2_num,
                Ifnull(od1.lessThan2Num, 0)                                      lessThan2_num,
                Ifnull(od1.soccer_num, 0)                                        soccer_num,
                Ifnull(od1.basketball_num, 0)                                    basketball_num,
                Ifnull(( od1.all_num - od1.soccer_num - od1.basketball_num ), 0) others_num,
                Ifnull(b.series_num, 0)                                          series_num,
                0                                                                soccer_handicap_num,
                0                                                                soccer_overunder_num,
                Ifnull(od1.basketball_handicap_num, 0)                           basketball_handicap_num,
                Ifnull(od1.basketball_overunder_num, 0)                          basketball_overunder_num,
                0                                                                soccer_handicap_main,
                0                                                                soccer_handicap_second,
                0                                                                soccer_overunder_main,
                0                                                                soccer_overunder_second,
                ta.amount                                                        balance,
                tu.user_level                                                    level_id,
                Ifnull(a.settle_amount, 0)                                       settle_amount,
                Ifnull(a.valid_order_num, 0)                                     valid_order_num,
                date_sub(execute_date, interval 1 day)                           first_bet_date
            FROM   (SELECT DISTINCT s.uid
                FROM   tybss_new.t_settle s
                WHERE  s.create_time >= start_date
                       AND s.create_time <= unix_timestamp(execute_date)*1000 - 1
                UNION
                SELECT DISTINCT o.uid
                FROM   tybss_new.t_order o
                WHERE  o.create_time >= start_date
                       AND o.create_time <= unix_timestamp(execute_date)*1000 - 1) u
               LEFT JOIN (SELECT Sum(settle_amount) settle_amount,
                                 uid,
                                 Sum(profit_amount) profit,
                                 Sum(bet_amount)    valid_bet_amount,
                                 Count(0)          valid_order_num,
                                 Count(CASE
                                         WHEN profit_amount > 0 THEN id
                                       END)         AS profit_order_num
                          FROM   tybss_new.t_settle s
                          WHERE  s.create_time >= start_date
                                 AND s.create_time <= unix_timestamp(execute_date)*1000 - 1
                                 AND s.last_settle = 1
                          GROUP  BY s.uid) a
                      ON u.uid = a.uid
               LEFT JOIN (SELECT Sum(o.order_amount_total) bet_amount,
                                 uid,
                                 Count(order_no)           order_amount,
                                 Max(create_time)          last_bet,
                                 Count(CASE
                                         WHEN order_status = 2 THEN id
                                       END)                AS cancel_order_num,
                                 Sum(CASE
                                       WHEN order_status = 2 THEN order_amount_total
                                     END)                  AS cancel_order_amount,
                                 Count(CASE
                                         WHEN order_status = 4 THEN id
                                       END)                AS refuse_order_num,
                                 Sum(CASE
                                       WHEN order_status = 4 THEN order_amount_total
                                     END)                  AS refuse_order_amount,
                                 Count(CASE
                                         WHEN series_type != 1 THEN id
                                       END)                AS series_num
                          FROM   tybss_new.t_order o
                          WHERE  o.create_time >= start_date
                                 AND o.create_time <= unix_timestamp(execute_date)*1000 - 1
                                 AND o.order_status in (0,1)
                          GROUP  BY o.uid) b
                      ON u.uid = b.uid
               LEFT JOIN (SELECT uid,
                                 Count(0)   AS all_num,
                                 Count(CASE
                                         WHEN odds_value >= 200000 THEN id
                                       END) AS greterThan2_num,
                                 Count(CASE
                                         WHEN odds_value < 200000 THEN id
                                       END) AS lessThan2Num,
                                 Count(CASE
                                         WHEN sport_id = 1 THEN id
                                       END) AS soccer_num,
                                 Count(CASE
                                         WHEN sport_id = 2 THEN id
                                       END) AS basketball_num,
                                 Count(CASE
                                         WHEN sport_id = 2
                                              AND play_id in (4,19,39,46,52,58,64,143,172) THEN id
                                       END) AS basketball_handicap_num,
                                 Count(CASE
                                         WHEN sport_id = 2
                                              AND play_id in (2,10,87,88,97,98) THEN id
                                       END) AS basketball_overunder_num
                          FROM   tybss_new.t_order_detail
                          WHERE  create_time >= start_date
                                 AND create_time <= unix_timestamp(execute_date)*1000 - 1
                          GROUP  BY uid) od1
                      ON u.uid = od1.uid
               LEFT JOIN tybss_new.t_user tu
                      ON u.uid = tu.uid
               LEFT JOIN tybss_new.t_account ta
                      ON u.uid = ta.uid
               LEFT JOIN tybss_new.t_merchant m
                      ON m.merchant_code = tu.merchant_code
               LEFT JOIN (SELECT uid,
                                 Max(login_time) last_login
                          FROM   tybss_new.t_log_history
                          GROUP  BY uid) h
                      ON u.uid = h.uid
        ON DUPLICATE KEY UPDATE
            user_id=values(user_id),
            last_update=values(last_update),
            order_amount = merchant.user_order_all.order_amount+values(order_amount),
            valid_bet_amount = merchant.user_order_all.valid_bet_amount+values(valid_bet_amount),
            bet_amount = merchant.user_order_all.bet_amount+values(bet_amount),
            profit = merchant.user_order_all.profit+values(profit),
            last_login = values(last_login),
            last_bet = values(last_bet),
            profit_order_num = merchant.user_order_all.profit_order_num+values(profit_order_num),
            refuse_order_num = merchant.user_order_all.refuse_order_num+values(refuse_order_num),
            refuse_order_amount = merchant.user_order_all.refuse_order_amount+values(refuse_order_amount),
            cancel_order_num = merchant.user_order_all.cancel_order_num+values(cancel_order_num),
            cancel_order_amount = merchant.user_order_all.cancel_order_amount+values(cancel_order_amount),
            all_num = merchant.user_order_all.all_num+values(all_num),
            greterThan2_num = merchant.user_order_all.greterThan2_num+values(greterThan2_num),
            lessThan2_num = merchant.user_order_all.lessThan2_num+values(lessThan2_num),
            soccer_num = merchant.user_order_all.soccer_num+values(soccer_num),
            basketball_num = merchant.user_order_all.basketball_num+values(basketball_num),
            others_num = merchant.user_order_all.others_num+values(others_num),
            series_num = merchant.user_order_all.series_num+values(series_num),
            basketball_handicap_num = merchant.user_order_all.basketball_handicap_num+values(basketball_handicap_num),
            basketball_overunder_num = merchant.user_order_all.basketball_overunder_num+values(basketball_overunder_num),
            basketball_overunder_num = merchant.user_order_all.basketball_overunder_num+values(basketball_overunder_num),
            balance = values(balance),
            level_id = values(level_id),
            settle_amount = merchant.user_order_all.settle_amount+values(settle_amount),
            valid_order_num = merchant.user_order_all.valid_order_num+values(valid_order_num);
        /*sql结束*/
        /*执行成功，添加日志*/
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, " p_user_order_all success!");
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

    ELSE
        /*sql结束*/
        /*执行成功，添加日志*/
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, " p_user_order_all successAfter!");
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END IF;


END//
DELIMITER ;


/**
    执行语句
    全部全量 */
CALL  p_user_order_all('2020-05-04',1);




/****************************************人员event****************************************/
drop event if EXISTS e_p_user_order_all;
drop event if EXISTS e_p_user_order_all_yesterday;
drop event if EXISTS e_p_user_order_all_before_yesterday;
CREATE EVENT p_user_order_all ON SCHEDULE EVERY 1800 SECOND DO CALL p_user_order_all(DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY),2);
CREATE EVENT p_user_order_all_yesterday ON SCHEDULE EVERY 3600 SECOND DO CALL p_user_order_all(DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY),2);
CREATE EVENT p_user_order_all_before_yesterday ON SCHEDULE EVERY 12 hour DO CALL p_user_order_all(DATE_ADD(CURRENT_DATE,INTERVAL -2 DAY),2);

drop event if EXISTS e_p_user_order_all_ALLDATA;
CREATE EVENT p_user_order_all_ALLDATA ON SCHEDULE EVERY 12 hour DO CALL p_user_order_all(DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY),1);

