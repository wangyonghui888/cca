INSERT INTO dws_report.rcs_match_play_day
SELECT concat(p.match_id,p.play_id,p.begin_time) AS mpd_id,
       p.begin_time AS begin_time,
       p.match_id AS match_id,
       p.sport_id AS sport_id,
       p.tournament_id AS tournament_id,
       p.play_id AS play_id,
       p.match_type AS match_type,
       p.match_info AS match_info,
       floor(d.total_bet_amount) AS total_bet_amount,
       p.play_name AS play_name,
       floor(p.bet_amount) AS bet_amount,
       floor(p.settle_amount) AS settle_amount,
       p.order_num AS order_num,
       p.user_num AS user_num,
       nvl(cast((p.bet_amount/d.total_bet_amount) * 100 As decimal(10,1)),0) as bet_proportion,
       floor((p.bet_amount - p.settle_amount)) AS profit_amount,
       nvl(floor(p.bet_amount/p.order_num),0) AS average_amount,
       nvl(round(p.order_num/p.user_num,1),0) AS person_order_num,
       nvl(floor(p.bet_amount/p.user_num),0) AS person_bet_amount,
       floor(p.option_one_amount) as option_one_amount,
       p.option_one_num as option_one_num,
       floor(p.option_two_amount) as option_two_amount,
       p.option_two_num as option_two_num
FROM
  (SELECT a.begin_time,
          a.match_id,
          a.sport_id,
          a.tournament_id,
          a.play_id,
          a.match_type,
          a.match_info,
          a.play_name,
          sum(a.bet_amount) AS bet_amount,
          count(1) AS order_num,
          sum(nvl(s.settle_amount,0)) AS settle_amount,
          count(DISTINCT a.uid) user_num,
          sum(case WHEN a.odds_type in('1','Over') THEN a.bet_amount ELSE 0 END) as option_one_amount,
          count(case WHEN a.odds_type in('1','Over') THEN 1 ELSE null END) as option_one_num,
          sum(case WHEN a.odds_type in('2','Under') THEN a.bet_amount ELSE 0 END) as option_two_amount,
          count(case WHEN a.odds_type in('2','Under') THEN 1 ELSE null END) as option_two_num
   FROM dwm_report.order_match_detail a
   LEFT JOIN dwm_report.t_settle_full_detail s ON a.order_no=s.order_no
   left join base_temp.bss_order o on a.order_no=o.order_no
   WHERE o.series_type=1
     AND o.order_status in (0,1)
     AND a.bet_time BETWEEN unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),1))*1000 +(12 * 60 * 60 * 1000) AND unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),0))*1000 +(12 * 60 * 60 * 1000 - 1)
   GROUP BY a.match_id,
            a.play_id,
            a.play_name,
            a.match_info,
            a.sport_id,
            a.begin_time,
            a.tournament_id,
            a.match_type) p
LEFT JOIN
  (SELECT sum(a.bet_amount) AS total_bet_amount,
          a.match_id
   FROM dwm_report.order_match_detail a
   left join base_temp.bss_order o on a.order_no=o.order_no
   WHERE o.series_type=1
     AND o.order_status in (0,1)
   GROUP BY a.match_id) d ON p.match_id=d.match_id