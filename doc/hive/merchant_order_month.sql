INSERT INTO TABLE dws_report.merchant_order_month
SELECT Concat(a.merchant_code, Date_format(CURRENT_DATE(), 'yyyyMM')) AS id,
       a.merchant_code,
       Date_format(CURRENT_DATE(), 'yyyyMM')                          AS time,
       Round(b.order_user_count / a.user_all_count, 2)                AS bet_user_rate,
       b.profit_amount                                                AS profit,
       a.user_register_count                                          AS register_amount,
       a.user_all_count,
       Round(b.profit_amount / b.bet_amount, 2)                       AS profit_rate,
       b.return_amount,
       Round(b.return_amount / b.bet_amount, 2)                       AS return_rate,
       a.LEVEL                                                        AS merchant_level,
       b.bet_amount,
       b.order_num,
       b.first_bet_user,
       b.order_user_count                                             AS bet_user_amount,
       Round(c.settle_user_count / a.user_all_count, 2)               AS settle_user_rate,
       c.settle_profit,
       c.settle_return,
       Round(c.settle_return / c.settle_bet_amount, 2)                AS settle_return_rate,
       c.settle_bet_amount,
       c.settle_order_num,
       Round(d.live_user_num / a.user_all_count, 2)                   AS live_user_rate,
       d.live_profit,
       d.live_return,
       Round(d.live_return / d.live_bet_amount, 2)                    AS live_return_rate,
       d.live_bet_amount,
       d.live_order_num,
       a.merchant_name
FROM   (SELECT Sum(CASE
                     WHEN From_unixtime(Cast(Substring(ui.user_create_time, 0, 10) AS BIGINT), 'yyyyMM') = Date_format(CURRENT_DATE (), 'yyyyMM') THEN 1
                     ELSE 0
                   END) AS user_register_count,
               Count(0) AS user_all_count,
               ui.merchant_code,
               ui.merchant_name,
               ui.LEVEL
        FROM   dwm_report.user_full_info ui
        GROUP  BY ui.merchant_code,
                  ui.merchant_name,
                  ui.LEVEL) a
       LEFT JOIN (SELECT Sum(Cast(o.order_amount_total AS BIGINT)) AS bet_amount,
                         Count(DISTINCT o.uid)                     AS order_user_count,
                         Count(o.order_no)                         AS order_num,
                         Sum(Cast(s.settle_amount AS BIGINT))      AS return_amount,
                         Sum(Cast(s.profit_amount AS BIGINT))      AS profit_amount,
                         Sum(CASE
                               WHEN From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyyMM') = Date_format(CURRENT_DATE (), 'yyyyMM') THEN 1
                               ELSE 0
                             END)                                  AS first_bet_user,
                         u.merchant_code
                  FROM   base_temp.bss_order o
                         LEFT JOIN dwm_report.user_full_info u
                                ON o.uid = u.uid
                         LEFT JOIN base_temp.bss_settle s
                                ON s.order_no = o.order_no
                  WHERE  Date_format(o.dt, 'yyyyMM') = Date_format(CURRENT_DATE(), 'yyyyMM')
                    and  Date_format(s.dt, 'yyyyMM') >= Date_format(o.dt, 'yyyyMM')
                  GROUP  BY u.merchant_code) b
              ON a.merchant_code = b.merchant_code
       LEFT JOIN (SELECT Sum(Cast(settle_amount AS BIGINT))   AS settle_return,
                         Sum(Cast(s.bet_amount AS BIGINT))    AS settle_bet_amount,
                         Sum(Cast(s.profit_amount AS BIGINT)) AS settle_profit,
                         Count(DISTINCT s.uid)                AS settle_user_count,
                         Count(s.order_no)                    AS settle_order_num,
                         Sum(Cast(s.settle_amount AS BIGINT)) AS return_amount,
                         s.merchant_code
                  FROM   dwm_report.t_settle_full_detail s
                  WHERE  Date_format(s.dt, 'yyyyMM') = Date_format(CURRENT_DATE(), 'yyyyMM')
                  GROUP  BY s.merchant_code) c
              ON a.merchant_code = c.merchant_code
       LEFT JOIN (SELECT Sum(Cast(d.bet_amount AS BIGINT))    AS live_bet_amount,
                         Count(DISTINCT d.uid)                AS live_user_num,
                         Count(DISTINCT d.order_no)           AS live_order_num,
                         Sum(Cast(s.settle_amount AS BIGINT)) AS live_return,
                         Sum(Cast(s.profit_amount AS BIGINT)) AS live_profit,
                         d.merchant_code
                  FROM   dwm_report.order_match_detail d
                         LEFT JOIN base_temp.bss_order o
                                ON d.order_no = o.order_no
                         LEFT JOIN base_temp.bss_settle s
                                ON d.order_no = s.order_no
                  WHERE  o.series_type = 1
                         AND Date_format(d.dt, 'yyyyMM') = Date_format(CURRENT_DATE(), 'yyyyMM')
                         AND Date_format(o.dt, 'yyyyMM') >= Date_format(d.dt, 'yyyyMM')
                         AND Date_format(s.dt, 'yyyyMM') >= Date_format(d.dt, 'yyyyMM')
                  GROUP  BY d.merchant_code) d
              ON a.merchant_code = d.merchant_code
