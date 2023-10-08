ALTER TABLE `tybss_report`.`r_match_settle_bet_info`
    MODIFY COLUMN `settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '已结算注单总金额' AFTER `match_status`,
    MODIFY COLUMN `settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '结算金额' AFTER `settle_bet_amount`,
    MODIFY COLUMN `pre_settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘注单总金额' AFTER `tournament_level`,
    MODIFY COLUMN `pre_settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘结算总金额' AFTER `pre_settle_bet_amount`,
    MODIFY COLUMN `pre_settle_profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘盈利总金额' AFTER `pre_settle_amount`,
    MODIFY COLUMN `live_settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球注单总金额' AFTER `pre_order_count`,
    MODIFY COLUMN `live_settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球结算总金额' AFTER `live_settle_bet_amount`,
    MODIFY COLUMN `live_settle_profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球盈利总金额' AFTER `live_settle_amount`;



ALTER TABLE `tybss_report`.`r_tournament_settle_bet_info`
    MODIFY COLUMN `settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '已结算注单总金额' AFTER `tournament_id`,
    MODIFY COLUMN `settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '结算金额' AFTER `settle_bet_amount`,
    MODIFY COLUMN `profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '盈利金额' AFTER `settle_amount`,
    MODIFY COLUMN `pre_settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘注单总金额' AFTER `settle_order_count`,
    MODIFY COLUMN `pre_settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘结算总金额' AFTER `pre_settle_bet_amount`,
    MODIFY COLUMN `pre_settle_profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '早盘盈利总金额' AFTER `pre_settle_amount`,
    MODIFY COLUMN `live_settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球注单总金额' AFTER `pre_order_count`,
    MODIFY COLUMN `live_settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球结算总金额' AFTER `live_settle_bet_amount`,
    MODIFY COLUMN `live_settle_profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '滚球盈利总金额' AFTER `live_settle_amount`;




ALTER TABLE `tybss_report`.`r_match_market_settle_day_utc8`
    MODIFY COLUMN `settle_bet_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '已结算注单总金额' AFTER `match_type`,
    MODIFY COLUMN `settle_amount` decimal(32, 4) NULL DEFAULT NULL COMMENT '结算金额' AFTER `settle_bet_amount`,
    MODIFY COLUMN `profit` decimal(32, 4) NULL DEFAULT NULL COMMENT '盈利金额' AFTER `settle_amount`;



alter table tybss_report.r_merchant_match_bet_info
  MODIFY  column parlay_profit_rate decimal(32, 4) NULL COMMENT '串关盈利率';

 alter table tybss_report.r_match_bet_info
    MODIFY column parlay_profit_rate decimal(32, 4) NULL COMMENT '串关盈利率';


/*
 优化动成拼接脚本
 */
DROP PROCEDURE IF EXISTS `tybss_report`.`p_r_match_loop_exc`;


DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_r_match_loop_exc`(IN `start_date` VARCHAR(100), IN `end_date` VARCHAR(100), IN `procedure_name` VARCHAR(100))
BEGIN
    DECLARE startDate date DEFAULT NOW();
    DECLARE endDate date DEFAULT NOW();
	DECLARE SQL_FOR_SELECT varchar(500); -- 定义预处理sql语句


    set startDate = DATE_FORMAT(start_date, '%Y-%m-%d');
    set endDate = DATE_FORMAT(end_date, '%Y-%m-%d');

    WHILE startDate <= endDate
        DO
		    set SQL_FOR_SELECT = CONCAT("CALL ",procedure_name,"('",startDate,"')");   -- 拼接查询sql语句
			set @sql = SQL_FOR_SELECT;
            PREPARE stmt FROM @sql;       -- 预处理动态sql语句
            EXECUTE stmt ;                -- 执行sql语句
            deallocate prepare stmt;      -- 释放prepare


            set startDate = DATE_ADD(startDate, INTERVAL 1 DAY);
        END WHILE;
END$$
DELIMITER ;


/*
解决更新的问题
 */
DROP PROCEDURE IF EXISTS `tybss_report`.`p_r_merchant_match_bet_info`;

DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_r_merchant_match_bet_info`(IN `execute_date` VARCHAR(100))
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
       CASE
           WHEN d.match_over =1 THEN 4
           WHEN d.match_period_id =999 THEN 4
           ELSE d.match_status
           END                                                                              match_status,
       IFNULL(SUM(case when d.series_type = 1 then d.bet_amount else 0 end) / 100, 0)                                                       bet_amount,
       IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.bet_amount END) / 100, 0) valid_bet_amount,
       IFNULL(SUM(case when d.series_type = 1 then s.settle_amount else 0 end) / 100, 0)                                                    settle_amount,
       IFNULL(SUM(case when d.series_type = 1 then s.profit_amount else 0 end) / 100, 0)                                                    profit,
       MIN(d.tournament_id)                                                                     tournament_id,
       COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.uid END))            user_amount,
       COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.order_no END))       order_amount,
       (IFNULL(CONVERT(sum(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN s.profit_amount END)/
                       sum(CASE WHEN d.series_type = 1 and d.order_status = 1 THEN d.bet_amount END) , DECIMAL(32, 4)),
               0))                                                                       profitRate,
       COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.play_id END))        play_amount,
       UNIX_TIMESTAMP(current_timestamp(3)) AS                                                  elasticsearch_id,
       MIN(d.tournament_name)                                                                   tournamentName,
       d.merchant_code,
       d.merchant_name,
       MIN(d.tournament_level)                                                                  tournamentLevel,
       IFNULL( COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.order_no END)), 0)             un_settle_order,
       IFNULL( SUM(CASE WHEN  d.series_type = 1 and d.order_status in (0) THEN d.bet_amount/100 END), 0)                un_settle_amount,
       d.agent_level        agent_level,


       IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.order_amount_total END) / 100,
              0)                                                                 parlay_vaild_bet_amount,
       IFNULL(SUM(s.profit_amount) / 100, 0)                                     parlay_profit,
       IFNULL(CONVERT(SUM(s.profit_amount) /
                      SUM(CASE WHEN d.order_status = 1 THEN d.order_amount_total END), DECIMAL(32, 4)), 0)
           parlay_profit_rate,
       COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.order_no END))  parlay_valid_tickets
FROM view_r_order_match_detail d
         LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
  AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
  AND d.order_status in (0, 1)
GROUP BY d.match_id, d.merchant_code;
SET exce_msg = CONCAT(execute_date, " p_r_merchant_match_bet_info success!");
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);







REPLACE INTO tybss_report.r_merchant_match_bet_info
SELECT CONCAT(d.match_id, mn.merchant_code)                                           id,
       d.match_id,
       MIN(d.match_info)                                                              match_info,
       MIN(d.sport_id)                                                                sport_id,
       MIN(d.sport_name)                                                              sport_name,
       MIN(d.begin_time)                                                              begin_time,
       CASE
           WHEN d.match_over =1 THEN 4
           WHEN d.match_period_id =999 THEN 4
           ELSE d.match_status
           END                                                                              match_status,
       IFNULL(SUM(d.bet_amount) / 100, 0)                                             bet_amount,
       IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.bet_amount END) / 100, 0) valid_bet_amount,
       IFNULL(SUM(s.settle_amount) / 100, 0)                                          settle_amount,
       IFNULL(SUM(s.profit_amount) / 100, 0)                                          profit,
       MIN(d.tournament_id)                                                           tournament_id,
       COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.uid END))            user_amount,
       COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.order_no END))       order_amount,
       IFNULL(CONVERT(sum(CASE WHEN d.order_status in (0, 1) THEN s.profit_amount END)/
                      sum(CASE WHEN d.order_status = 1 THEN d.bet_amount END) , DECIMAL(32, 4)),
              0)                                                                              profitRate,
       COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.play_id END))        play_amount,
       UNIX_TIMESTAMP(current_timestamp(3)) AS                                        elasticsearch_id,
       MIN(d.tournament_name)                                                         tournamentName,
       mn.merchant_code,
       mn.merchant_name,
       MIN(d.tournament_level)                                                        tournamentLevel,
       IFNULL( COUNT(DISTINCT (CASE WHEN d.order_status in (0) THEN d.order_no END)), 0)             un_settle_order,
       IFNULL( SUM(CASE WHEN  d.order_status in (0) THEN d.bet_amount/100 END), 0)                un_settle_amount,
       mn.agent_level        agent_level,
       0,0,0,0
