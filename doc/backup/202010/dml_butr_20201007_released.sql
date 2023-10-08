alter table tybss_report.r_match_bet_info add column tournament_level int(5);
alter table tybss_report.r_merchant_match_bet_info add column tournament_level  int(5);


alter table tybss_report.r_market_bet_info add column begin_time bigint(20);
alter table tybss_report.r_market_bet_info add column tournament_level  int(5);
alter table tybss_report.r_merchant_market_bet_info add column begin_time bigint(20);
alter table tybss_report.r_merchant_market_bet_info add column tournament_level  int(5);
alter table tybss_report.r_market_bet_info add column match_status  varchar(2);
alter table tybss_report.r_merchant_market_bet_info add column match_status  varchar(2);

DROP VIEW IF EXISTS tybss_report.view_r_order_match_detail;
CREATE ALGORITHM = UNDEFINED DEFINER =`root`@`localhost` SQL SECURITY DEFINER VIEW `tybss_report`.`view_r_order_match_detail` AS
select uuid()                                                                        AS `id`,
       date_format(from_unixtime((`smi`.`begin_time` / 1000)), '%Y-%m')              AS `dt`,
       `smi`.`id`                                                                    AS `match_id`,
       `smi`.`sport_id`                                                              AS `sport_id`,
       `smi`.`tournament_id`                                                         AS `tournament_id`,
       `smi`.`third_match_id`                                                        AS `third_match_id`,
       `smi`.`neutral_ground`                                                        AS `neutral_ground`,
       NULL                                                                          AS `can_parlay`,
       `smi`.`operate_match_status`                                                  AS `operate_match_status`,
       `smi`.`begin_time`                                                            AS `begin_time`,
       NULL                                                                          AS `visible`,
       `smi`.`match_manage_id`                                                       AS `match_manage_id`,
       `smi`.`risk_manager_code`                                                     AS `risk_manager_code`,
       NULL                                                                          AS `match_data_source_code`,
       NULL                                                                          AS `match_remark`,
       `smi`.`name_code`                                                             AS `name_code`,
       `smi`.`pre_match_business`                                                    AS `pre_match_business`,
       `smi`.`live_odd_business`                                                     AS `live_odd_business`,
       `smi`.`match_over`                                                            AS `match_over`,
       NULL                                                                          AS `home_away_info`,
       `smi`.`score`                                                                 AS `score`,
       `smi`.`match_period_id`                                                       AS `match_period_id`,
       `smi`.`match_status`                                                          AS `match_status`,
       `smi`.`tournament_level`                                                          AS `tournament_level`,
       NULL                                                                          AS `home_extra_time_score`,
       NULL                                                                          AS `away_extra_time_score`,
       NULL                                                                          AS `home_penalty_score`,
       NULL                                                                          AS `away_penalty_score`,
       NULL                                                                          AS `home_red_card_score`,
       NULL                                                                          AS `away_red_card_score`,
       `smi`.`remaining_time`                                                        AS `remaining_time`,
       `smi`.`seconds_match_start`                                                   AS `seconds_match_start`,
       NULL                                                                          AS `home_yellow_card_score`,
       NULL                                                                          AS `away_yellow_card_score`,
       NULL                                                                          AS `home_corner_score`,
       NULL                                                                          AS `away_corner_score`,
       NULL                                                                          AS `extra_time_score`,
       NULL                                                                          AS `penalty_score`,
       NULL                                                                          AS `match_score`,
       NULL                                                                          AS `first_half_score`,
       NULL                                                                          AS `second_half_score`,
       NULL                                                                          AS `match_status2`,
       `smi`.`match_handicap_status`                                                 AS `match_handicap_status`,
       NULL                                                                          AS `operation`,
       NULL                                                                          AS `operate_time`,
       `tod`.`id`                                                                    AS `bet_id`,
       `tod`.`bet_no`                                                                AS `bet_no`,
       `tod`.`order_no`                                                              AS `order_no`,
       `tod`.`play_options_id`                                                       AS `play_options_id`,
       `tod`.`sport_name`                                                            AS `sport_name`,
       `tod`.`play_id`                                                               AS `play_id`,
       `tod`.`play_name`                                                             AS `play_name`,
       `tod`.`match_name`                                                            AS `match_name`,
       `tod`.`match_type`                                                            AS `match_type`,
       `tod`.`bet_time`                                                              AS `bet_time`,
       `tod`.`market_type`                                                           AS `market_type`,
       `tod`.`market_value`                                                          AS `market_value`,
       `tod`.`match_info`                                                            AS `match_info`,
       `tod`.`bet_amount`                                                            AS `bet_amount`,
       NULL                                                                          AS `detail_odds_value`,
       `tod`.`market_type_finally`                                                   AS `market_type_finally`,
       `tod`.`odd_finally`                                                           AS `odd_finally`,
       `tod`.`accept_bet_odds`                                                       AS `accept_bet_odds`,
       `tod`.`max_win_amount`                                                        AS `max_win_amount`,
       `tod`.`bet_status`                                                            AS `bet_status`,
       `tod`.`score_benchmark`                                                       AS `score_benchmark`,
       `tod`.`play_options`                                                          AS `play_options`,
       `tod`.`del_flag`                                                              AS `del_flag`,
       `tod`.`match_process_id`                                                      AS `match_process_id`,
       `tod`.`remark`                                                                AS `remark`,
       `tod`.`is_result`                                                             AS `is_result`,
       `tod`.`bet_result`                                                            AS `bet_result`,
       `tod`.`play_option_name`                                                      AS `play_option_name`,
       `tod`.`bet_all_result`                                                        AS `bet_all_result`,
       `tod`.`settle_score`                                                          AS `settle_score`,
       `smo`.`odds_type`                                                             AS `odds_type`,
       `smo`.`addition1`                                                             AS `addition1`,
       `smo`.`addition2`                                                             AS `addition2`,
       `smo`.`addition3`                                                             AS `addition3`,
       `smo`.`addition4`                                                             AS `addition4`,
       `smo`.`addition5`                                                             AS `addition5`,
       `smo`.`addition6`                                                             AS `addition6`,
       `smo`.`target_side`                                                           AS `target_side`,
       `smo`.
           `name_expression_value`                                                   AS `name_expression_value`,
       `smo`.`pa_odds_value`                                                         AS `pa_odds_value`,
       `smo`.`odds_stall`                                                            AS `odds_stall`,
       `smo`.`odds_value`                                                            AS `odds_value`,
       `smo`.`odds_fields_template_id`                                               AS `odds_fields_template_id`,
       `smo`.`original_odds_value`                                                   AS `original_odds_value`,
       `smo`.`order_odds`                                                            AS `order_odds`,
       NULL                                                                          AS `odds_data_source_code`,
       `smo`.`third_odds_field_source_id`                                            AS `third_odds_field_source_id`,
       NULL                                                                          AS `odds_remark`,
       `smo`.`odds_status`                                                           AS `odds_status`,
       `smo`.`original_market_value`                                                 AS `original_market_value`,
       `smo`.`update_time`                                                           AS `update_time`,
       `tu`.`uid`                                                                    AS `uid`,
       `tu`.`username`                                                               AS `username`,
       `tu`.`real_name`                                                              AS `real_name`,
       `tu`.`phone`                                                                  AS `phone`,
       `tu`.`id_card`                                                                AS `id_card`,
       `tu`.`email`                                                                  AS `email`,
       `tu`.`user_level`                                                             AS `user_level`,
       `tu`.`ip_address`                                                             AS `ip_address`,
       `tu`.`currency_code`                                                          AS `currency_code`,
       `tu`.`merchant_code`                                                          AS `merchant_code`,
       `tm`.`id`                                                                     AS `merchant_id`,
       `tm`.`merchant_name`                                                          AS `merchant_name`,
       `tm`.`level`                                                                  AS `level`,
       `tm`.`parent_id`                                                              AS `parent_id`,
       `tm`.`sport_list`                                                             AS `sport_list`,
       `tm`.`max_bet`                                                                AS `max_bet`,
       `tm`.`url`                                                                    AS `url`,
       `tm`.`transfer_mode`                                                          AS `transfer_mode`,
       `tm`.`create_time`                                                            AS `merchant_create_time`,
       NULL                                                                          AS `bonus_mode`,
       `tm`.`callback_url`                                                           AS `callback_url`,
       `tm`.`topic`                                                                  AS `topic`,
       NULL                                                                          AS `panda_order_test`,
       `tm`.`agent_level`                                                            AS `agent_level`,
       `tm`.`email`                                                                  AS `merchant_email`,
       `tm`.`contact`                                                                AS `contact`,
       `tm`.`phone`                                                                  AS `merchant_phone`,
       `tm`.`logo`                                                                   AS `logo`,
       `tm`.`fee_json`                                                               AS `fee_json`,
       `tm`.`address`                                                                AS `address`,
       NULL                                                                          AS `merchant_currency`,
       `tm`.`rate`                                                                   AS `rate`,
       `tm`.`file_path`                                                              AS `file_path`,
       `tm`.`country`                                                                AS `country`,
       `tm`.`province`                                                               AS `province`,
       NULL                                                                          AS `liveness`,
       NULL                                                                          AS `first_bet_date`,
       `tor`.`series_type`                                                           AS `series_type`,
       `tor`.`order_status`                                                          AS `order_status`,
       `smo`.`market_id`                                                             AS `market_id`,
       `tod`.`market_main`                                                           AS `market_main`,
       date_format(from_unixtime((`tor`.`create_time` / 1000)), '%Y-%m-%d %H:%m:%s') AS `sync_time`,
       `sl1`.`zs`                                                                    AS `tournament_name`
