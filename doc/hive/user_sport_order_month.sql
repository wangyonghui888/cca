INSERT into TABLE dws_report.user_sport_order_month
SELECT Concat(m.uid, m.sport_id, Date_format(CURRENT_DATE (), 'yyyyMM')) AS id,
       m.sport_id,
       m.uid                                                             AS user_id,
       m.username                                                        AS user_name,
       m.merchant_code,
       bet_num,
       m.bet_amount,
       m.profit,
       Round(m.profit / m.bet_amount, 2)                                 AS profit_rate,
       kk.settle_order_num,
       kk.settle_order_amount,
       kk.settle_profit,
       Round(kk.settle_profit / kk.settle_order_amount, 2)               AS settle_profit_rate,
       bb.live_order_num,
       bb.live_order_amount,
       bb.live_profit,
       Round(bb.live_profit / bb.live_order_amount, 2)                   AS live_profit_rate,
       Date_format(CURRENT_DATE (), 'yyyyMM')                            AS time,
       n.active_days,
       m.merchant_name
FROM   (SELECT a.uid                uid,
               ui.username          username,
               s.sport_id           sport_id,
               s.merchant_code      merchant_code,
               s.merchant_name      merchant_name,
               Sum(a.bet_amount)    bet_amount,
               Count(a.order_no)    bet_num,
               Sum(a.profit_amount) profit
        FROM   t_settle_full_detail a
               LEFT JOIN t_order_full_detail s
                      ON a.order_no = s.order_no
               LEFT JOIN user_full_info ui
                      ON ui.uid = a.uid
        WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyyMM') <= Date_format(CURRENT_DATE(), 'yyyyMM')
        GROUP  BY a.uid,
                  ui.username,
                  s.merchant_code,
                  s.merchant_name) m
       LEFT JOIN (SELECT a.uid                uid,
                         a.sport_id           sport_id,
                         Sum(a.bet_amount)    live_order_amount,
                         Count(a.order_no)    live_order_num,
                         Sum(s.profit_amount) live_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                         LEFT JOIN order_match_detail md
                                ON a.order_no = a.order_no
                  WHERE  From_unixtime(Cast(Substring(md.begin_time, 0, 10) AS BIGINT), 'yyyyMM') <= Date_format(CURRENT_DATE(), 'yyyyMM')
                  GROUP  BY a.uid,
                            a.sport_id) bb
              ON bb.uid = m.uid
       LEFT JOIN (SELECT a.uid                uid,
                         a.sport_id           sport_id,
                         Sum(a.bet_amount)    settle_bet_amount,
                         Count(a.order_no)    settle_order_num,
                         Sum(s.profit_amount) settle_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                  WHERE  a.series_type = 1
                         AND From_unixtime(Cast(Substring(s.settle_time, 0, 10) AS BIGINT), 'yyyyMM') <= Date_format(CURRENT_DATE(), 'yyyyMM')
                  GROUP  BY a.uid,
                            a.sport_id) kk
              ON kk.uid = m.uid
       LEFT JOIN(SELECT uid      AS uid,
                        Count(*) AS active_days
                 FROM   (SELECT m.uid uid,
                                m.active_days
                         FROM   (SELECT s.uid                                                                      uid,
                                        From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyyMMdd') AS active_days
                                 FROM   t_order_full_detail s) m
                         GROUP  BY m.uid,
                                   m.active_days
                         ORDER  BY m.active_days DESC) k
                 GROUP  BY k.uid)n
              ON n.uid = kk.uid
