DROP TABLE if EXISTS tybss_report.r_bc_match_bet_info;

CREATE TABLE tybss_report.`r_bc_match_bet_info`
(
    `id`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '赛事ID',
    `match_info_zs`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '赛事对阵zs',
    `match_info_en`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '赛事对阵en',
    `sport_id`       int(5)                                                        DEFAULT '0' COMMENT '运动种类编号',
    `begin_time`     bigint(20)                                                    DEFAULT NULL COMMENT '开赛时间',
    `bet_amount`     decimal(32, 2)                                                DEFAULT NULL COMMENT '投注金额',
    `profit_amount`  decimal(32, 2)                                                DEFAULT NULL COMMENT '中奖金额',
    `pre_profit`     decimal(32, 2)                                                DEFAULT NULL COMMENT '赛前盘盈利',
    `rolling_profit` decimal(32, 2)                                                DEFAULT NULL COMMENT '滚球盘盈利',
    `profit_rate`    decimal(32, 2)                                                DEFAULT NULL,
    `time`           bigint(20)                                                    DEFAULT '0' COMMENT '日期',
    `updated_time`   bigint(20)                                                   NOT NULL COMMENT '根据此字段增量同步到elasticsearch'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='报表-BC赛事投注统计信息'
  ROW_FORMAT = DYNAMIC;
/*存储过程*/
DROP PROCEDURE IF EXISTS tybss_report.p_r_bc_match_bet_info;
DELIMITER //
CREATE PROCEDURE tybss_report.p_r_bc_match_bet_info(in execute_date varchar(100))
BEGIN
    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 5;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE executetimeL BIGINT(10);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_bc_match_bet_info成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_bc_match_bet_info错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set executetimeL = DATE_FORMAT(execute_date, '%Y%m%d');

/*sql开始*/
    DELETE FROM tybss_report.r_bc_match_bet_info WHERE time = executetimeL;
    REPLACE INTO tybss_report.r_bc_match_bet_info
    SELECT s.id,
           s.match_zs,
           s.match_en,
           s.sport_id,
           s.begin_time,
           s.bet_amount,
           s.profit_amount,
           s.pre_profit,
           s.rolling_profit,
           s.profit_rate,
           executetimeL                  AS time,
           UNIX_TIMESTAMP(
                   CURRENT_TIMESTAMP(3)) AS update_time
    FROM (
             SELECT g.id,
                    min(g.match_zs)                                                             match_zs,
                    min(g.match_en)                                                             match_en,
                    min(g.sport_id)                                                             sport_id,
                    min(g.begin_time)                                                           begin_time,
                    sum(g.bet_amount)                                                           bet_amount,
                    sum(g.profit_amount)                                                        profit_amount,
                    sum(g.pre_profit)                                                           pre_profit,
                    sum(g.rolling_profit)                                                       rolling_profit,
                    (
                        sum(g.pre_profit) + sum(g.rolling_profit)) / sum(g.bet_amount) * 100 AS profit_rate
             FROM (
                      SELECT s.id,
                             s.sport_id,
                             CONCAT(l.zs, ' vs ', l1.zs)                                                           match_zs,
                             CONCAT(l.en, ' vs ', l1.en)                                                           match_en,
                             s.begin_time,
                             sum(o.order_amount_total / 100 * r.rate)                                              bet_amount,
                             sum(case when t.profit_amount > 0 then t.profit_amount + o.order_amount_total else 0 end) *
                             rate /
                             100                                                                                   profit_amount,
                             sum(CASE d.match_type
                                     WHEN 1 THEN t.profit_amount * - 1 * r.rate / 100
                                     ELSE 0 END) AS                                                                pre_profit,
                             sum(CASE d.match_type
                                     WHEN 2 THEN t.profit_amount * - 1 * r.rate / 100
                                     ELSE 0 END) AS                                                                rolling_profit
                      FROM tybss_new.t_order_detail d
                               LEFT JOIN tybss_new.t_order o ON o.order_no = d.order_no
                               LEFT JOIN tybss_new.bs_order_rate r
                                         ON r.START <= o.create_time AND r.END >= o.create_time
                               LEFT JOIN tybss_new.t_settle t ON t.order_no = d.order_no
                          AND t.last_settle = 1
                               LEFT JOIN tybss_new.s_match_info s ON s.id = d.match_id
                               LEFT JOIN tybss_new.s_language l ON l.name_code = s.home_name_code
                               LEFT JOIN tybss_new.s_language l1 ON l1.name_code = s.away_name_code
                      WHERE o.series_type = 1
                        and d.odds_data_sourse = 'BC'

                        AND o.order_status = 1
                        AND s.begin_time >= UNIX_TIMESTAMP(executetimeL) * 1000
                        AND s.begin_time <= unix_timestamp(date_add(executetimeL, INTERVAL 1 DAY)) * 1000 - 1
                      GROUP BY s.id,
                               r.rate
                  ) g
             GROUP BY g.id
         ) s;
    /*sql结束*/

/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_bc_match_bet_info success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

#循环调用 日期类存储过程   start_date<=目标<=end_date
DROP PROCEDURE IF EXISTS tybss_report.p_r_merchant_loop_exc;

DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_merchant_loop_exc(in start_date varchar(100), end_date varchar(100),
                                                         procedure_name varchar(100), type_num varchar(100))
BEGIN
    DECLARE startDate date DEFAULT NOW();
    DECLARE endDate date DEFAULT NOW();
    set startDate = DATE_FORMAT(start_date, '%Y-%m-%d');
    set endDate = DATE_FORMAT(end_date, '%Y-%m-%d');
#日统计
    IF type_num = '1' THEN
        WHILE startDate <= endDate
            DO
                IF 'p_r_merchant_finance_day' = procedure_name THEN
                    CALL p_r_merchant_finance_day(startDate);

                ELSEIF 'p_r_merchant_order_day' = procedure_name THEN
                    CALL p_r_merchant_order_day(startDate);

                ELSEIF 'p_r_merchant_sport_order_day' = procedure_name THEN
                    CALL p_r_merchant_sport_order_day(startDate);

                ELSEIF 'p_r_user_order_day' = procedure_name THEN
                    CALL p_r_user_order_day(startDate);

                ELSEIF 'p_r_user_sport_order_day' = procedure_name THEN
                    CALL p_r_user_sport_order_day(startDate);

                ELSEIF 'p_r_market_bet_info' = procedure_name THEN
                    CALL p_r_market_bet_info(startDate);

                ELSEIF 'p_r_match_bet_info' = procedure_name THEN
                    CALL p_r_match_bet_info(startDate);

                ELSEIF 'p_r_merchant_market_bet_info' = procedure_name THEN
                    CALL p_r_merchant_market_bet_info(startDate);

                ELSEIF 'p_r_merchant_match_bet_info' = procedure_name THEN
                    CALL p_r_merchant_match_bet_info(startDate);

                ELSEIF 'p_r_bc_match_bet_info' = procedure_name THEN
                    CALL p_r_bc_match_bet_info(startDate);

                END IF;

                set startDate = DATE_ADD(startDate, INTERVAL 1 DAY);
            END WHILE;
    END IF;
#周统计
    IF type_num = '2' THEN
        WHILE DATE_FORMAT(date_add(startDate, INTERVAL -1 day), '%Y%U') <=
              DATE_FORMAT(date_add(endDate, INTERVAL -1 day), '%Y%U')
            DO

                IF 'p_r_merchant_order_week' = procedure_name THEN
                    CALL p_r_merchant_order_week(startDate);

                ELSEIF 'p_r_merchant_sport_order_week' = procedure_name THEN
                    CALL p_r_merchant_sport_order_week(startDate);

                ELSEIF 'p_r_user_order_week' = procedure_name THEN
                    CALL p_r_user_order_week(startDate);

                ELSEIF 'p_r_user_sport_order_week' = procedure_name THEN
                    CALL p_r_user_sport_order_week(startDate);

                END IF;

                set startDate = DATE_ADD(startDate, INTERVAL 1 WEEK);
            END WHILE;
    END IF;
#月统计
    IF type_num = '3' THEN
        WHILE DATE_FORMAT(startDate, '%Y-%m') <= DATE_FORMAT(endDate, '%Y-%m')
            DO
                IF 'p_r_merchant_finance_month' = procedure_name THEN
                    CALL p_r_merchant_finance_month(startDate);

                ELSEIF 'p_r_merchant_order_month' = procedure_name THEN
                    CALL p_r_merchant_order_month(startDate);

                ELSEIF 'p_r_merchant_sport_order_month' = procedure_name THEN
                    CALL p_r_merchant_sport_order_month(startDate);

                ELSEIF 'p_r_user_order_month' = procedure_name THEN
                    CALL p_r_user_order_month(startDate);

                ELSEIF 'p_r_user_sport_order_month' = procedure_name THEN
                    CALL p_r_user_sport_order_month(startDate);
                END IF;

                set startDate = DATE_ADD(startDate, INTERVAL 1 MONTH);
            END WHILE;
    END IF;

END//
DELIMITER ;


/****************************************BC赛事报表****************************************/
drop event if EXISTS e_p_r_bc_match_bet_info;
drop event if EXISTS e_p_r_bc_match_bet_info_yesterday;
drop event if EXISTS e_p_r_bc_match_bet_info_before_yesterday;
CREATE EVENT e_p_r_bc_match_bet_info ON SCHEDULE EVERY 2 hour STARTS '2020-06-18 00:47:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_bc_match_bet_info(CURRENT_DATE);
CREATE EVENT e_p_r_bc_match_bet_info_yesterday ON SCHEDULE EVERY 12 hour  STARTS '2020-06-18 00:52:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_bc_match_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -1 DAY));
CREATE EVENT e_p_r_bc_match_bet_info_before_yesterday ON SCHEDULE EVERY 12   hour STARTS '2020-06-18 12:53:00' ON COMPLETION PRESERVE ENABLE DO CALL p_r_bc_match_bet_info(DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY));
CALL tybss_report.p_r_merchant_loop_exc('2020-06-01', '2020-09-30', 'p_r_bc_match_bet_info', '1');