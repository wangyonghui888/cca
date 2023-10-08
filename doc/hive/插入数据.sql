--------------用户------------------
insert into table dws_report.rcs_user_statistic_day
 SELECT
       concat(u.uid ,'_', o.match_date) as sid,
       u.uid,
       unix_timestamp(o.match_date,'yyyy-mm-dd') as match_date,
       u.username,
       u.merchant_id,
       u.merchant_code,
       cast (o.bet_amount/100 AS bigint) as bet_amount,
       o.bet_order_nums,
       cast(o.profit_amount/100 as bigint) AS losses,
       if(cast(((o.profit_amount)/ o.bet_amount)* 100 As decimal(10,2)) is null,0,cast(((o.profit_amount)/ o.bet_amount)* 100 As decimal(10,2))) AS losses_point,
       if(o.profit_bet_nums is null,0,o.profit_bet_nums) AS profit_bet_nums,
       if(cast((o.profit_bet_nums/o.bet_order_nums)*100 As decimal(10,4)) is null,0,cast((o.profit_bet_nums/o.bet_order_nums)*100 As decimal(10,3))) AS profit_point,
       o.refuse_order_nums,
       o.refuse_order_amount,
       o.cancel_order_amount,
       o.cancel_order_nums,
       o.odds_high,
       o.odds_low,
       o.football_asian_handicap_master_makert,
       o.football_asian_handicap_slave_makert,
       o.football_goal_line_master_makert,
       o.football_goal_line_slave_makert,
       o.race_football,
       o.race_basketball,
       o.race_other,
       o.race_multiple_bet,
       o.bsketball_goal_line,
       o.bsketball_asian_handicap
FROM
  (
    SELECT
          from_unixtime(FLOOR(CAST(md.begin_time AS bigint)/1000) +(12 * 60 * 60 * 1000),'yyyy-MM-dd') as match_date,
          o.uid,
          sum(case when o.order_status='1' and o.series_type=1 then o.bet_amount else 0 end) AS bet_amount,
          count(DISTINCT case when o.order_status='1' and o.series_type=1 then o.order_no else null end) AS bet_order_nums,
          sum(if(s.profit_amount is null,0,s.profit_amount)) AS profit_amount,
          count(distinct(case when s.out_come=4 then s.order_no else null end )) AS profit_bet_nums,
          count(CASE
                    WHEN o.order_status = 4 THEN 1
                    ELSE 0
                END) AS refuse_order_nums,
          sum(CASE
                    WHEN o.order_status = 4 THEN o.bet_amount
                    ELSE 0
                END) AS refuse_order_amount,
          count(CASE
                    WHEN s.out_come in (7,8) THEN 1
                    ELSE 0
                END) AS cancel_order_nums,
          sum(CASE
                    WHEN s.out_come in (7,8) THEN o.bet_amount
                    ELSE 0
                END) AS cancel_order_amount,
          count(CASE
                    WHEN o.odds_value>=2 THEN 1
                    ELSE 0
                END) AS odds_high,
          count(CASE
                    WHEN o.odds_value<2 THEN 1
                    ELSE 0
                END) AS odds_low,
          count(CASE
                    WHEN o.sport_id=1 THEN 1
                    ELSE 0
                END) AS race_football,
          count(CASE
                    WHEN o.sport_id=2 THEN 1
                    ELSE 0
                END) AS race_basketball,
          count(CASE
                    WHEN o.sport_id<>1
                         AND o.sport_id<>2 THEN 1
                    ELSE 0
                END) AS race_other,
          count(CASE
                    WHEN o.series_type<>1 THEN 1
                    ELSE 0
                END) AS race_multiple_bet,
          count(CASE
                    WHEN m.market_main=0
                         AND o.play_id IN(4,19) THEN 1
                    ELSE 0
                END) AS football_asian_handicap_master_makert,
          count(CASE
                    WHEN m.market_main=1
                         AND o.play_id IN(4,19) THEN 1
                    ELSE 0
                END) AS football_asian_handicap_slave_makert,
          count(CASE
                    WHEN m.market_main=0
                         AND o.play_id IN(2,18) THEN 1
                    ELSE 0
                END) AS football_goal_line_master_makert,
          count(CASE
                    WHEN m.market_main=1
                         AND o.play_id IN(2,18) THEN 1
                    ELSE 0
                END) AS football_goal_line_slave_makert,
          count(CASE
                    WHEN o.play_id IN (4,19,39,46,52,58,64,143)
                         AND o.sport_id=2 THEN 1
                    ELSE 0
                END) AS bsketball_asian_handicap,
          count(CASE
                    WHEN o.play_id IN (2,38,18,26,45,51,57,63,87,89,145,146)
                         AND o.sport_id=2 THEN 1
                    ELSE 0
                END) AS bsketball_goal_line,
                o.order_no,
                min(s.settle_amount) settle_amount
   FROM dwm_report.t_order_full_detail o
   left JOIN base_temp.bss_market m ON o.market_id=m.id
   left JOIN dwm_report.t_settle_full_detail s ON o.order_no = s.order_no
   left JOIN dwm_report.order_match_detail md ON md.match_id=o.match_id
   where md.begin_time BETWEEN unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),1))*1000 +(12 * 60 * 60 * 1000) AND unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),0))*1000 +(12 * 60 * 60 * 1000 - 1)
   GROUP BY from_unixtime( FLOOR(CAST(md.begin_time AS bigint)/1000)+(12 * 60 * 60 * 1000),'yyyy-MM-dd'),o.uid,o.order_no) o
