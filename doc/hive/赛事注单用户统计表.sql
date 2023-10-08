
INSERT INTO dws_report.rcs_match_user_day
SELECT concat(p.match_id,p.uid,p.begin_time) AS mud_id,
       p.begin_time AS begin_time,
       p.match_id AS match_id,
       p.sport_id AS sport_id,
       p.tournament_id AS tournament_id,
       p.match_info AS match_info,
       p.uid AS uid,
       u.username AS username,
       '' AS usertags,
       floor(d.total_bet_amount) AS total_bet_amount,
       floor(p.bet_amount) AS bet_amount,
       floor(p.settle_amount) AS settle_amount,
       p.order_num AS order_num,
       floor(p.settle_amount - p.bet_amount) AS profit_amount,
       floor(p.live_bet_amount) AS live_bet_amount,
       p.live_order_num AS live_order_num,
       floor(p.live_settle_amount - p.live_bet_amount) AS live_profit_amount,
       floor(p.pre_bet_amount) AS pre_bet_amount,
       p.pre_order_num AS pre_order_num,
       floor(p.pre_settle_amount - p.pre_bet_amount) AS pre_profit_amount,
       floor(p.pre_two_bet_amount) AS pre_two_bet_amount,
       p.pre_two_order_num AS pre_two_order_num,
       floor(p.pre_two_settle_amount - p.pre_two_bet_amount) AS pre_two_profit_amount,
       floor(p.pre_one_bet_amount) AS pre_one_bet_amount,
       p.pre_one_order_num AS pre_one_order_num,
       floor(p.pre_one_settle_amount - p.pre_one_bet_amount) AS pre_one_profit_amount
FROM
  (SELECT d.match_id,
          d.uid,
          d.match_info,
          d.sport_id,
          d.begin_time,
          d.tournament_id,
          sum(d.bet_amount) AS bet_amount,
          count(1) AS order_num,
          sum(nvl(s.settle_amount,0)) AS settle_amount,
          sum(case WHEN d.match_type=2 THEN d.bet_amount ELSE 0 END) as live_bet_amount,
          count(case WHEN d.match_type=2 THEN 1 ELSE null END) AS live_order_num,
          sum(case WHEN d.match_type=2 THEN nvl(s.settle_amount,0) ELSE 0 END) AS live_settle_amount,
          sum(case WHEN d.match_type=1 THEN d.bet_amount ELSE 0 END) as pre_bet_amount,
          count(case WHEN d.match_type=1 THEN 1 ELSE null END) AS pre_order_num,
          sum(case WHEN d.match_type=1 THEN nvl(s.settle_amount,0) ELSE 0 END) AS pre_settle_amount,
          sum(case WHEN d.bet_time BETWEEN d.begin_time - (2 * 60 * 60 * 1000) and d.begin_time THEN d.bet_amount ELSE 0 END) as pre_two_bet_amount,
          count(case WHEN d.bet_time BETWEEN d.begin_time - (2 * 60 * 60 * 1000) and d.begin_time THEN 1 ELSE null END) AS pre_two_order_num,
          sum(case WHEN d.bet_time BETWEEN d.begin_time - (2 * 60 * 60 * 1000) and d.begin_time THEN nvl(s.settle_amount,0) ELSE 0 END) AS pre_two_settle_amount,
          sum(case WHEN d.bet_time BETWEEN d.begin_time - (1 * 60 * 60 * 1000) and d.begin_time THEN d.bet_amount ELSE 0 END) as pre_one_bet_amount,
          count(case WHEN d.bet_time BETWEEN d.begin_time - (1 * 60 * 60 * 1000) and d.begin_time THEN 1 ELSE null END) AS pre_one_order_num,
          sum(case WHEN d.bet_time BETWEEN d.begin_time - (1 * 60 * 60 * 1000) and d.begin_time THEN nvl(s.settle_amount,0) ELSE 0 END) AS pre_one_settle_amount
   FROM dwm_report.order_match_detail d
   LEFT JOIN dwm_report.t_settle_full_detail s ON d.order_no=s.order_no
   left join base_temp.bss_order o on d.order_no=o.order_no
   WHERE o.series_type=1
     AND o.order_status in (0,1)
     AND d.bet_time BETWEEN unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),1))*1000 +(12 * 60 * 60 * 1000) AND unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),0))*1000 +(12 * 60 * 60 * 1000 - 1)
   GROUP BY d.match_id,
            d.uid,
            d.match_info,
            d.sport_id,
            d.begin_time,
            d.tournament_id) p
LEFT JOIN dwm_report.user_full_info u ON p.uid=u.uid
LEFT JOIN
  (SELECT sum(a.bet_amount) AS total_bet_amount,
          a.match_id
   FROM dwm_report.order_match_detail a
   left join base_temp.bss_order o on a.order_no=o.order_no
   WHERE o.series_type=1
     AND o.order_status in (0,1)
   GROUP BY a.match_id) d ON p.match_id=d.match_id