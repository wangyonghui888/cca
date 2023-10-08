##此存储过程禁止改动!!!!!!!!!

DROP PROCEDURE if EXISTS tybss_report.p_user_order_all;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE tybss_report.p_user_order_all(in str_date varchar(100), start_int int(1),
                                                                 special_date varchar(100))
label:
BEGIN
    DECLARE task_type INT(2) DEFAULT 99;
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
    DECLARE dateOffSet INT(5) DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('1', '错误码：', result_code, ' p_user_order_all 错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    SET execute_date = str_to_date(str_date, '%Y-%m-%d');

    IF special_date is not null
    THEN
        SET execute_date = str_to_date(special_date, '%Y-%m-%d');
    END IF;
    /*当全量和按日统计时，特殊处理*/
    SELECT DATEDIFF(CURDATE(), str_date) into dateOffSet;

    if dateOffSet <= 0 and start_int = 2 then
        SET end_time = get_cur_ymdhms();
        SET result = 2;
        SET exce_msg = CONCAT('userOderAll日期偏移量为:', dateOffSet, ',执行日期为:', execute_date, ',该日期不符合条件,故不执行存储工程!');
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
        LEAVE label; # 退出存储过程
    end if;
    SELECT MAX(last_update) INTO flag_date FROM user_order_all;
    IF start_int = 1
    THEN
        set startTimeL = 0;
        truncate table tybss_report.user_order_all;
        UPDATE tybss_new.t_user SET profit = 0, bet_amount = 0;
        commit;
    ELSE
        if flag_date is null then
            set startTimeL = unix_timestamp(str_date) * 1000;
        else
            set startTimeL = unix_timestamp(flag_date) * 1000 + 24 * 60 * 60 * 1000;
        end if;
    END IF;

    set endTimeL = unix_timestamp(str_date) * 1000 + 24 * 60 * 60 * 1000 - 1;

    SELECT MAX(last_update) INTO flag_date FROM user_order_all;

    IF flag_date <= date_sub(execute_date, interval 1 day) or flag_date is null or special_date is not null
    THEN
        /*新增用户相关统计数据*/
        INSERT INTO tybss_report.user_order_all
        SELECT u.uid                                                    user_id,
               execute_date                                             last_update,
               tu.username                                              user_name,
               tu.currency_code                                         currency_code,
               tc.rate                                                  exchange_rate,
               tu.merchant_code                                         merchant_code,
               Ifnull(b.tickets, 0)                                     order_amount,
               Ifnull(a.valid_bet_amount, 0)                            valid_bet_amount,
               Ifnull(b.bet_amount, 0)                                  bet_amount,
               Ifnull(a.profit, 0)                                      profit,
               0                                                        profit_rate,
               tu.modify_time                                           last_login,
               b.last_bet                                               last_bet,
               m.merchant_name                                          merchant_name,
               tu.create_time                                           register_time,
               0                                                        order_limit_20per,
               0                                                        order_limit_20to50,
               0                                                        order_limit_50to80,
               0                                                        order_limit_80per,
               Ifnull(a.profit_order_num, 0)                            profit_order_num,
               0                                                        profit_order_rate,
               Ifnull(b.full_bet_num, 0)                                achieve_amount_num,
               0                                                        achieve_amount_rate,
               Ifnull(b.refuse_order_num, 0)                            refuse_order_num,
               Ifnull(b.refuse_order_amount, 0)                         refuse_order_amount,
               Ifnull(b.cancel_order_num, 0)                            cancel_order_num,
               Ifnull(b.cancel_order_amount, 0)                         cancel_order_amount,
               Ifnull(c.all_num, 0)                                     all_num,
               Ifnull(c.greterThan2_num, 0)                             greterThan2_num,
               Ifnull(c.lessThan2Num, 0)                                lessThan2_num,
               Ifnull(c.soccer_num, 0)                                  soccer_num,
               Ifnull(c.basketball_num, 0)                              basketball_num,
               Ifnull((c.all_num - c.soccer_num - c.basketball_num), 0) others_num,
               Ifnull(b.series_num, 0)                                  series_num,
               Ifnull(c.soccer_handicap, 0)                             soccer_handicap_num,
               Ifnull(c.soccer_overunder, 0)                            soccer_overunder_num,
               Ifnull(c.basketball_handicap_num, 0)                     basketball_handicap_num,
               Ifnull(c.basketball_overunder_num, 0)                    basketball_overunder_num,
               Ifnull(c.soccer_handicap_main, 0)                        soccer_handicap_main,
               Ifnull(c.soccer_handicap_second, 0)                      soccer_handicap_second,
               Ifnull(c.soccer_overunder_main, 0)                       soccer_overunder_main,
               Ifnull(c.soccer_overunder_second, 0)                     soccer_overunder_second,
               0                                                        balance,
               tu.user_level                                            level_id,
               Ifnull(a.settle_amount, 0)                               settle_amount,
               Ifnull(a.valid_order_num, 0)                             valid_order_num,
               execute_date                                             first_bet_date,
               m1.merchant_name                                         parent_name,
               m1.merchant_code                                         parent_code
        FROM (SELECT iu.uid
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
                AND o.create_time <= endTimeL) u
                 LEFT JOIN (SELECT Sum(settle_amount)                                settle_amount,
                                   uid,
                                   Sum(profit_amount)                                profit,
                                   Sum(bet_amount)                                   valid_bet_amount,
                                   Count(*)                                          valid_order_num,
                                   Count(CASE WHEN profit_amount > 0 THEN id END) AS profit_order_num
                            FROM tybss_new.t_settle s
                            WHERE s.create_time >= startTimeL
                              AND s.create_time <= endTimeL
                              AND s.last_settle = 1
                              and s.bet_amount > 0
                            GROUP BY s.uid) a ON u.uid = a.uid
                 LEFT JOIN (SELECT Sum(o.order_amount_total)                                               bet_amount,
                                   uid,
                                   Count(id)                                                               tickets,
                                   Max(create_time)                                                        last_bet,
                                   Count(CASE WHEN order_status = 2 THEN id END)                        AS cancel_order_num,
                                   Sum(CASE WHEN order_status = 2 THEN order_amount_total END)          AS cancel_order_amount,
                                   Count(CASE WHEN order_status = 4 THEN id END)                        AS refuse_order_num,
                                   Sum(CASE WHEN order_status = 4 THEN order_amount_total END)          AS refuse_order_amount,
                                   Count(CASE WHEN series_type <> 1 THEN id END)                        AS series_num,
                                   Count(CASE WHEN full_bet = 1 and order_status in (0, 1) THEN id END) AS full_bet_num
                            FROM tybss_new.t_order o
                            WHERE o.create_time >= startTimeL
                              AND o.create_time <= endTimeL
                            GROUP BY o.uid) b ON u.uid = b.uid
                 LEFT JOIN (SELECT od.uid,
                                   Count(*)                                                AS all_num,
                                   Count(CASE WHEN od.odds_value >= 200000 THEN od.id END) AS greterThan2_num,
                                   Count(CASE WHEN od.odds_value < 200000 THEN od.id END)  AS lessThan2Num,
                                   Count(CASE WHEN od.sport_id = 1 THEN od.id END)         AS soccer_num,
                                   Count(CASE WHEN od.sport_id = 2 THEN od.id END)         AS basketball_num,
                                   Count(CASE
                                             WHEN od.sport_id = 2 AND od.play_id in (4, 19, 39, 46, 52, 58, 64, 143, 172)
                                                 THEN od.id END)                           AS basketball_handicap_num,
                                   Count(CASE
                                             WHEN od.sport_id = 2 AND od.play_id in (2, 10, 87, 88, 97, 98)
                                                 THEN od.id END)                              basketball_overunder_num,
                                   Count(CASE
                                             WHEN od.sport_id = 1 AND od.play_id in (4, 19, 39, 46, 52, 58, 64, 143, 172)
                                                 THEN od.id END)                              soccer_handicap,

                                   Count(CASE
                                             WHEN od.sport_id = 1 AND od.play_id in (2, 10, 87, 88, 97, 98)
                                                 THEN od.id END)                              soccer_overunder,

                                   Count(CASE
                                             WHEN od.sport_id = 1 AND
                                                  od.play_id in (4, 19, 39, 46, 52, 58, 64, 143, 172) and
                                                  od.market_main = 0
                                                 THEN od.id END)                              soccer_handicap_main,
                                   Count(CASE
                                             WHEN od.sport_id = 1 AND
                                                  od.play_id in (4, 19, 39, 46, 52, 58, 64, 143, 172) and
                                                  od.market_main = 1
                                                 THEN od.id END)                              soccer_handicap_second,
                                   Count(CASE
                                             WHEN od.sport_id = 1 AND od.play_id in (2, 10, 87, 88, 97, 98) and
                                                  od.market_main = 0
                                                 THEN od.id END)                              soccer_overunder_main,
                                   Count(CASE
                                             WHEN od.sport_id = 1 AND od.play_id in (2, 10, 87, 88, 97, 98) and
                                                  od.market_main = 1
                                                 THEN od.id END)                              soccer_overunder_second
                            FROM tybss_new.t_order_detail od
                                     left join tybss_new.t_order o on o.order_no = od.order_no
                            WHERE od.create_time >= startTimeL
                              AND od.create_time <= endTimeL
                              and o.order_status in (0, 1)
                            GROUP BY od.uid) c ON u.uid = c.uid
                 LEFT JOIN tybss_new.t_user tu ON u.uid = tu.uid
                 LEFT JOIN tybss_new.t_currency_rate tc ON tu.currency_code = tc.currency_code
                 LEFT JOIN tybss_new.t_merchant m ON m.merchant_code = tu.merchant_code
                 LEFT JOIN tybss_new.t_merchant m1 ON m1.id = m.parent_id
        ON DUPLICATE KEY UPDATE user_id=values(user_id),
                                last_update=values(last_update),
                                order_amount = tybss_report.user_order_all.order_amount + values(order_amount),
/*                                valid_bet_amount = tybss_report.user_order_all.valid_bet_amount +
                                                   values(valid_bet_amount),*/
                                bet_amount = tybss_report.user_order_all.bet_amount + values(bet_amount),
/*                                profit = tybss_report.user_order_all.profit + values(profit),
*/
                                last_bet = values(last_bet),
                                profit_order_num = tybss_report.user_order_all.profit_order_num +
                                                   values(profit_order_num),
                                achieve_amount_num = tybss_report.user_order_all.achieve_amount_num +
                                                     values(achieve_amount_num),
                                refuse_order_num = tybss_report.user_order_all.refuse_order_num +
                                                   values(refuse_order_num),
                                refuse_order_amount = tybss_report.user_order_all.refuse_order_amount +
                                                      values(refuse_order_amount),
                                cancel_order_num = tybss_report.user_order_all.cancel_order_num +
                                                   values(cancel_order_num),
                                cancel_order_amount = tybss_report.user_order_all.cancel_order_amount +
                                                      values(cancel_order_amount),
                                all_num = tybss_report.user_order_all.all_num + values(all_num),
                                greterThan2_num = tybss_report.user_order_all.greterThan2_num + values(greterThan2_num),
                                lessThan2_num = tybss_report.user_order_all.lessThan2_num + values(lessThan2_num),
                                soccer_num = tybss_report.user_order_all.soccer_num + values(soccer_num),
                                basketball_num = tybss_report.user_order_all.basketball_num + values(basketball_num),
                                others_num = tybss_report.user_order_all.others_num + values(others_num),
                                series_num = tybss_report.user_order_all.series_num + values(series_num),
                                soccer_handicap_num = tybss_report.user_order_all.soccer_handicap_num +
                                                      values(soccer_handicap_num),
                                soccer_overunder_num = tybss_report.user_order_all.soccer_overunder_num +
                                                       values(soccer_overunder_num),
                                basketball_handicap_num = tybss_report.user_order_all.basketball_handicap_num +
                                                          values(basketball_handicap_num),
                                basketball_overunder_num = tybss_report.user_order_all.basketball_overunder_num +
                                                           values(basketball_overunder_num),
                                soccer_handicap_main = tybss_report.user_order_all.soccer_handicap_main +
                                                       values(soccer_handicap_main),
                                soccer_handicap_second = tybss_report.user_order_all.soccer_handicap_second +
                                                         values(soccer_handicap_second),
                                soccer_overunder_main = tybss_report.user_order_all.soccer_overunder_main +
                                                        values(soccer_overunder_main),
                                soccer_overunder_second = tybss_report.user_order_all.soccer_overunder_second +
                                                          values(soccer_overunder_second),
                                level_id = values(level_id),
                                settle_amount = tybss_report.user_order_all.settle_amount + values(settle_amount),
                                valid_order_num = tybss_report.user_order_all.valid_order_num + values(valid_order_num);
        /*sql结束*/
        /*执行成功，添加日志*/
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, 'p_user_order_all成功,', startTimeL, ',', endTimeL);
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    ELSE
        /*sql结束*/
        /*执行成功，添加日志*/
        SET end_time = get_cur_ymdhms();
        set result = 2;
        SET exce_msg = CONCAT('userOderAll最后更新日期为:', flag_date, ',执行日期为:', execute_date, ',该日期不符合条件,故不执行存储工程!');
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END IF;
END
//
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
##此存储过程禁止改动!!!!!!!!!