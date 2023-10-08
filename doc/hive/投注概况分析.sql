SELECT s.order_day,
       s.order_phase,
       s.order_week,
       s.order_year,
       c.merchant_id,
       c.match_date,
       c.sport_id,
       c.tournament_id,
       c.play_id,
       c.match_type,
       c.bet_order_amount,
       c.bet_order_nums,
       c.match_bet_times,
       c.profit_amount,
       c.settled_bet_amount,
       c.profit_point,
       c.per_bet_order_nums,
       c.per_capita_order_amount,
       c.per_capita_order_nums,
       c.amount_ltone_thousand_count,
       c.amount_gtone_thousand_count,
       c.amount_gttwo_thousand_count,
       c.amount_gtfive_thousand_count,
       c.amount_gtten_thousand_count
FROM base_temp.rcs_order_statistic_date s
LEFT JOIN
  (
  SELECT e.merchant_id,
       e.match_date,
       e.sport_id,
       e.tournament_id,
       e.play_id,
       e.match_type,
       Cast(sum(e.bet_order_amount/100) AS BIGINT) AS bet_order_amount,
       sum(e.bet_order_nums) AS bet_order_nums,
       sum(e.match_bet_times) AS match_bet_times,
       Cast(sum(e.profit_amount)/100 AS BIGINT) AS profit_amount,
       Cast(sum(e.settled_bet_amount/100) AS BIGINT) AS settled_bet_amount,
       sum(e.profit_amount)/sum(e.settled_bet_amount) AS profit_point,
       cast(sum(e.bet_order_amount/100)/sum(e.bet_order_nums) AS DECIMAL(10,1)) AS per_bet_order_nums,
       cast(sum(e.bet_order_amount/100)/sum(e.match_bet_times) AS DECIMAL(10,1)) AS per_capita_order_amount,
       cast(sum(e.bet_order_nums)/sum(e.match_bet_times) AS BIGINT) AS per_capita_order_nums,
       sum(e.amount_ltone_thousand_count) AS amount_ltone_thousand_count,
       sum(e.amount_gtone_thousand_count) AS amount_gtone_thousand_count,
       sum(e.amount_gttwo_thousand_count) AS amount_gttwo_thousand_count,
       sum(e.amount_gtfive_thousand_count) AS amount_gtfive_thousand_count,
       sum(e.amount_gtten_thousand_count) AS amount_gtten_thousand_count
FROM
  ( SELECT from_unixtime(FLOOR(CAST(m.begin_time AS bigint)/1000),'yyyy-MM-dd') AS match_date,
           d.sport_id,
           d.tournament_id,
           d.match_id,
           d.play_id,
           d.match_type,
           u.merchant_id,
           if(sum(s.profit_amount) IS NULL,0,sum(s.profit_amount)) AS profit_amount,
           if(sum(s.bet_amount) IS NULL,0,sum(s.bet_amount)) AS settled_bet_amount,
           count(CASE
                     WHEN d.order_status='1' THEN 1
                     ELSE 0
                 END) AS bet_order_nums,
           sum(CASE
                   WHEN d.order_status='1' THEN d.bet_amount
                   ELSE 0
               END) AS bet_order_amount,
           count(DISTINCT d.uid) AS match_bet_times,
           count(CASE
                     WHEN d.bet_amount<1000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_ltone_thousand_count,
           count(CASE
                     WHEN d.bet_amount>1000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_gtone_thousand_count,
           count(CASE
                     WHEN d.bet_amount>2000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_gttwo_thousand_count,
           count(CASE
                     WHEN d.bet_amount>5000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_gtfive_thousand_count,
           count(CASE
                     WHEN d.bet_amount>10000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_gtten_thousand_count
   FROM dwm_report.t_order_full_detail d
   LEFT JOIN dwm_report.order_match_detail m ON m.match_id=d.match_id
   LEFT JOIN dwm_report.user_full_info u ON u.uid=d.uid
   LEFT JOIN dwm_report.settle_full_detail s ON s.order_no=d.order_no
   GROUP BY from_unixtime(FLOOR(CAST(m.begin_time AS bigint)/1000),'yyyy-MM-dd'),
            d.sport_id,
            d.tournament_id,
            d.match_id,
            d.play_id,
            d.match_type,
            u.merchant_id) e
GROUP BY e.match_date,
         e.merchant_id,
         e.sport_id,
         e.tournament_id,
         e.play_id,
         e.match_type

  ) c ON s.order_day=c.match_date;