from (
      (
          (
              (
                  (
                      (
                          (
                              (
                                  (`tybss_new`.`t_order_detail` `tod` left join `tybss_new`.`s_match_info` `smi` on
                                      ((`smi`.`id` = `tod`.`match_id`)))
                                      left join `tybss_new`.`t_order` `tor` on ((`tor`.`order_no` = `tod`.`order_no`)))
                                  left join `tybss_new`.`s_market_odds` `smo` on ((`tod`.`play_options_id` = `smo`.`id`)))
                              left join `tybss_new`.`t_user` `tu` on ((`tu`.`uid` = `tod`.`uid`)))
                          left join `tybss_new`.`t_merchant` `tm` on ((`tm`.`merchant_code` = `tu`.`merchant_code`)))
                      left join `tybss_new`.`s_betting_play` `sbt` on ((`sbt`.`id` = `tod`.`play_id`)))
                  left join `tybss_new`.`s_tournament` `st` on ((`st`.`id` = `tod`.`tournament_id`)))
              left join `tybss_new`.`s_language` `sl` on ((`sl`.`name_code` = `sbt`.`play_name_code`)))
         left join `tybss_new`.`s_language` `sl1` on ((`sl1`.`name_code` = `st`.`name_code`)));



/*存储过程*/

DROP PROCEDURE IF EXISTS p_r_match_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_match_bet_info(in execute_date varchar(100))
BEGIN

    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 2;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_match_bet_info成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_match_bet_info错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

