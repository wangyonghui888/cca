truncate table tybss_report.r_merchant_match_bet_info_group;
REPLACE INTO tybss_report.r_merchant_match_bet_info_group
    SELECT CONCAT(d.match_id, d.merchant_code,d.order_currency_code)                              id,
           d.match_id,
           d.order_currency_code,
		   d.s_match_type                                                                    match_type,
           MIN(d.match_info)                                                                 match_info,
           MIN(d.sport_id)                                                                   sport_id,
           MIN(d.sport_name)                                                                 sport_name,
           MIN(d.begin_time)                                                                  begin_time,
           (CASE
                WHEN d.match_over = 1 THEN 4
                WHEN d.match_period_id = 999 THEN 4
                ELSE d.match_status
               END)                                                                          match_status,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then d.bet_amount else 0 end) / 100, 0)    bet_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.bet_amount END) / 100,
                  0)                                                                         valid_bet_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then s.settle_amount end) / 100, 0)        settle_amount,
           IFNULL(SUM(CASE WHEN d.series_type = 1 then s.profit_amount end) / 100, 0)        profit,
           MIN(d.tournament_id)                                                              tournament_id,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.uid END))
                                                                                             user_amount,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.order_no END))
                                                                                             order_amount,
           IFNULL(CONVERT(sum(CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN s.profit_amount END) /
                          sum(
                                  CASE WHEN d.series_type = 1 and d.order_status = 1 THEN d.bet_amount END), DECIMAL(32, 4)),
                  0)                                                                         profitRate,
           COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0, 1) THEN d.play_id END))
                                                                                             play_amount,
           UNIX_TIMESTAMP(current_timestamp(3)) AS                                           elasticsearch_id,
           MIN(d.tournament_name)                                                            tournamentName,
           d.merchant_code,
           d.merchant_name,
           MIN(d.tournament_level)                                                           tournamentLevel,
           IFNULL(COUNT(DISTINCT (CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.order_no END)), 0)
                                                                                             un_settle_order,
           IFNULL(SUM(CASE WHEN d.series_type = 1 and d.order_status in (0) THEN d.bet_amount / 100 END), 0)
                                                                                             un_settle_amount,
           d.agent_level                                                                     agent_level,
           IFNULL(SUM(CASE WHEN d.series_type > 1 and d.order_status in (0, 1) THEN d.order_amount_total END) / 100,
                  0)                                                                         parlay_vaild_bet_amount,
           COUNT(DISTINCT (CASE
                               WHEN d.series_type > 1 and d.order_status in (0, 1)
                                   THEN d.order_no END))                                     parlay_valid_tickets,
           IFNULL(SUM(case when d.series_type > 1 then s.profit_amount else 0 end) / 100, 0) parlay_profit,
           0                                                                                 parlay_profit_rate
    FROM tybss_report.view_r_order_match_detail_group d
             LEFT JOIN tybss_report.view_r_settle_full_detail_group s ON d.order_no = s.order_no
    WHERE d.begin_time >= (UNIX_TIMESTAMP(DATE_SUB(NOW() , INTERVAL 30 DAY)) * 1000 + (12 * 60 * 60 * 1000))
      AND d.begin_time <= (UNIX_TIMESTAMP(NOW()) * 1000 + (12 * 60 * 60 * 1000 - 1))
      AND d.order_status in (0, 1)
    GROUP BY d.match_id, d.merchant_code, d.order_currency_code;