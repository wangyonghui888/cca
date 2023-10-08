INSERT into TABLE dws_report.user_sport_order_day
SELECT Concat(m.uid, m.sport_id, Date_format(CURRENT_DATE (), 'yyyyMMdd')) AS id,
       m.sport_id,
       m.uid                                                               user_id,
       m.username                                                          user_name,
       m.merchant_code,
       bet_num,
       m.bet_amount,
       m.profit,
       Round(m.profit / m.bet_amount, 2)                                   AS profit_rate,
       kk.settle_order_num,
       kk.settle_order_amount,
       kk.settle_profit,
       Round(kk.settle_profit / kk.settle_order_amount, 2)                 AS settle_profit_rate,
       bb.live_order_num,
       bb.live_order_amount,
       bb.live_profit,
       Round(bb.live_profit / bb.live_order_amount, 2)                     AS live_profit_rate,
       Date_format(CURRENT_DATE (), 'yyyyMMdd')                            AS time,
       m.merchant_name
FROM   (SELECT a.uid                uid,
               ui.username          username,
               od.sport_id          sport_id,
               od.merchant_code     merchant_code,
               od.merchant_name     merchant_name,
               Sum(a.bet_amount)    bet_amount,
               Count(a.order_no)    bet_num,
               Sum(a.profit_amount) profit
        FROM   t_settle_full_detail a
               LEFT JOIN t_order_full_detail od
                      ON a.uid = od.uid
               LEFT JOIN user_full_info ui
                      ON ui.uid = a.uid
        GROUP  BY a.uid,
                  od.sport_id,
                  ui.username,
                  od.merchant_code,
                  od.merchant_name) m
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
                  WHERE  From_unixtime(Cast(Substring(md.begin_time, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') <= Date_format(CURRENT_DATE(), 'yyyyMMdd 11:59:59')
                  GROUP  BY a.uid,
                            a.sport_id) bb
              ON bb.uid = m.uid
       LEFT JOIN (SELECT a.uid                uid,
                         a.sport_id           sport_id,
                         Sum(a.bet_amount)    settle_order_amount,
                         Count(a.order_no)    settle_order_num,
                         Sum(s.profit_amount) settle_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                  WHERE  a.series_type = 1
                         AND From_unixtime(Cast(Substring(s.settle_time, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') <= Date_format(CURRENT_DATE(), 'yyyyMMdd 11:59:59')
                  GROUP  BY a.uid,
                            a.sport_id) kk
              ON kk.uid = m.uid