/*sql开始*/
    REPLACE INTO tybss_report.r_match_bet_info
    SELECT d.match_id                                                                               id,
           d.match_id,
           MIN(d.match_info)                                                                        match_info,
           MIN(d.sport_id)                                                                          sport_id,
           MIN(d.sport_name)                                                                        sport_name,
           MIN(d.begin_time)                                                                        begin_time,
           MIN(d.match_status)                                                                      match_status,
           IFNULL(SUM(d.bet_amount) / 100, 0)                                                       bet_amount,
           IFNULL(SUM(s.bet_amount) / 100, 0)                                                       valid_bet_amount,
           IFNULL(SUM(s.settle_amount) / 100, 0)                                                    settle_amount,
           IFNULL(SUM(s.profit_amount) / 100, 0)                                                    profit,
           MIN(d.tournament_id)                                                                     tournament_id,
           COUNT(DISTINCT d.uid)                                                                    user_amount,
           COUNT(DISTINCT d.order_no)                                                               order_amount,
           IFNULL(CONVERT(SUM(s.profit_amount / 100) / SUM(d.bet_amount / 100), DECIMAL(32, 4)), 0) profit_rate,
           COUNT(DISTINCT d.play_id)                                                                play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                                  elasticsearch_id,
           MIN(d.tournament_name)                                                                   tournamentName,
           MIN(d.tournament_level)                                                                   tournamentLevel
    FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.series_type = 1
      AND d.order_status in (0, 1)
    GROUP BY d.match_id;
    /*sql结束*/

