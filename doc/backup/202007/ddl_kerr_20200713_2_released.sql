# merchant库执行
########## 大小判断问题导致隔日执行

DROP PROCEDURE if EXISTS merchant.p_user_order_all;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE merchant.p_user_order_all(in str_date varchar(100), start_int int(1),
                                                    special_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 1;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;
    DECLARE endTimeL BIGINT(13);
    DECLARE startTimeL BIGINT(13);
    DECLARE flag_date date;
    DECLARE total INT(10) DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("1", "错误码：", result_code, " p_user_order_all 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    SET execute_date = str_to_date(str_date, '%Y-%m-%d');
    IF special_date is not null
    THEN
        SET execute_date = str_to_date(special_date, '%Y-%m-%d');
    END IF;
    /*当全量和按日统计时，特殊处理*/
    IF start_int = 1
    THEN
        set startTimeL = 0;
        DELETE FROM user_order_all;
        UPDATE tybss_new.t_user SET profit = 0, bet_amount = 0;
    ELSE
        set startTimeL = unix_timestamp(str_date) * 1000;
    END IF;
    set endTimeL = unix_timestamp(str_date) * 1000 + 24 * 60 * 60 * 1000 - 1;

    SELECT MAX(last_update) INTO flag_date FROM user_order_all;

    IF flag_date <= date_sub(execute_date, interval 1 day) or flag_date is null or special_date is not null
    THEN
        /*新增用户相关统计数据*/
        INSERT INTO merchant.user_order_all
        SELECT u.uid                                                          user_id,
               execute_date                                                   last_update,
               tu.username                                                    user_name,
               tu.merchant_code                                               merchant_code,
               Ifnull(b.order_amount, 0)                                      order_amount,
               Ifnull(a.valid_bet_amount, 0)                                  valid_bet_amount,
               Ifnull(b.bet_amount, 0)                                        bet_amount,
               Ifnull(a.profit, 0)                                            profit,
               0                                                              profit_rate,
               h.login_time                                                   last_login,
               b.last_bet                                                     last_bet,
               m.merchant_name                                                merchant_name,
               tu.create_time                                                 register_time,
               0                                                              order_limit_20per,
               0                                                              order_limit_20to50,
               0                                                              order_limit_50to80,
               0                                                              order_limit_80per,
               Ifnull(a.profit_order_num, 0)                                  profit_order_num,
               0                                                              profit_order_rate,
               0                                                              achieve_amount_num,
               0                                                              achieve_amount_rate,
               Ifnull(b.refuse_order_num, 0)                                  refuse_order_num,
               Ifnull(b.refuse_order_amount, 0)                               refuse_order_amount,
               Ifnull(b.cancel_order_num, 0)                                  cancel_order_num,
               Ifnull(b.cancel_order_amount, 0)                               cancel_order_amount,
               Ifnull(od1.all_num, 0)                                         all_num,
               Ifnull(od1.greterThan2_num, 0)                                 greterThan2_num,
               Ifnull(od1.lessThan2Num, 0)                                    lessThan2_num,
               Ifnull(od1.soccer_num, 0)                                      soccer_num,
               Ifnull(od1.basketball_num, 0)                                  basketball_num,
               Ifnull((od1.all_num - od1.soccer_num - od1.basketball_num), 0) others_num,
               Ifnull(b.series_num, 0)                                        series_num,
               0                                                              soccer_handicap_num,
               0                                                              soccer_overunder_num,
               Ifnull(od1.basketball_handicap_num, 0)                         basketball_handicap_num,
               Ifnull(od1.basketball_overunder_num, 0)                        basketball_overunder_num,
               0                                                              soccer_handicap_main,
               0                                                              soccer_handicap_second,
               0                                                              soccer_overunder_main,
               0                                                              soccer_overunder_second,
               ta.amount                                                      balance,
               tu.user_level                                                  level_id,
               Ifnull(a.settle_amount, 0)                                     settle_amount,
               Ifnull(a.valid_order_num, 0)                                   valid_order_num,
               execute_date                                                   first_bet_date,
               m1.merchant_name                                               parent_name,
               m1.merchant_code                                               parent_code
        FROM (
                 SELECT DISTINCT iu.uid
                 FROM tybss_new.t_user iu
                 WHERE iu.create_time >= startTimeL
                   AND iu.create_time <= endTimeL
                 UNION
                 SELECT DISTINCT s.uid
                 FROM tybss_new.t_settle s
                 WHERE s.create_time >= startTimeL
                   AND s.create_time <= endTimeL
                 UNION
                 SELECT DISTINCT o.uid
                 FROM tybss_new.t_order o
                 WHERE o.create_time >= startTimeL
                   AND o.create_time <= endTimeL
             ) u
                 LEFT JOIN (SELECT Sum(settle_amount) settle_amount,
                                   uid,
                                   Sum(profit_amount) profit,
                                   Sum(bet_amount)    valid_bet_amount,
                                   Count(0)           valid_order_num,
                                   Count(CASE
                                             WHEN profit_amount > 0 THEN id
                                       END) AS        profit_order_num
                            FROM tybss_new.t_settle s
                            WHERE s.create_time >= startTimeL
                              AND s.create_time <= endTimeL
                              AND s.last_settle = 1
                            GROUP BY s.uid) a
                           ON u.uid = a.uid
                 LEFT JOIN (SELECT Sum(o.order_amount_total) bet_amount,
                                   uid,
                                   Count(order_no)           order_amount,
                                   Max(create_time)          last_bet,
                                   Count(CASE
                                             WHEN order_status = 2 THEN id
                                       END) AS               cancel_order_num,
                                   Sum(CASE
                                           WHEN order_status = 2 THEN order_amount_total
                                       END) AS               cancel_order_amount,
                                   Count(CASE
                                             WHEN order_status = 4 THEN id
                                       END) AS               refuse_order_num,
                                   Sum(CASE
                                           WHEN order_status = 4 THEN order_amount_total
                                       END) AS               refuse_order_amount,
                                   Count(CASE
                                             WHEN series_type != 1 THEN id
                                       END) AS               series_num
                            FROM tybss_new.t_order o
                            WHERE o.create_time >= startTimeL
                              AND o.create_time <= endTimeL
                              AND o.order_status in (0, 1)
                            GROUP BY o.uid) b
                           ON u.uid = b.uid
                 LEFT JOIN (SELECT uid,
                                   Count(0) AS all_num,
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
                                                 AND play_id in (4, 19, 39, 46, 52, 58, 64, 143, 172) THEN id
                                       END) AS basketball_handicap_num,
                                   Count(CASE
                                             WHEN sport_id = 2
                                                 AND play_id in (2, 10, 87, 88, 97, 98) THEN id
                                       END) AS basketball_overunder_num
                            FROM tybss_new.t_order_detail
                            WHERE create_time >= startTimeL
                              AND create_time <= endTimeL
                            GROUP BY uid) od1
                           ON u.uid = od1.uid
                 LEFT JOIN tybss_new.t_user tu
                           ON u.uid = tu.uid
                 LEFT JOIN tybss_new.t_account ta
                           ON u.uid = ta.uid
                 LEFT JOIN tybss_new.t_merchant m
                           ON m.merchant_code = tu.merchant_code
                 LEFT JOIN tybss_new.t_merchant m1
                           ON m1.id = m.parent_id
                 LEFT JOIN (SELECT uid,
                                   Max(login_time) login_time
                            FROM tybss_new.t_log_history
                            where login_time >= startTimeL
                            GROUP BY uid) h
                           ON u.uid = h.uid
        ON DUPLICATE KEY UPDATE user_id=values(user_id),
                                last_update=values(last_update),
                                order_amount = merchant.user_order_all.order_amount + values(order_amount),
                                valid_bet_amount = merchant.user_order_all.valid_bet_amount + values(valid_bet_amount),
                                bet_amount = merchant.user_order_all.bet_amount + values(bet_amount),
                                profit = merchant.user_order_all.profit + values(profit),
                                last_login = values(last_login),
                                last_bet = values(last_bet),
                                profit_order_num = merchant.user_order_all.profit_order_num + values(profit_order_num),
                                refuse_order_num = merchant.user_order_all.refuse_order_num + values(refuse_order_num),
                                refuse_order_amount = merchant.user_order_all.refuse_order_amount +
                                                      values(refuse_order_amount),
                                cancel_order_num = merchant.user_order_all.cancel_order_num + values(cancel_order_num),
                                cancel_order_amount = merchant.user_order_all.cancel_order_amount +
                                                      values(cancel_order_amount),
                                all_num = merchant.user_order_all.all_num + values(all_num),
                                greterThan2_num = merchant.user_order_all.greterThan2_num + values(greterThan2_num),
                                lessThan2_num = merchant.user_order_all.lessThan2_num + values(lessThan2_num),
                                soccer_num = merchant.user_order_all.soccer_num + values(soccer_num),
                                basketball_num = merchant.user_order_all.basketball_num + values(basketball_num),
                                others_num = merchant.user_order_all.others_num + values(others_num),
                                series_num = merchant.user_order_all.series_num + values(series_num),
                                basketball_handicap_num = merchant.user_order_all.basketball_handicap_num +
                                                          values(basketball_handicap_num),
                                basketball_overunder_num = merchant.user_order_all.basketball_overunder_num +
                                                           values(basketball_overunder_num),
                                basketball_overunder_num = merchant.user_order_all.basketball_overunder_num +
                                                           values(basketball_overunder_num),
                                balance = values(balance),
                                level_id = values(level_id),
                                settle_amount = merchant.user_order_all.settle_amount + values(settle_amount),
                                valid_order_num = merchant.user_order_all.valid_order_num + values(valid_order_num);
        commit;
        SET end_time = get_cur_ymdhms();
        SELECT count(*) INTO total FROM merchant.user_order_all where last_update = execute_date;

        SET exce_msg = CONCAT(execute_date, " p_user_order_all,userOrderAll执行成功!共:", total);

        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

        /*更新t_user的用户数据*/
        UPDATE tybss_new.t_user tu inner join (select a.valid_bet_amount, a.profit, a.valid_order_num, a.user_id
                                               from merchant.user_order_all a
                                               where a.last_update = execute_date) uo on tu.uid = uo.user_id
        set tu.bet_amount=uo.valid_bet_amount,
            tu.total_tickets=uo.valid_order_num,
            tu.profit=uo.profit;
        /*sql结束*/
        /*执行成功，添加日志*/
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, " p_user_order_all success!");
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

    ELSE
        /*sql结束*/
        /*执行成功，添加日志*/
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("userOderAll最后更新日期为:", flag_date, ",执行日期为:", execute_date, ",该日期不符合条件,故不执行存储工程!");
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END IF;


END//
DELIMITER ;

/**
    执行语句
    全部全量
CALL  p_user_order_all('2020-05-03',1,NULL);
    每日增量
CALL  p_user_order_all('2020-05-03',2,NULL);
CALL  p_user_order_all(NULL,2,'2020-05-03'); 强制执行的意思  --指定日期统计，如果当日统计过会导致累加2次的情况

比如18号，定时器传进去的是跑的时间是2020-06-17，计算的是2020-06-16 12:00:00到2020-06-17 11:59:59的数据
  */



###重新生成数据  清数据
DELETE FROM user_order_all;
UPDATE tybss_new.t_user set bet_amount = 0,total_tickets = 0,profit = 0;

##重新全量生成
CALL  p_user_order_all(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY),1,NULL);