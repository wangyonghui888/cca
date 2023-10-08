SELECT
       u.uid,
       o.match_date,
       u.username,
       u.merchant_id,
       CAST (sum(o.bet_amount)/100 AS bigint) AS bet_amount,
            sum(o.bet_order_nums),
            cast(sum(profit_amount)/100 AS bigint) AS losses,
            cast(((sum(o.profit_amount))/ sum(o.bet_amount)) AS decimal(10,2)) AS losses_point,
            sum(o.profit_bet_nums) AS profit_bet_nums,
            cast((sum(o.profit_bet_nums)/sum(o.bet_order_nums)) AS decimal(10,3)) AS profit_point,
            sum(refuse_order_nums) AS refuse_order_nums,
            sum(refuse_order_amount) AS refuse_order_amount,
            sum(cancel_order_nums) AS cancel_order_nums,
            sum(cancel_order_amount) AS cancel_order_amount,
            sum(o.odds_high) AS odds_high,
            sum(o.odds_low) AS odds_low,
            sum(o.football_asian_handicap_master_makert) AS football_asian_handicap_master_makert,
            sum(o.football_asian_handicap_slave_makert) AS football_asian_handicap_slave_makert,
            sum(o.football_goal_line_master_makert) AS football_goal_line_master_makert,
            sum(o.football_goal_line_slave_makert) AS football_goal_line_slave_makert,
            sum(o.race_football) AS race_football,
            sum(o.race_basketball) AS race_basketball,
            sum(o.race_other) AS race_other,
            sum(o.race_multiple_bet) AS race_multiple_bet,
            sum(bsketball_goal_line) AS bsketball_goal_line,
            sum(bsketball_asian_handicap) AS bsketball_asian_handicap
FROM
  (SELECT from_unixtime(FLOOR(CAST(md.begin_time AS bigint)/1000),'yyyy-MM-dd') AS match_date,
          o.uid,
          sum(CASE WHEN o.order_status='1' THEN o.bet_amount
                  ELSE 0
              END) AS bet_amount,
          count(DISTINCT o.order_no) AS bet_order_nums,
          sum(s.profit_amount) AS profit_amount,
          count(distinct(CASE
                             WHEN s.out_come=4 THEN s.order_no
                             ELSE NULL
                         END)) AS profit_bet_nums,
          count(DISTINCT(CASE
                    WHEN o.order_status = 4 THEN s.order_no
                    ELSE NULL
                END)) AS refuse_order_nums,
          sum(CASE
                  WHEN o.order_status = 4 THEN o.bet_amount
                  ELSE 0
              END) AS refuse_order_amount,
          count(DISTINCT(CASE
                    WHEN s.out_come IN (7,8) THEN s.order_no
                    ELSE NULL
                END)) AS cancel_order_nums,
          sum(CASE
                  WHEN s.out_come IN (7,8) THEN o.bet_amount
                  ELSE 0
              END) AS cancel_order_amount,
          count(DISTINCT(CASE
                    WHEN o.odds_value>=200000 THEN s.order_no
                    ELSE NULL
                END)) AS odds_high,
          count(DISTINCT(CASE
                    WHEN o.odds_value<200000 THEN s.order_no
                    ELSE NULL
                END)) AS odds_low,
          count(DISTINCT(CASE
                    WHEN o.sport_id=1 THEN s.order_no
                    ELSE NULL
                END)) AS race_football,
          count(DISTINCT(CASE
                    WHEN o.sport_id=2 THEN s.order_no
                    ELSE NULL
                END)) AS race_basketball,
          count(DISTINCT(CASE
                    WHEN o.sport_id<>1
                         AND o.sport_id<>2 THEN s.order_no
                    ELSE NULL
                END)) AS race_other,
          count(DISTINCT(CASE
                    WHEN o.series_type<>1 THEN s.order_no
                    ELSE NULL
                END)) AS race_multiple_bet,
          count(DISTINCT(CASE
                    WHEN m.market_main=0
                         AND o.play_id IN(4,19) THEN s.order_no
                    ELSE NULL
                END)) AS football_asian_handicap_master_makert,
          count(DISTINCT(CASE
                    WHEN m.market_main=1
                         AND o.play_id IN(4,19) THEN s.order_no
                    ELSE NULl
                END)) AS football_asian_handicap_slave_makert,
          count(DISTINCT(CASE
                    WHEN m.market_main=0
                         AND o.play_id IN(2,18) THEN s.order_no
                    ELSE NULL
                END)) AS football_goal_line_master_makert,
          count(DISTINCT(CASE
                    WHEN m.market_main=1
                         AND o.play_id IN(2,18) THEN s.order_no
                    ELSE NULL
                END)) AS football_goal_line_slave_makert,
          count(DISTINCT(CASE
                    WHEN o.play_id IN (4,19,39,46,52,58,64,143)
                         AND o.sport_id=2 THEN s.order_no
                    ELSE NULL
                END)) AS bsketball_asian_handicap,
          count(DISTINCT(CASE
                    WHEN o.play_id IN (2,38,18,26,45,51,57,63,87,89,145,146)
                         AND o.sport_id=2 THEN s.order_no
                    ELSE NULL
                END)) AS bsketball_goal_line,
          o.order_no,
          min(s.settle_amount) settle_amount
   FROM dwm_report.t_order_full_detail o
   Left join dwm_report.order_match_detail md on md.match_id=o.id
   LEFT JOIN base_temp.bss_market m ON o.market_id=m.id
   LEFT JOIN dwm_report.settle_full_detail s ON o.order_no = s.order_no
   WHERE s.out_come <> 0
     AND o.series_type=1
   GROUP BY from_unixtime(FLOOR(CAST(md.begin_time AS bigint)/1000),'yyyy-MM-dd'),
            o.uid,
            o.order_no) o
LEFT JOIN dwm_report.user_full_info u ON u.uid = o.uid
GROUP BY u.uid,
         o.match_date,
         u.username,
         u.merchant_id;