/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_match_bet_info success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_match_bet_info(@execute_date);*/

/*存储过程*/

DROP PROCEDURE IF EXISTS p_r_merchant_market_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_merchant_market_bet_info(in execute_date varchar(100))
BEGIN

    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 3;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_merchant_market_bet_info 成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, "p_r_merchant_market_bet_info 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    REPLACE INTO tybss_report.r_merchant_market_bet_info
    SELECT concat(od.match_id, od.play_id, od.market_id, n.merchant_code)                                   id,
           od.match_id,
           od.play_id,
           od.market_id,
           min(od.match_info)                                                                               match_info,
           min(od.play_name)                                                                                play_name,
           min(od.market_value)                                                                             market_value,
           min(od.market_type)                                                                              market_type,
           IFNULL(sum(od.bet_amount) / 100, 0)                                                              bet_amount,
           IFNULL(sum(o.order_amount_total) / 100, 0)                                                       order_amount_total,
           IFNULL(sum(s.bet_amount) / 100, 0)                                                               valid_bet_amount,
           IFNULL(sum(s.settle_amount) / 100, 0)                                                            settle_amount,
           IFNULL(sum(s.profit_amount) / 100, 0)                                                            profit,
           IFNULL(CONVERT(sum(s.profit_amount / 100) / sum(o.order_amount_total / 100), DECIMAL(32, 4)), 0) profitRate,
           count(DISTINCT od.uid)                                                                           user_amount,
           count(DISTINCT od.order_no)                                                                      order_amount,
           UNIX_TIMESTAMP(current_timestamp(3))                                                             elasticsearch_id,
           n.merchant_code,
           n.merchant_name,
           m.begin_time beging_time,
           min(od.tournament_level) tournament_level,
           m.match_status                                                                      match_status
    FROM tybss_new.t_order_detail od
             LEFT JOIN tybss_new.t_order o ON od.order_no = o.order_no
             LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no
             LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
             left join tybss_new.t_user u on u.uid = o.uid
             left join tybss_new.t_merchant n on n.merchant_code = u.merchant_code
    WHERE o.series_type = 1
      AND o.order_status in (0, 1)
      AND m.begin_time >= (unix_timestamp(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND m.begin_time <= (unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
    GROUP BY od.match_id, od.play_id, od.market_id, n.merchant_code;


      REPLACE INTO tybss_report.r_merchant_market_bet_info
    SELECT concat(od.match_id, od.play_id, od.market_id, mn.merchant_code)                                   id,
           od.match_id,
           od.play_id,
           od.market_id,
           min(od.match_info)                                                                               match_info,
           min(od.play_name)                                                                                play_name,
           min(od.market_value)                                                                             market_value,
           min(od.market_type)                                                                              market_type,
           IFNULL(sum(od.bet_amount) / 100, 0)                                                              bet_amount,
           IFNULL(sum(o.order_amount_total) / 100, 0)                                                       order_amount_total,
           IFNULL(sum(s.bet_amount) / 100, 0)                                                               valid_bet_amount,
           IFNULL(sum(s.settle_amount) / 100, 0)                                                            settle_amount,
           IFNULL(sum(s.profit_amount) / 100, 0)                                                            profit,
           IFNULL(CONVERT(sum(s.profit_amount / 100) / sum(o.order_amount_total / 100), DECIMAL(32, 4)), 0) profitRate,
           count(DISTINCT od.uid)                                                                           user_amount,
           count(DISTINCT od.order_no)                                                                      order_amount,
           UNIX_TIMESTAMP(current_timestamp(3))                                                             elasticsearch_id,
           mn.merchant_code,
           mn.merchant_name,
           m.begin_time beging_time,
           min(od.tournament_level) tournament_level,
           m.match_status                                                                      match_status
    FROM tybss_new.t_order_detail od
             LEFT JOIN tybss_new.t_order o ON od.order_no = o.order_no
             LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no
             LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
             left join tybss_new.t_user u on u.uid = o.uid
             left join tybss_new.t_merchant n on n.merchant_code = u.merchant_code
             left join tybss_new.t_merchant mn on n.parent_id = mn.id
    WHERE o.series_type = 1
      AND o.order_status in (0, 1)
      AND m.begin_time >= (unix_timestamp(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND m.begin_time <= (unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
    GROUP BY od.match_id, od.play_id, od.market_id, mn.merchant_code;


/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_merchant_market_bet_info success!");


    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

DROP PROCEDURE IF EXISTS p_r_market_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_market_bet_info(in execute_date varchar(100))
BEGIN

    DECLARE task_type INT(2) DEFAULT 1;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_market_bet_info成功';
    DECLARE msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_market_bet_info错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    REPLACE INTO tybss_report.r_market_bet_info
    SELECT concat(od.match_id, od.play_id, od.market_id)                                                    id,
           od.match_id,
           od.play_id,
           od.market_id,
           min(od.match_info)                                                                               match_info,
           min(od.play_name)                                                                                play_name,
           min(od.market_value)                                                                             market_value,
           min(od.market_type)                                                                              market_type,
           IFNULL(sum(od.bet_amount) / 100, 0)                                                              bet_amount,
           IFNULL(sum(o.order_amount_total) / 100, 0)                                                       order_amount_total,
           IFNULL(sum(s.bet_amount) / 100, 0)                                                               valid_bet_amount,
           IFNULL(sum(s.settle_amount) / 100, 0)                                                            settle_amount,
           IFNULL(sum(s.profit_amount) / 100, 0)                                                            profit,
           IFNULL(CONVERT(sum(s.profit_amount / 100) / sum(o.order_amount_total / 100), DECIMAL(32, 4)), 0) profitRate,
           IFNULL(count(DISTINCT od.uid), 0)                                                                user_amount,
           IFNULL(count(DISTINCT od.order_no), 0)                                                           order_amount,
           UNIX_TIMESTAMP(current_timestamp(3))                                                             elasticsearch_id,
             m.begin_time beging_time,
           min(od.tournament_level) tournament_level,
           m.match_status                                                                      match_status
    FROM tybss_new.t_order_detail od
             LEFT JOIN tybss_new.t_order o ON od.order_no = o.order_no
             LEFT JOIN tybss_new.t_settle s ON s.order_no = od.order_no
             LEFT JOIN tybss_new.s_match_info m ON od.match_id = m.id
    WHERE o.series_type = 1
      AND o.order_status in (0, 1)
      AND m.begin_time >= (unix_timestamp(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND m.begin_time <= (unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
    GROUP BY od.match_id, od.play_id, od.market_id;

    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_market_bet_info success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_market_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_market_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_market_bet_info(@execute_date);*/

/*存储过程*/

DROP PROCEDURE IF EXISTS p_r_merchant_match_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_merchant_match_bet_info(in execute_date varchar(100))
BEGIN

    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 4;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_merchant_match_bet_info成功';
    DECLARE msg TEXT;

/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT("错误码：", result_code, execute_date, " p_r_merchant_match_bet_info 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    REPLACE INTO tybss_report.r_merchant_match_bet_info
    SELECT CONCAT(d.match_id, d.merchant_code)                                                      id,
           d.match_id,
           MIN(d.match_info)                                                                        match_info,
           MIN(d.sport_id)                                                                          sport_id,
           MIN(d.sport_name)                                                                        sport_name,
           MIN(d.begin_time)                                                                        begin_time,
           MIN(d.match_status)                                                                      match_status,
           IFNULL(SUM(d.bet_amount) / 100, 0)                                                       bet_amount,
           IFNULL(SUM(s.bet_amount) / 100, 0)                                                       valid_bet_amount,
           IFNULL(SUM(s.settle_amount) / 100, 0)                                                    settle_amount,
           IFNULL(SUM(s.profit_amount) / 100, 0)                                                    profit,
           MIN(d.tournament_id)                                                                     tournament_id,
           COUNT(DISTINCT d.uid)                                                                    user_amount,
           COUNT(DISTINCT d.order_no)                                                               order_amount,
           IFNULL(CONVERT(SUM(s.profit_amount / 100) / SUM(d.bet_amount / 100), DECIMAL(32, 4)), 0) profit_rate,
           COUNT(DISTINCT d.play_id)                                                                play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                                  elasticsearch_id,
           MIN(d.tournament_name)                                                                   tournamentName,
           d.merchant_code,
           d.merchant_name,
           MIN(d.tournament_level)                                                                   tournamentLevel
    FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.series_type = 1
      AND d.order_status in (0, 1)
    GROUP BY d.match_id, d.merchant_code;


    REPLACE INTO tybss_report.r_merchant_match_bet_info
    SELECT CONCAT(d.match_id, mn.merchant_code)                                                      id,
           d.match_id,
           MIN(d.match_info)                                                                        match_info,
           MIN(d.sport_id)                                                                          sport_id,
           MIN(d.sport_name)                                                                        sport_name,
           MIN(d.begin_time)                                                                        begin_time,
           MIN(d.match_status)                                                                      match_status,
           IFNULL(SUM(d.bet_amount) / 100, 0)                                                       bet_amount,
           IFNULL(SUM(s.bet_amount) / 100, 0)                                                       valid_bet_amount,
           IFNULL(SUM(s.settle_amount) / 100, 0)                                                    settle_amount,
           IFNULL(SUM(s.profit_amount) / 100, 0)                                                    profit,
           MIN(d.tournament_id)                                                                     tournament_id,
           COUNT(DISTINCT d.uid)                                                                    user_amount,
           COUNT(DISTINCT d.order_no)                                                               order_amount,
           IFNULL(CONVERT(SUM(s.profit_amount / 100) / SUM(d.bet_amount / 100), DECIMAL(32, 4)), 0) profit_rate,
           COUNT(DISTINCT d.play_id)                                                                play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                                  elasticsearch_id,
           MIN(d.tournament_name)                                                                   tournamentName,
           mn.merchant_code,
          mn.merchant_name,
           MIN(d.tournament_level)                                                                   tournamentLevel
    FROM view_r_order_match_detail d
             LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
             left join tybss_new.t_merchant mn on d.parent_id = mn.id
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.series_type = 1
      AND d.order_status in (0, 1)
    GROUP BY d.match_id, mn.merchant_code;
    /*sql结束*/

/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(current_date, " p_r_merchant_match_bet_info success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_match_bet_info(@execute_date);*/


CALL  p_r_merchant_loop_exc('2020-05-01','2020-10-20','p_r_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-10-20','p_r_merchant_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-10-20','p_r_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-10-20','p_r_merchant_market_bet_info','1');