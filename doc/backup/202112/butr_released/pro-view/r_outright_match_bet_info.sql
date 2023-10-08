DROP PROCEDURE IF EXISTS p_r_outright_match_bet_info;
DELIMITER //
CREATE PROCEDURE p_r_outright_match_bet_info(in execute_date varchar(100))
BEGIN
    /*声明日志信息*/
    DECLARE task_type INT(2) DEFAULT 201;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_outright_match_bet_info';
    DECLARE msg TEXT;
    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);
/*异常处理*/
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码:', result_code, execute_date, 'p_r_outright_match_bet_info错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set startTimeUTCL = unix_timestamp(execute_date) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1;
    REPLACE INTO tybss_report.r_match_bet_info
    select a.id                                    id,
           a.id                                    matchId,
           ''   matchInfo,
           a.sport_id,
           b.sportName,
           a.begin_time,
           '' match_status,
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
           b.tournamentName,
           0 tournament_level,
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
                match_begion_time  begin_time,
                 tournament_id
          from tybss_merchant_common.s_outright_match_info mi
          where mi.match_begion_time >= startTimeUTCL
            AND mi.match_begion_time <= endTimeUTCL
          ) a
             left join
         (SELECT d.match_id                                                                             matchId,
                 d.match_id,
                 MIN(d.match_info)                                                                      matchInfo,
                 MIN(d.sport_id)                                                                        sportId,
                 MIN(d.sport_name)                                                                      sportName,
                 MIN(d.begin_time)                                                                      beginTime,

                 IFNULL(SUM(case when d.series_type = 1 then d.order_amount_total else 0 end) / 100, 0) betAmount,
                 IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.order_amount_total END) /
                        100,
                        0)                                                                              validBetAmount,
                 IFNULL(SUM(case when d.series_type = 1 then s.settle_amount else 0 end) / 100, 0)      settleAmount,
                 IFNULL(SUM(case when d.series_type = 1 then s.profit_amount else 0 end) / 100, 0)      profit,
                 MIN(d.tournament_id)                                                                   tournamentId,
                 COUNT(DISTINCT (CASE WHEN d.order_status in (0, 1) THEN d.uid END))                    betUsers,
                 COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.order_no END))
                                                                                                        validTickets,
                 IFNULL(CONVERT(SUM(case when d.series_type = 1 then s.profit_amount else 0 end) /
                                SUM(CASE
                                        WHEN d.series_type = 1 and d.order_status = 1
                                            THEN d.order_amount_total END), DECIMAL(32, 4)), 0)
                                                                                                        profitRate,
                 COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.play_id END))
                                                                                                        play_amount,
                 MIN(d.tournament_name)                                                                 tournamentName,

                 IFNULL(COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.order_no END)),
                        0)                                                                              un_settle_order,
                 IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.order_amount_total / 100 END),
                        0)                                                                              un_settle_amount,
                 sum(case
                         when d.series_type = 1 and d.match_type = 1 and d.order_status in (0, 1) then
                             d.order_amount_total / 100 end)                                            preBetAmount,
                 COUNT(case
                           when d.series_type = 1 and d.match_type = 1 and d.order_status in (0, 1)
                               then d.order_no end)                                                     preValidTickets,
                 COUNT(DISTINCT
                       (CASE WHEN d.series_type = 1 and d.match_type = 1 and d.order_status in (0, 1) THEN d.uid END))
                                                                                                        preBetUsers,
                 IFNULL(SUM(case when d.series_type = 1 and d.match_type = 1 then s.profit_amount end) / 100,
                        0)                                                                              preProfit,
                 IFNULL(COUNT(DISTINCT (CASE
                                            WHEN d.series_type = 1 and d.match_type = 1 and d.order_status in (0)
                                                THEN d.order_no END)),
                        0)                                                                              pre_un_settle_order,
                 IFNULL(SUM(CASE
                                WHEN d.series_type = 1 and d.match_type = 1 and d.order_status in (0)
                                    THEN d.order_amount_total / 100 END),
                        0)                                                                              pre_un_settle_amount,
                 sum(case
                         when d.series_type = 1 and d.match_type = 2 and d.order_status in (0, 1)
                             then d.order_amount_total / 100 end)
                                                                                                        liveBetAmount,
                 COUNT(case
                           when d.series_type = 1 and d.match_type = 2 and d.order_status in (0, 1)
                               then d.order_no end)                                                     liveValidTickets,
                 COUNT(DISTINCT
                       (CASE WHEN d.series_type = 1 and d.match_type = 2 and d.order_status in (0, 1) THEN d.uid END))
                                                                                                        liveBetUsers,
                 IFNULL(SUM(case when d.series_type = 1 and d.match_type = 2 then s.profit_amount end) / 100,
                        0)                                                                              liveProfit,

                 IFNULL(COUNT(DISTINCT (CASE
                                            WHEN d.series_type = 1 and d.match_type = 2 and d.order_status in (0)
                                                THEN d.order_no END)),
                        0)                                                                              live_un_settle_order,
                 IFNULL(SUM(CASE
                                WHEN d.series_type = 1 and d.match_type = 2 and d.order_status in (0)
                                    THEN d.order_amount_total / 100 END),
                        0)                                                                              live_un_settle_amount,

                 sum(case
                         when d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and d.order_status in (0, 1)
                             then d.order_amount_total / 100 end)
                                                                                                        livePABetAmount,
                 COUNT(case
                           when d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and
                                d.order_status in (0, 1)
                               then d.order_no end)                                                     livePAValidTickets,
                 COUNT(DISTINCT (CASE
                                     WHEN d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and
                                          d.order_status in (0, 1)
                                         THEN d.uid END))
                                                                                                        livePABetUsers,
                 IFNULL(SUM(case
                                when d.series_type = 1 and d.match_type = 2 and d.manager_code = 1
                                    then s.profit_amount end) / 100,
                        0)                                                                              livePAProfit,

                 IFNULL(COUNT(DISTINCT (CASE
                                            WHEN d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and
                                                 d.order_status in (0)
                                                THEN d.order_no END)),
                        0)                                                                              livePA_un_settle_order,
                 IFNULL(SUM(CASE
                                WHEN d.series_type = 1 and d.match_type = 2 and d.manager_code = 1 and
                                     d.order_status in (0)
                                    THEN d.order_amount_total / 100 END),
                        0)                                                                              livePA_un_settle_amount,

                 sum(case
                         when d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and d.order_status in (0, 1)
                             then d.order_amount_total / 100 end)
                                                                                                        prePABetAmount,
                 COUNT(case
                           when d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and
                                d.order_status in (0, 1)
                               then d.order_no end)                                                     prePAValidTickets,
                 COUNT(DISTINCT (CASE
                                     WHEN d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and
                                          d.order_status in (0, 1)
                                         THEN d.uid END))
                                                                                                        prePABetUsers,
                 IFNULL(SUM(case
                                when d.series_type = 1 and d.match_type = 1 and d.manager_code = 1
                                    then s.profit_amount end) / 100,
                        0)                                                                              prePAProfit,

                 IFNULL(COUNT(DISTINCT (CASE
                                            WHEN d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and
                                                 d.order_status in (0)
                                                THEN d.order_no END)),
                        0)                                                                              prePA_un_settle_order,
                 IFNULL(SUM(CASE
                                WHEN d.series_type = 1 and d.match_type = 1 and d.manager_code = 1 and
                                     d.order_status in (0)
                                    THEN d.order_amount_total / 100 END),
                        0)                                                                              prePA_un_settle_amount,
                 COUNT(DISTINCT (CASE
                                     WHEN d.series_type = 1 and d.match_type = 1 and d.manager_code = 2 and
                                          d.order_status in (0, 1)
                                         THEN d.uid END))
                                                                                                        preMTSBetUsers,
                 COUNT(DISTINCT (CASE
                                     WHEN d.series_type = 1 and d.match_type = 2 and d.manager_code = 2 and
                                          d.order_status in (0, 1)
                                         THEN d.uid END))
                                                                                                        liveMTSBetUsers,
                 IFNULL(SUM(CASE WHEN d.series_type > 1 and d.order_status IN (0, 1) THEN d.order_amount_total END) /
                        100,
                        0)                                                                              parlay_vaild_bet_amount,
                 COUNT(DISTINCT (CASE
                                     WHEN d.series_type > 1 and d.order_status IN (0, 1)
                                         THEN d.order_no END))                                          parlay_valid_tickets,
                 IFNULL(SUM(case when d.series_type > 1 then s.profit_amount else 0 end) / 100,
                        0)                                                                              parlay_profit,
                 0                                                                                      parlay_profit_rate
          FROM view_r_order_outright_match_detail d
                   LEFT JOIN view_r_settle_full_detail s ON d.order_no = s.order_no
          WHERE d.begin_time >= startTimeUTCL
            AND d.begin_time <= endTimeUTCL
          GROUP BY d.match_id) b on a.id = b.match_id;

    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, 'p_r_outright_match_bet_info success!');

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END//
DELIMITER ;

/*set @execute_date:=CURRENT_DATE;
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY);
call p_r_match_bet_info(@execute_date);

set @execute_date:=DATE_ADD(CURRENT_DATE,INTERVAL 1 DAY);
call p_r_match_bet_info(@execute_date);*/