FROM view_r_order_match_detail d
         LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
         left join tybss_new.t_merchant mn on d.parent_id = mn.id
WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
  AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
  AND d.series_type = 1
  and mn.merchant_code is not null
GROUP BY d.match_id, mn.merchant_code;

UPDATE tybss_report.r_merchant_match_bet_info a
    INNER JOIN (
    SELECT  d.match_id  matchId, d.merchant_code merchantCode, IFNULL(SUM(CASE WHEN d.order_status in (0, 1) THEN d.order_amount_total END) / 100,
    0)                                                                 validBetAmount,
    IFNULL(SUM(s.profit_amount) / 100, 0)                                     profit,
    IFNULL(CONVERT(SUM(s.profit_amount) /
    SUM(CASE WHEN d.order_status = 1 THEN d.order_amount_total END), DECIMAL(32, 4)), 0)
    profitRate,
    COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.order_no END))  validTickets
    FROM view_r_order_match_detail d
    LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
    left join tybss_new.t_merchant mn on d.parent_id = mn.id
    WHERE d.begin_time >= (UNIX_TIMESTAMP(execute_date) * 1000 + (12 * 60 * 60 * 1000))
    AND d.begin_time <= (UNIX_TIMESTAMP(DATE_ADD(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1))
    AND d.series_type > 1
    and mn.merchant_code is not null
    AND d.order_status in (0, 1)
    GROUP BY d.match_id, d.merchant_code
    ) b ON a.match_id = b.matchId and  a.merchant_code = b.merchantCode
    SET a.parlay_vaild_bet_amount = b.validBetAmount ,a.parlay_profit=b.profit,a.parlay_profit_rate=b.profitRate,a.parlay_valid_tickets=b.validTickets;



/*sql结束*/
/*执行成功，添加日志*/
SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_match_bet_info2 success!");

CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END$$
DELIMITER ;


/*
优化表死锁问题
*/
DROP PROCEDURE IF EXISTS `tybss_report`.`p_r_match_bet_info`;


DELIMITER $$
CREATE DEFINER=`yunweiroot`@`%` PROCEDURE `p_r_match_bet_info`(IN `execute_date` VARCHAR(100))
BEGIN

    DECLARE task_type INT(2) DEFAULT 2;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_match_bet_info成功';
    DECLARE msg TEXT;
    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码:', result_code, execute_date, 'p_r_match_bet_info错误信息：', msg);
        SET result = 2;
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END;

    set startTimeUTCL = unix_timestamp(execute_date) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1;



REPLACE INTO tybss_report.r_match_bet_info
select a.id                                    id,
       a.id                                    matchId,
       concat(a.homeName, ' v ', a.awayName)   matchInfo,
       a.sport_id,
       b.sportName,
       a.begin_time,
       a.match_status,
       b.betAmount,
       b.validBetAmount,
       b.settleAmount,
       b.profit,
       a.tournament_id,
       b.betUsers,
       b.validTickets,
       b.profitRate,
       b.play_amount,
       UNIX_TIMESTAMP(current_timestamp(3)) AS elasticsearch_id,
       a.tournamentName,
       a.tournament_level,
       b.un_settle_order,
       b.un_settle_amount,

       b.preBetAmount,
       b.preValidTickets,
       b.preBetUsers,
       b.preProfit,
       b.pre_un_settle_order,
       b.pre_un_settle_amount,

       b.liveBetAmount,
       b.liveValidTickets,
       b.liveBetUsers,
       b.liveProfit,
       b.live_un_settle_order,
       b.live_un_settle_amount,


       b.livePABetAmount,
       b.livePAValidTickets,
       b.livePABetUsers,
       b.livePAProfit,
       b.livePA_un_settle_order,
       b.livePA_un_settle_amount,


       b.prePABetAmount,
       b.prePAValidTickets,
       b.prePABetUsers,
       b.prePAProfit,
       b.prePA_un_settle_order,
       b.prePA_un_settle_amount,

       b.preMTSBetUsers,
       b.liveMTSBetUsers,
       b.parlay_vaild_bet_amount,
       b.parlay_valid_tickets,
       b.parlay_profit,
       b.parlay_profit_rate