LEFT JOIN dwm_report.user_full_info u ON u.uid = o.uid;
--------------------概况-------------------------------------------------------------------------------------------
insert into table dws_report.rcs_analyze_overview_day
SELECT
       concat(c.match_date,'_' ,c.merchant_code,'_',c.sport_id,'_',c.tournament_id,'_',c.play_id,'_',c.match_type) as sid,
       c.merchant_id,
       c.merchant_code,
       unix_timestamp(c.match_date,'yyyy-mm-dd')  as match_date,
       s.order_day,
       s.order_year,
       s.order_phase,
       s.order_week,
       c.sport_id,
       c.tournament_id,
       c.play_id,
       c.match_type,
       c.bet_order_amount,
       c.bet_order_nums,
       c.match_bet_times,
       c.profit_amount,
       if(c.profit_point is null,0,c.profit_point) as profit_point,
       c.per_bet_order_nums,
       c.per_capita_order_amount,
       c.per_capita_order_nums,
       c.amount_ltone_thousand_count,
       c.amount_gtone_thousand_count,
       c.amount_gttwo_thousand_count,
       c.amount_gtfive_thousand_count,
       c.amount_gtten_thousand_count
FROM base_temp.rcs_order_statistic_date s
inner JOIN
  (
  SELECT  e.merchant_id,
       e.merchant_code,
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
  ( SELECT from_unixtime(FLOOR(CAST(m.begin_time AS bigint)/1000)+(12 * 60 * 60 * 1000),'yyyy-MM-dd') AS match_date,
           d.sport_id,
           d.tournament_id,
           d.match_id,
           d.play_id,
           d.match_type,
           u.merchant_id,
           u.merchant_code,
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
           sum(CASE
                     WHEN d.bet_amount<1000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_ltone_thousand_count,
           sum(CASE
                     WHEN d.bet_amount>1000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_gtone_thousand_count,
           sum(CASE
                     WHEN d.bet_amount>2000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_gttwo_thousand_count,
           sum(CASE
                     WHEN d.bet_amount>5000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_gtfive_thousand_count,
           sum(CASE
                     WHEN d.bet_amount>10000
                          AND d.order_status='1' THEN 1
                     ELSE 0
                 END) AS amount_gtten_thousand_count
   FROM dwm_report.t_order_full_detail d
   left JOIN dwm_report.order_match_detail m ON m.match_id=d.match_id
   left JOIN dwm_report.user_full_info u ON u.uid=d.uid
   left JOIN dwm_report.t_settle_full_detail s ON s.order_no=d.order_no
   where m.begin_time BETWEEN unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),1))*1000 +(12 * 60 * 60 * 1000) AND unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),0))*1000 +(12 * 60 * 60 * 1000 - 1)
   GROUP BY from_unixtime(FLOOR(CAST(m.begin_time AS bigint)/1000)+(12 * 60 * 60 * 1000),'yyyy-MM-dd'),
            d.sport_id,
            d.tournament_id,
            d.match_id,
            d.play_id,
            d.match_type,
            u.merchant_id,
            u.merchant_code) e
GROUP BY e.match_date,
         e.merchant_id,
         e.merchant_code,
         e.sport_id,
         e.tournament_id,
         e.play_id,
         e.match_type
  ) c ON s.order_day=c.match_date WHERE c.match_date IS NOT NULL;