from (select id,
             sport_id,
             begin_time,
             match_status,
             (select zs from tybss_new.s_language where name_code = home_name_code)       homeName,
             (select zs from tybss_new.s_language where name_code = away_name_code)       awayName,
             tournament_id,
             tournament_level,
             (select zs from tybss_new.s_language where name_code = tournament_name_code) tourNamentName
      from tybss_new.s_match_info mi
      where mi.begin_time >= startTimeUTCL
        AND mi.begin_time <= endTimeUTCL
        and (mi.pre_match_time > 0 or mi.live_odd_time > 0)) a
         left join
     (SELECT d.match_id                                                                matchId,
             d.match_id,
             MIN(d.match_info)                                                         matchInfo,
             MIN(d.sport_id)                                                           sportId,
             MIN(d.sport_name)                                                         sportName,
             MIN(d.begin_time)                                                         beginTime,
             MIN(d.match_status)                                                       matchStatus,
             IFNULL(SUM(case when d.series_type = 1 then d.order_amount_total else 0 end) / 100, 0)                                betAmount,
             IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.order_amount_total else 0 END) / 100,
                    0)                                                                 validBetAmount,
             IFNULL(SUM(case when  d.series_type = 1 then s.settle_amount else 0 end) / 100, 0)                                     settleAmount,
             IFNULL(SUM(case when  d.series_type = 1 then s.profit_amount else 0 end) / 100, 0)                                     profit,
             MIN(d.tournament_id)                                                      tournamentId,
             COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.uid END))       betUsers,
             COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.order_no END))  validTickets,
             CONVERT(SUM(case when d.series_type = 1 then s.profit_amount else 0 end) /
                     SUM(CASE WHEN d.series_type = 1 and d.order_status = 1 THEN d.order_amount_total END), DECIMAL(32, 4))
                 profitRate,
             COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.play_id END))   play_amount,
             MIN(d.tournament_name)                                                    tournamentName,
             MIN(d.tournament_level)                                                   tournamentLevel,
             IFNULL(COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.order_no END)),
                    0)                                                                 un_settle_order,
             IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.order_amount_total / 100 END),
                    0)                                                                 un_settle_amount,

             sum(case when d.series_type = 1 and d.match_type = 1 and d.order_status in (0, 1) then d.order_amount_total / 100 end)
                 preBetAmount,
             COUNT(case
                       when d.series_type = 1 and d.match_type = 1 and d.order_status in (0, 1)
                           then d.order_no end)                                        preValidTickets,
             COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.match_type = 1 and d.order_status in (0, 1) THEN d.uid END))
                 preBetUsers,
             IFNULL(SUM(case when d.series_type = 1 and d.match_type = 1 then s.profit_amount end) / 100, 0) preProfit,

             IFNULL(COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.match_type = 1 and d.order_status in (0) THEN d.order_no END)),
                    0)                                                                 pre_un_settle_order,
             IFNULL(SUM(CASE WHEN d.series_type = 1 and d.match_type = 1 and d.order_status in (0) THEN d.order_amount_total / 100 END),
                    0)                                                                 pre_un_settle_amount,


             sum(case when d.series_type = 1 and d.match_type = 2 and d.order_status in (0, 1) then d.order_amount_total / 100 end)
                 liveBetAmount,
             COUNT(case
                       when d.series_type = 1 and d.match_type = 2 and d.order_status in (0, 1)
                           then d.order_no end)                                        liveValidTickets,
             COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.match_type = 2 and d.order_status in (0, 1) THEN d.uid END))
                 liveBetUsers,
             IFNULL(SUM(case when d.series_type = 1 and d.match_type = 2 then s.profit_amount end) / 100, 0) liveProfit,

             IFNULL(COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.match_type = 2 and d.order_status in (0) THEN d.order_no END)),
                    0)                                                                 live_un_settle_order,
             IFNULL(SUM(CASE WHEN d.series_type = 1 and d.match_type = 2 and d.order_status in (0) THEN d.order_amount_total / 100 END),
                    0)                                                                 live_un_settle_amount,

             sum(case
                     when d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and d.order_status in (0, 1)
                         then d.order_amount_total / 100 end)
                 livePABetAmount,
             COUNT(case
                       when d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and d.order_status in (0, 1)
                           then d.order_no end)                                        livePAValidTickets,
             COUNT(DISTINCT (CASE
                                 WHEN d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and d.order_status in (0, 1)
                                     THEN d.uid END))
                 livePABetUsers,
             IFNULL(SUM(case when d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 then s.profit_amount end) / 100,
                    0)                                                                 livePAProfit,

             IFNULL(COUNT(DISTINCT (CASE
                                        WHEN d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and d.order_status in (0)
                                            THEN d.order_no END)), 0)                  livePA_un_settle_order,
             IFNULL(SUM(CASE
                            WHEN d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and d.order_status in (0)
                                THEN d.order_amount_total / 100 END), 0)               livePA_un_settle_amount,

             sum(case
                     when d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and d.order_status in (0, 1)
                         then d.order_amount_total / 100 end)
                 prePABetAmount,
             COUNT(case
                       when d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and d.order_status in (0, 1)
                           then d.order_no end)                                        prePAValidTickets,
             COUNT(DISTINCT (CASE
                                 WHEN d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and d.order_status in (0, 1)
                                     THEN d.uid END))
                 prePABetUsers,
             IFNULL(SUM(case when d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 then s.profit_amount end) / 100,
                    0)                                                                 prePAProfit,

             IFNULL(COUNT(DISTINCT (CASE
                                        WHEN d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and d.order_status in (0)
                                            THEN d.order_no END)), 0)                  prePA_un_settle_order,
             IFNULL(SUM(CASE
                            WHEN d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and d.order_status in (0)
                                THEN d.order_amount_total / 100 END), 0)               prePA_un_settle_amount,

             COUNT(DISTINCT (CASE
                                 WHEN d.series_type = 1 and d.match_type = 1 and d.manager_code = 2 and d.order_status in (0, 1)
                                     THEN d.uid END))
                 preMTSBetUsers,
             COUNT(DISTINCT (CASE
                                 WHEN d.series_type = 1 and d.match_type = 2 and d.manager_code = 2 and d.order_status in (0, 1)
                                     THEN d.uid END))
                 liveMTSBetUsers,
             IFNULL( SUM( CASE WHEN d.series_type <> 1 and d.order_status IN ( 0, 1 ) THEN d.order_amount_total END ) / 100, 0 ) parlay_vaild_bet_amount,
             IFNULL( SUM( case when d.series_type <> 1 then s.profit_amount else 0 end ) / 100, 0 ) parlay_profit,
             IFNULL(CONVERT (SUM(  CASE WHEN d.series_type <> 1 then s.profit_amount else 0 end ) / SUM( CASE WHEN d.order_status <> 1 and d.order_status = 1 THEN d.order_amount_total END ),DECIMAL ( 32, 4 )),0) parlay_profit_rate,
             COUNT(DISTINCT ( CASE WHEN d.series_type <> 1 and d.order_status IN ( 0, 1 ) THEN d.order_no END )) parlay_valid_tickets
      FROM view_r_order_match_detail d
               LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
      WHERE d.begin_time >= startTimeUTCL
        AND d.begin_time <= endTimeUTCL
        and d.bet_time > unix_timestamp(date_add(execute_date, INTERVAL -60 DAY)) * 1000
      GROUP BY d.match_id) b on a.id = b.match_id;




SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, 'p_r_match_bet_info success!');

CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END$$
DELIMITER ;

