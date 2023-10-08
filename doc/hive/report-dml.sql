
----------------------------------------------------------------------------1  商户日订单统计表 :每天下午13点跑一遍昨天的数据.
INSERT INTO  TABLE dws_report.merchant_order_day
SELECT Concat(a.merchant_code, Date_format(CURRENT_DATE(), 'yyyyMMdd')) AS id,
       a.merchant_code,
       Date_format(CURRENT_DATE(), 'yyyyMMdd')                          AS time,
       Round(b.order_user_count / a.user_all_count, 2)                  AS bet_user_rate,
       b.profit_amount                                                  AS profit,
       a.user_register_count                                            AS register_amount,
       a.user_all_count,
       Round(b.profit_amount / b.bet_amount, 2)                         AS profit_rate,
       b.return_amount,
       Round(b.return_amount / b.bet_amount, 2)                         AS return_rate,
       a.LEVEL                                                          AS merchant_level,
       b.bet_amount,
       b.order_num,
       b.first_bet_user,
       b.order_user_count                                               AS bet_user_amount,
       Round(c.settle_user_count / a.user_all_count, 2)                 AS settle_user_rate,
       c.settle_profit,
       c.settle_return,
       Round(c.settle_return / c.settle_bet_amount, 2)                  AS settle_return_rate,
       c.settle_bet_amount,
       c.settle_order_num,
       Round(d.live_user_num / a.user_all_count, 2)                     AS live_user_rate,
       d.live_profit,
       d.live_return,
       Round(d.live_return / d.live_bet_amount, 2)                      AS live_return_rate,
       d.live_bet_amount,
       d.live_order_num,
       a.merchant_name
FROM   (SELECT Sum(CASE
                       WHEN From_unixtime(Cast(Substring(ui.user_create_time, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') <= Date_format(CURRENT_DATE(), 'yyyyMMdd 11:59:59')
                            AND From_unixtime(Cast(Substring(ui.user_create_time, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') >= Date_format(Date_add(CURRENT_DATE(), -1), 'yyyyMMdd 12:00:00') THEN 1
                       ELSE 0
                     END) AS user_register_count,
               Count(0)   AS user_all_count,
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
                                 WHEN From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') <= Date_format(CURRENT_DATE(), 'yyyyMMdd 11:59:59')
                                      AND From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') >= Date_format(Date_add(CURRENT_DATE(), -1), 'yyyyMMdd 12:00:00') THEN 1
                                 ELSE 0
                               END)                                AS first_bet_user,
                         u.merchant_code
                  FROM   base_temp.bss_order o
                         LEFT JOIN dwm_report.user_full_info u
                                ON o.uid = u.uid
                         LEFT JOIN base_temp.bss_settle s
                                ON s.order_no = o.order_no
                  WHERE  o.dt = CURRENT_DATE()
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
                  WHERE  s.dt = CURRENT_DATE()
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
                         AND d.dt = CURRENT_DATE()
                  GROUP  BY d.merchant_code) d
              ON a.merchant_code = d.merchant_code

			  
			  
			  
--------------------------------------------------------------------------2 商户月订单统计表: 每天下午13点 跑一遍当月的数据;每月一号13点 跑一遍上月的数据.
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
                  GROUP  BY d.merchant_code) d
              ON a.merchant_code = d.merchant_code

---------------------------------------------------------------------------------3 商户周订单统计表:  每天下午13点 跑一遍当周的数据;每周一 下午13点 跑一遍上周的数据.
INSERT INTO TABLE dws_report.merchant_order_week
SELECT Concat(a.merchant_code, Date_format(CURRENT_DATE(), 'yyyyww')) AS id,
       a.merchant_code,
       Date_format(CURRENT_DATE(), 'yyyyww')                          AS time,
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
                       WHEN From_unixtime(Cast(Substring(ui.user_create_time, 0, 10) AS BIGINT), 'yyyyww') = Date_format(CURRENT_DATE (), 'yyyyww') THEN 1
                       ELSE 0
                     END) AS user_register_count,
               Count(0)   AS user_all_count,
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
                                 WHEN From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyyww') = Date_format(CURRENT_DATE (), 'yyyyww') THEN 1
                                 ELSE 0
                               END)                                AS first_bet_user,
                         u.merchant_code
                  FROM   base_temp.bss_order o
                         LEFT JOIN dwm_report.user_full_info u
                                ON o.uid = u.uid
                         LEFT JOIN base_temp.bss_settle s
                                ON s.order_no = o.order_no
                  WHERE  Date_format(o.dt, 'yyyyww') = Date_format(CURRENT_DATE(), 'yyyyww')
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
                  WHERE  Date_format(s.dt, 'yyyyww') = Date_format(CURRENT_DATE(), 'yyyyww')
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
                         AND Date_format(d.dt, 'yyyyww') = Date_format(CURRENT_DATE(), 'yyyyww')
                  GROUP  BY d.merchant_code) d
              ON a.merchant_code = d.merchant_code


---------------------------------------------------------------------------4 商户体种日订单统计表 :每天下午13点跑一遍昨天的数据.
INSERT into  TABLE dws_report.merchant_sport_order_day
SELECT Concat(a.merchant_code, a.sport_id, Cast(Date_format(CURRENT_DATE, 'yyyyMMdd') AS BIGINT) - 1)id,
       a.sport_id,
       a.merchant_code,
       Cast(Date_format(CURRENT_DATE, 'yyyyMMdd') AS BIGINT) - 1                                     time,
       Round(order_user_count / user_all_count, 2)                                                   AS bet_user_rate,
       sum_bet_amount - return_amount                                                                AS profit,
       user_register_count                                                                           AS register_amount,
       user_all_count                                                                                AS user_all_count,
       Round(return_amount / sum_bet_amount, 2)                                                      AS return_rate,
       return_amount,
       level                                                                                         AS merchant_level,
       sum_bet_amount                                                                                AS bet_amount,
       order_num,
       first_order_user_count                                                                        AS first_bet_user,
       order_user_count                                                                              AS bet_user_amount,
       Round(settle_user_count / user_all_count, 2)                                                  AS settle_user_rate,
       settle_bet_amount - settle_return                                                             AS settle_profit,
       settle_return,
       Round(settle_return / settle_bet_amount, 2)                                                   AS settle_return_rate,
       settle_bet_amount,
       settle_order_num,
       Round(live_user_num / user_all_count, 2)                                                      AS live_user_rate,
       live_bet_amount - live_return                                                                 AS live_profit,
       live_return,
       Round(live_return / live_bet_amount, 2)                                                       AS live_return_rate,
       live_bet_amount,
       live_order_num,
       merchant_name
FROM   (SELECT user_all_count,
               n.merchant_id,
               user_register_count,
               first_order_user_count,
               n.merchant_name,
               level,
               order_user_count,
               order_num,
               sum_bet_amount,
               n.merchant_code,
               m.sport_id
        FROM   (SELECT Count(DISTINCT s.uid)                    AS order_user_count,
                       Count(DISTINCT order_no)                 AS order_num,
                       Sum(Cast (order_amount_total AS BIGINT)) AS sum_bet_amount,
                       r.merchant_name,
                       s.sport_id
                FROM   dwm_report.t_order_full_detail s
                       LEFT JOIN dwm_report.user_full_info r
                              ON s.uid = r.uid
                WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 1), 'yyyy-MM-dd'), " 12:00:00")
                       AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00")
                       AND r.merchant_name IS NOT NULL
                GROUP  BY r.merchant_name,
                          s.sport_id) m
               LEFT JOIN (SELECT Count(1)   user_all_count,
                                 merchant_id,
                                 Count(CASE
                                         WHEN From_unixtime(Cast(Substring(user_create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 1), 'yyyy-MM-dd'), " 12:00:00")
                                              AND From_unixtime(Cast(Substring(user_create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00") THEN 1
                                         ELSE NULL
                                       END) AS user_register_count,
                                 Count(CASE
                                         WHEN From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 1), 'yyyy-MM-dd'), " 12:00:00")
                                              AND From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00") THEN 1
                                         ELSE NULL
                                       END) AS first_order_user_count,
                                 merchant_name,
                                 level,
                                 merchant_code
                          FROM   dwm_report.user_full_info
                          GROUP  BY merchant_name,
                                    level,
                                    merchant_id,
                                    merchant_code) n
                      ON m.merchant_name = n.merchant_name) a
       LEFT JOIN (SELECT settle_return,
                         settle_bet_amount,
                         settle_user_count,
                         settle_order_num,
                         return_amount,
                         m.merchant_code,
                         live_return,
                         live_bet_amount,
                         live_user_num,
                         live_order_num,
                         m.sport_id
                  FROM   (SELECT Sum(Cast (s.settle_amount AS BIGINT)) AS settle_return,
                                 Sum(Cast (s.bet_amount AS BIGINT))    AS settle_bet_amount,
                                 Count(DISTINCT s.uid)                 AS settle_user_count,
                                 Count(s.order_no)                     AS settle_order_num,
                                 o.sport_id,
                                 Sum(Cast (( CASE
                                               WHEN From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 1), 'yyyy-MM-dd'), " 12:00:00")
                                                    AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00") THEN s.settle_amount
                                               ELSE 0
                                             END ) AS BIGINT))         AS return_amount,
                                 s.merchant_code
                          FROM   dwm_report.t_settle_full_detail s
                                 LEFT JOIN base_temp.bss_order_detail o
                                        ON o.order_no = s.order_no
                                           AND s.series_type = 1
                          WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 1), 'yyyy-MM-dd'), " 12:00:00")
                                 AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00")
                          GROUP  BY s.merchant_code,
                                    o.sport_id) m
                         LEFT JOIN (SELECT Sum(Cast (o.settle_amount AS BIGINT)) AS live_return,
                                           Sum(Cast (o.bet_amount AS BIGINT))    AS live_bet_amount,
                                           Count(DISTINCT s.uid)                 AS live_user_num,
                                           Count(DISTINCT s.order_no)            AS live_order_num,
                                           s.merchant_code                       AS merchant_code,
                                           s.sport_id
                                    FROM   dwm_report.order_match_detail s
                                           LEFT JOIN dwm_report.t_settle_full_detail o
                                                  ON s.order_no = o.order_no
                                                     AND o.series_type = 1
                                    WHERE  From_unixtime(Cast(Substring(s.begin_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 1), 'yyyy-MM-dd'), " 12:00:00")
                                           AND From_unixtime(Cast(Substring(s.begin_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00")
                                    GROUP  BY s.merchant_code,
                                              s.sport_id) n
                                ON m.merchant_code = n.merchant_code
                                   AND m.sport_id = n.sport_id) b
              ON a.merchant_code = b.merchant_code
                 AND a.sport_id = b.sport_id


-------------------------------------------------------------5 商户体种月订单统计表: 每天下午13点 跑一遍当月的数据;每月一号13点 跑一遍上月的数据.

INSERT into  TABLE dws_report.merchant_sport_order_month
SELECT Concat(a.merchant_code, a.sport_id, Cast(Date_format(CURRENT_DATE, 'yyyyMM') AS BIGINT) - 1) id,
       a.sport_id,
       a.merchant_code,
       Cast(Date_format(CURRENT_DATE, 'yyyyMM') AS BIGINT) - 1                                      time,
       Round(order_user_count / user_all_count, 2)                                                  AS bet_user_rate,
       sum_bet_amount - return_amount                                                               AS profit,
       user_register_count                                                                          AS register_amount,
       user_all_count                                                                               AS user_all_count,
       Round(return_amount / sum_bet_amount, 2)                                                     AS return_rate,
       return_amount,
       level                                                                                        AS merchant_level,
       sum_bet_amount                                                                               AS bet_amount,
       order_num,
       first_order_user_count                                                                       AS first_bet_user,
       order_user_count                                                                             AS bet_user_amount,
       Round(settle_user_count / user_all_count, 2)                                                 AS settle_user_rate,
       settle_bet_amount - settle_return                                                            AS settle_profit,
       settle_return,
       Round(settle_return / settle_bet_amount, 2)                                                  AS settle_return_rate,
       settle_bet_amount,
       settle_order_num,
       Round(live_user_num / user_all_count, 2)                                                     AS live_user_rate,
       live_bet_amount - live_return                                                                AS live_profit,
       live_return,
       Round(live_return / live_bet_amount, 2)                                                      AS live_return_rate,
       live_bet_amount,
       live_order_num,
       merchant_name
FROM   (SELECT user_all_count,
               n.merchant_id,
               user_register_count,
               first_order_user_count,
               n.merchant_name,
               level,
               order_user_count,
               order_num,
               sum_bet_amount,
               n.merchant_code,
               m.sport_id
        FROM   (SELECT Count(DISTINCT s.uid)                    AS order_user_count,
                       Count(DISTINCT order_no)                 AS order_num,
                       Sum(Cast (order_amount_total AS BIGINT)) AS sum_bet_amount,
                       r.merchant_name,
                       s.sport_id
                FROM   dwm_report.t_order_full_detail s
                       LEFT JOIN dwm_report.user_full_info r
                              ON s.uid = r.uid
                WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), " 12:00:00")
                       AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), " 12:00:00")
                       AND r.merchant_name IS NOT NULL
                GROUP  BY r.merchant_name,
                          s.sport_id) m
               LEFT JOIN (SELECT Count(1)   user_all_count,
                                 merchant_id,
                                 Count(CASE
                                         WHEN From_unixtime(Cast(Substring(user_create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), " 12:00:00")
                                              AND From_unixtime(Cast(Substring(user_create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), " 12:00:00") THEN 1
                                         ELSE NULL
                                       END) AS user_register_count,
                                 Count(CASE
                                         WHEN From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), " 12:00:00")
                                              AND From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), " 12:00:00") THEN 1
                                         ELSE NULL
                                       END) AS first_order_user_count,
                                 merchant_name,
                                 level,
                                 merchant_code
                          FROM   dwm_report.user_full_info
                          GROUP  BY merchant_name,
                                    level,
                                    merchant_id,
                                    merchant_code) n
                      ON m.merchant_name = n.merchant_name) a
       LEFT JOIN (SELECT settle_return,
                         settle_bet_amount,
                         settle_user_count,
                         settle_order_num,
                         return_amount,
                         m.merchant_code,
                         live_return,
                         live_bet_amount,
                         live_user_num,
                         live_order_num,
                         m.sport_id
                  FROM   (SELECT Sum(Cast (s.settle_amount AS BIGINT)) AS settle_return,
                                 Sum(Cast (s.bet_amount AS BIGINT))    AS settle_bet_amount,
                                 Count(DISTINCT s.uid)                 AS settle_user_count,
                                 Count(s.order_no)                     AS settle_order_num,
                                 o.sport_id,
                                 Sum(Cast (( CASE
                                               WHEN From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), " 12:00:00")
                                                    AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), " 12:00:00") THEN s.settle_amount
                                               ELSE 0
                                             END ) AS BIGINT))         AS return_amount,
                                 s.merchant_code
                          FROM   dwm_report.t_settle_full_detail s
                                 LEFT JOIN base_temp.bss_order_detail o
                                        ON o.order_no = s.order_no
                                           AND s.series_type = 1
                          WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), " 12:00:00")
                                 AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), " 12:00:00")
                          GROUP  BY s.merchant_code,
                                    o.sport_id) m
                         LEFT JOIN (SELECT Sum(Cast (o.settle_amount AS BIGINT)) AS live_return,
                                           Sum(Cast (o.bet_amount AS BIGINT))    AS live_bet_amount,
                                           Count(DISTINCT s.uid)                 AS live_user_num,
                                           Count(DISTINCT s.order_no)            AS live_order_num,
                                           s.merchant_code                       AS merchant_code,
                                           s.sport_id
                                    FROM   dwm_report.order_match_detail s
                                           LEFT JOIN dwm_report.t_settle_full_detail o
                                                  ON s.order_no = o.order_no
                                                     AND o.series_type = 1
                                    WHERE  From_unixtime(Cast(Substring(s.begin_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), " 12:00:00")
                                           AND From_unixtime(Cast(Substring(s.begin_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), " 12:00:00")
                                    GROUP  BY s.merchant_code,
                                              s.sport_id) n
                                ON m.merchant_code = n.merchant_code
                                   AND m.sport_id = n.sport_id) b
              ON a.merchant_code = b.merchant_code
                 AND a.sport_id = b.sport_id


---------------------------------------------------------------------------6  商户体种周订单统计表:  每天下午13点 跑一遍当周的数据;每周一 下午13点 跑一遍上周的数据.
INSERT into  TABLE dws_report.merchant_sport_order_week
SELECT Concat(a.merchant_code, a.sport_id, Cast(Date_format(CURRENT_DATE, 'yyyyww') AS BIGINT) - 1)id,
       a.sport_id,
       a.merchant_code,
       Cast(Date_format(CURRENT_DATE, 'yyyyww') AS BIGINT) - 1                                     time,
       Round(order_user_count / user_all_count, 2)                                                 AS bet_user_rate,
       sum_bet_amount - return_amount                                                              AS profit,
       user_all_count                                                                              AS user_all_count,
       user_register_count                                                                         AS register_amount,
       Round(return_amount / sum_bet_amount, 2)                                                    AS return_rate,
       return_amount,
       level                                                                                       AS merchant_level,
       sum_bet_amount                                                                              AS bet_amount,
       order_num,
       first_order_user_count                                                                      AS first_bet_user,
       order_user_count                                                                            AS bet_user_amount,
       Round(settle_user_count / user_all_count, 2)                                                AS settle_user_rate,
       settle_bet_amount - settle_return                                                           AS settle_profit,
       settle_return,
       Round(settle_return / settle_bet_amount, 2)                                                 AS settle_return_rate,
       settle_bet_amount,
       settle_order_num,
       Round(live_user_num / user_all_count, 2)                                                    AS live_user_rate,
       live_bet_amount - live_return                                                               AS live_profit,
       live_return,
       Round(live_return / live_bet_amount, 2)                                                     AS live_return_rate,
       live_bet_amount,
       live_order_num,
       merchant_name
FROM   (SELECT user_all_count,
               n.merchant_id,
               user_register_count,
               first_order_user_count,
               n.merchant_name,
               level,
               order_user_count,
               order_num,
               sum_bet_amount,
               n.merchant_code,
               m.sport_id
        FROM   (SELECT Count(DISTINCT s.uid)                    AS order_user_count,
                       Count(DISTINCT order_no)                 AS order_num,
                       Sum(Cast (order_amount_total AS BIGINT)) AS sum_bet_amount,
                       r.merchant_name,
                       s.sport_id
                FROM   dwm_report.t_order_full_detail s
                       LEFT JOIN dwm_report.user_full_info r
                              ON s.uid = r.uid
                WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 7), 'yyyy-MM-dd'), " 12:00:00")
                       AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00")
                       AND r.merchant_name IS NOT NULL
                GROUP  BY r.merchant_name,
                          s.sport_id) m
               LEFT JOIN (SELECT Count(1)   user_all_count,
                                 merchant_id,
                                 Count(CASE
                                         WHEN From_unixtime(Cast(Substring(user_create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 7), 'yyyy-MM-dd'), " 12:00:00")
                                              AND From_unixtime(Cast(Substring(user_create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00") THEN 1
                                         ELSE NULL
                                       END) AS user_register_count,
                                 Count(CASE
                                         WHEN From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 7), 'yyyy-MM-dd'), " 12:00:00")
                                              AND From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00") THEN 1
                                         ELSE NULL
                                       END) AS first_order_user_count,
                                 merchant_name,
                                 level,
                                 merchant_code
                          FROM   dwm_report.user_full_info
                          GROUP  BY merchant_name,
                                    level,
                                    merchant_id,
                                    merchant_code) n
                      ON m.merchant_name = n.merchant_name) a
       LEFT JOIN (SELECT settle_return,
                         settle_bet_amount,
                         settle_user_count,
                         settle_order_num,
                         return_amount,
                         m.merchant_code,
                         live_return,
                         live_bet_amount,
                         live_user_num,
                         live_order_num,
                         m.sport_id
                  FROM   (SELECT Sum(Cast (s.settle_amount AS BIGINT)) AS settle_return,
                                 Sum(Cast (s.bet_amount AS BIGINT))    AS settle_bet_amount,
                                 Count(DISTINCT s.uid)                 AS settle_user_count,
                                 Count(s.order_no)                     AS settle_order_num,
                                 o.sport_id,
                                 Sum(Cast (( CASE
                                               WHEN From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 7), 'yyyy-MM-dd'), " 12:00:00")
                                                    AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00") THEN s.settle_amount
                                               ELSE 0
                                             END ) AS BIGINT))         AS return_amount,
                                 s.merchant_code
                          FROM   dwm_report.t_settle_full_detail s
                                 LEFT JOIN base_temp.bss_order_detail o
                                        ON o.order_no = s.order_no
                                           AND s.series_type = 1
                          WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 7), 'yyyy-MM-dd'), " 12:00:00")
                                 AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00")
                          GROUP  BY s.merchant_code,
                                    o.sport_id) m
                         LEFT JOIN (SELECT Sum(Cast (o.settle_amount AS BIGINT)) AS live_return,
                                           Sum(Cast (o.bet_amount AS BIGINT))    AS live_bet_amount,
                                           Count(DISTINCT s.uid)                 AS live_user_num,
                                           Count(DISTINCT s.order_no)            AS live_order_num,
                                           s.merchant_code                       AS merchant_code,
                                           s.sport_id
                                    FROM   dwm_report.order_match_detail s
                                           LEFT JOIN dwm_report.t_settle_full_detail o
                                                  ON s.order_no = o.order_no
                                                     AND o.series_type = 1
                                    WHERE  From_unixtime(Cast(Substring(s.begin_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Date_sub(CURRENT_DATE, 7), 'yyyy-MM-dd'), " 12:00:00")
                                           AND From_unixtime(Cast(Substring(s.begin_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(CURRENT_DATE, 'yyyy-MM-dd'), " 12:00:00")
                                    GROUP  BY s.merchant_code,
                                              s.sport_id) n
                                ON m.merchant_code = n.merchant_code
                                   AND m.sport_id = n.sport_id) b
              ON a.merchant_code = b.merchant_code
                 AND a.sport_id = b.sport_id



------------------------------------------------------------------------7  用户订单天表:每天下午13点跑一遍昨天的数据.
INSERT into TABLE dws_report.user_order_day
SELECT Concat(m.uid, Date_format(CURRENT_DATE (), 'yyyyMMdd')) AS id,
       m.uid                                                   AS user_id,
       m.username                                              AS user_name,
       m.merchant_code,
       bet_num,
       m.bet_amount,
       m.profit,
       Round(m.profit / m.bet_amount, 2)                       AS profit_rate,
       kk.settle_order_num,
       kk.settle_order_amount,
       kk.settle_profit,
       Round(kk.settle_profit / kk.settle_order_amount, 2)     AS settle_profit_rate,
       bb.live_order_num,
       bb.live_order_amount,
       bb.live_profit,
       Round(bb.live_profit / bb.live_order_amount, 2)         AS live_profit_rate,
       Date_format(CURRENT_DATE (), 'yyyyMMdd')                AS time,
       m.merchant_name
FROM   (SELECT a.uid                uid,
               ui.username          username,
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
        WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') <= Date_format(CURRENT_DATE(), 'yyyyMMdd 11:59:59')
        GROUP  BY a.uid,
                  ui.username,
                  s.merchant_code,
                  s.merchant_name) m
       LEFT JOIN (SELECT a.uid                uid,
                         Sum(a.bet_amount)    live_order_amount,
                         Count(a.order_no)    live_order_num,
                         Sum(s.profit_amount) live_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                         LEFT JOIN order_match_detail md
                                ON a.order_no = a.order_no
                  WHERE  From_unixtime(Cast(Substring(md.begin_time, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') <= Date_format(CURRENT_DATE(), 'yyyyMMdd 11:59:59')
                  GROUP  BY a.uid) bb
              ON bb.uid = m.uid
       LEFT JOIN (SELECT a.uid                uid,
                         Sum(a.bet_amount)    settle_order_amount,
                         Count(a.order_no)    settle_order_num,
                         Sum(s.profit_amount) settle_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                  WHERE  a.bill_status = 1
                         AND From_unixtime(Cast(Substring(s.settle_time, 0, 10) AS BIGINT), 'yyyyMMdd HH:mm:ss') <= Date_format(CURRENT_DATE(), 'yyyyMMdd 11:59:59')
                  GROUP  BY a.uid) kk
              ON kk.uid = m.uid


------------------------------------------------------------8  用户订单月表 : 每天下午13点 跑一遍当月的数据;每月一号13点 跑一遍上月的数据.
INSERT into TABLE dws_report.user_order_month
SELECT Concat(m.uid, Date_format(CURRENT_DATE (), 'yyyyMM')) AS id,
       m.uid                                                 AS user_id,
       m.username                                            AS user_name,
       m.merchant_code,
       bet_num,
       m.bet_amount,
       m.profit,
       Round(m.profit / m.bet_amount, 2)                     AS profit_rate,
       kk.settle_order_num,
       kk.settle_order_amount,
       kk.settle_profit,
       Round(kk.settle_profit / kk.settle_order_amount, 2)   AS settle_profit_rate,
       bb.live_order_num,
       bb.live_order_amount,
       bb.live_profit,
       Round(bb.live_profit / bb.live_order_amount, 2)       AS live_profit_rate,
       Date_format(CURRENT_DATE (), 'yyyyMM')                AS time,
       n.active_days,
       m.merchant_name
FROM   (SELECT a.uid                uid,
               ui.username          username,
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
                         Sum(a.bet_amount)    live_order_amount,
                         Count(a.order_no)    live_order_num,
                         Sum(s.profit_amount) live_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                         LEFT JOIN order_match_detail md
                                ON a.order_no = a.order_no
                  WHERE  From_unixtime(Cast(Substring(md.begin_time, 0, 10) AS BIGINT), 'yyyyMM') <= Date_format(CURRENT_DATE(), 'yyyyMM')
                  GROUP  BY a.uid) bb
              ON bb.uid = m.uid
       LEFT JOIN (SELECT a.uid                uid,
                         Sum(a.bet_amount)    settle_order_amount,
                         Count(a.order_no)    settle_order_num,
                         Sum(s.profit_amount) settle_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                  WHERE  a.bill_status = 1
                         AND From_unixtime(Cast(Substring(s.settle_time, 0, 10) AS BIGINT), 'yyyyMM') <= Date_format(CURRENT_DATE(), 'yyyyMM')
                  GROUP  BY a.uid) kk
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

	
-----------------------------------------------------------9   用户订单周表 :  每天下午13点 跑一遍当周的数据;每周一 下午13点 跑一遍上周的数据.
INSERT into TABLE dws_report.user_order_week
SELECT Concat(m.uid, Date_format(CURRENT_DATE (), 'yyyyww')) AS id,
       m.uid                                                 AS user_id,
       m.username                                            AS user_name,
       m.merchant_code,
       bet_num,
       m.bet_amount,
       m.profit,
       Round(m.profit / m.bet_amount, 2)                     AS profit_rate,
       kk.settle_order_num,
       kk.settle_order_amount,
       kk.settle_profit,
       Round(kk.settle_profit / kk.settle_order_amount, 2)   AS settle_profit_rate,
       bb.live_order_num,
       bb.live_order_amount,
       bb.live_profit,
       Round(bb.live_profit / bb.live_order_amount, 2)       AS live_profit_rate,
       Date_format(CURRENT_DATE (), 'yyyyww')                AS time,
       n.active_days,
       m.merchant_name
FROM   (SELECT a.uid                uid,
               ui.username          username,
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
        WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyyww') <= Date_format(CURRENT_DATE(), 'yyyyww')
        GROUP  BY a.uid,
                  ui.username,
                  s.merchant_code,
                  s.merchant_name) m
       LEFT JOIN (SELECT a.uid                uid,
                         Sum(a.bet_amount)    live_order_amount,
                         Count(a.order_no)    live_order_num,
                         Sum(s.profit_amount) live_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                         LEFT JOIN order_match_detail md
                                ON a.order_no = a.order_no
                  WHERE  From_unixtime(Cast(Substring(md.begin_time, 0, 10) AS BIGINT), 'yyyyww') <= Date_format(CURRENT_DATE(), 'yyyyww')
                  GROUP  BY a.uid) bb
              ON bb.uid = m.uid
       LEFT JOIN (SELECT a.uid                uid,
                         Sum(a.bet_amount)    settle_order_amount,
                         Count(a.order_no)    settle_order_num,
                         Sum(s.profit_amount) settle_profit
                  FROM   t_order_full_detail a
                         LEFT JOIN t_settle_full_detail s
                                ON a.order_no = s.order_no
                  WHERE  a.bill_status = 1
                         AND From_unixtime(Cast(Substring(s.settle_time, 0, 10) AS BIGINT), 'yyyyww') <= Date_format(CURRENT_DATE(), 'yyyyww')
                  GROUP  BY a.uid) kk
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

	
-------------------------------------------------------------10  	用户订单体种天表:每天下午13点跑一遍昨天的数据.
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


--------------------------------------------------------------11   用户体种订单月表 : 每天下午13点 跑一遍当月的数据;每月一号13点 跑一遍上月的数据.
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

	
----------------------------------------------------------------12 用户体种订单周表 每天下午13点 跑一遍当周的数据;每周一 下午13点 跑一遍上周的数据.
INSERT into TABLE dws_report.user_sport_order_week
SELECT Concat(m.uid, m.sport_id, Date_format(CURRENT_DATE (), 'yyyyww')) AS id,
       m.sport_id,
       m.uid                                                             user_id,
       m.username                                                        user_name,
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
       Date_format(CURRENT_DATE (), 'yyyyww')                            AS time,
       n.active_days,
       m.merchant_name
FROM   (SELECT a.uid                uid,
               s.sport_id           sport_id,
               ui.username          username,
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
        WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyyww') <= Date_format(CURRENT_DATE(), 'yyyyww')
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
                  WHERE  From_unixtime(Cast(Substring(md.begin_time, 0, 10) AS BIGINT), 'yyyyww') <= Date_format(CURRENT_DATE(), 'yyyyww')
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
                         AND From_unixtime(Cast(Substring(s.settle_time, 0, 10) AS BIGINT), 'yyyyww') <= Date_format(CURRENT_DATE(), 'yyyyww')
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

	
	
------------------------------------------------------------13  投注概况分析 每天下午13点 跑一遍昨天的数据
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


-------------------------------------------------14  赛事注单天表汇总 :  每天下午13点跑一遍昨天的数据.
INSERT INTO dws_report.rcs_match_order_day
SELECT concat(p.match_id,p.begin_time) AS mod_id,
       p.begin_time AS begin_time,
       p.match_id AS match_id,
       p.sport_id AS sport_id,
       p.tournament_id AS tournament_id,
       p.match_info AS match_info,
       floor(p.match_amount) AS match_amount,
       floor(p.match_settle_amount) AS match_settle_amount,
       p.match_num AS match_num,
       floor(p.match_amount - p.match_settle_amount) AS match_actual_amount,
       floor(p.live_theory_amount + p.pre_theory_amount) AS match_theory_amount,
       floor(p.match_amount - p.match_settle_amount - p.live_theory_amount + p.pre_theory_amount) AS match_profit_amount,
       nvl(cast((p.match_settle_amount/p.match_amount) * 100 As decimal(10,1)),0)  AS match_proportion,
       floor(p.live_amount) AS live_amount,
       p.live_num AS live_num,
       nvl(floor(p.live_amount/p.live_num),0) AS live_average_amount,
       nvl(round(p.live_num/p.live_user_num,1),0) AS live_average_num,
       nvl(floor(p.live_amount/p.live_user_num),0) AS live_user_amount,
       floor(p.pre_amount) AS pre_amount,
       p.pre_num AS pre_num,
       nvl(floor(p.pre_amount/p.pre_num),0) AS pre_average_amount,
       nvl(round(p.pre_num/p.pre_user_num,1),0) AS pre_average_num,
       nvl(floor(p.pre_amount/p.pre_user_num),0) AS pre_user_amount,
       floor(p.pre_two_amount) AS pre_two_amount,
       p.pre_two_num AS pre_two_num,
       nvl(floor(p.pre_two_amount/p.pre_two_num),0) AS pre_two_average_amount,
       nvl(round(p.pre_two_num/p.pre_two_user_num,1),0) AS pre_two_average_num,
       nvl(floor(p.pre_two_amount/p.pre_two_user_num),0) AS pre_two_user_amount,
       floor(p.pre_one_amount) AS pre_one_amount,
       p.pre_one_num AS pre_one_num,
       nvl(floor(p.pre_one_amount/p.pre_one_num),0) AS pre_one_average_amount,
       nvl(round(p.pre_one_num/p.pre_one_user_num,1),0) AS pre_one_average_num,
       nvl(floor(p.pre_one_amount/p.pre_one_user_num),0) AS pre_one_user_amount,
       floor(p.live_amount - p.live_settle_amount) AS live_actual_amount,
       floor(p.live_theory_amount) AS live_theory_amount,
       floor(p.live_amount - p.live_settle_amount - p.live_theory_amount) AS live_profit_amount,
       nvl(cast((p.live_settle_amount/p.live_amount) * 100 As decimal(10,1)),0) AS live_proportion,
       floor(p.pre_amount - p.pre_settle_amount) AS pre_actual_amount,
       floor(p.pre_theory_amount) AS pre_theory_amount,
       floor(p.pre_amount - p.pre_settle_amount - p.pre_theory_amount) AS pre_profit_amount,
       nvl(cast((p.pre_settle_amount/p.pre_amount) * 100 As decimal(10,1)),0) AS pre_proportion,
       floor(p.pre_two_amount - p.pre_two_settle_amount) AS pre_two_actual_amount,
       floor(p.pre_two_theory_amount) AS pre_two_theory_amount,
       floor(p.pre_two_amount - p.pre_two_settle_amount - p.pre_two_theory_amount) AS pre_two_profit_amount,
       nvl(cast((p.pre_two_settle_amount/p.pre_two_amount) * 100 As decimal(10,1)),0) AS pre_two_proportion,
       floor(p.pre_one_amount - p.pre_one_settle_amount) AS pre_one_actual_amount,
       floor(p.pre_one_theory_amount) AS pre_one_theory_amount,
       floor(p.pre_one_amount - p.pre_one_settle_amount - p.pre_one_theory_amount) AS pre_one_profit_amount,
       nvl(cast((p.pre_one_settle_amount/p.pre_one_amount) * 100 As decimal(10,1)),0) AS pre_one_proportion
FROM
  (SELECT a.begin_time,
          a.match_id,
          a.sport_id,
          a.tournament_id,
          a.match_info,
          sum(a.bet_amount) AS match_amount,
          count(1) AS match_num,
          sum(nvl(s.settle_amount,0)) AS match_settle_amount,
          sum(CASE WHEN a.match_type=2 THEN a.bet_amount ELSE 0 END) AS live_amount,
          count(case WHEN a.match_type=2 THEN 1 ELSE null END) AS live_num,
          count(distinct case WHEN a.match_type=2 THEN a.uid ELSE null END) AS live_user_num,
          sum(CASE WHEN a.match_type=1 THEN a.bet_amount ELSE 0 END) AS pre_amount,
          count(case WHEN a.match_type=1 THEN 1 ELSE null END) AS pre_num,
          count(distinct case WHEN a.match_type=1 THEN a.uid ELSE null END) AS pre_user_num,
          sum(case WHEN a.bet_time BETWEEN a.begin_time - (2 * 60 * 60 * 1000) and a.begin_time THEN a.bet_amount ELSE 0 END) AS pre_two_amount,
          count(case WHEN a.bet_time BETWEEN a.begin_time - (2 * 60 * 60 * 1000) and a.begin_time THEN 1 ELSE null END) AS pre_two_num,
          count(distinct case WHEN a.bet_time BETWEEN a.begin_time - (2 * 60 * 60 * 1000) and a.begin_time THEN a.uid ELSE null END) AS pre_two_user_num,
          sum(case WHEN a.bet_time BETWEEN a.begin_time - (1 * 60 * 60 * 1000) and a.begin_time THEN a.bet_amount ELSE 0 END) AS pre_one_amount,
          count(case WHEN a.bet_time BETWEEN a.begin_time - (1 * 60 * 60 * 1000) and a.begin_time THEN 1 ELSE null END) AS pre_one_num,
          count(distinct case WHEN a.bet_time BETWEEN a.begin_time - (1 * 60 * 60 * 1000) and a.begin_time THEN a.uid ELSE null END) AS pre_one_user_num,
          sum(case WHEN a.match_type=2 THEN nvl(s.settle_amount,0) ELSE 0 END) AS live_settle_amount,
          sum(case WHEN a.match_type=1 THEN nvl(s.settle_amount,0) ELSE 0 END) AS pre_settle_amount,
          sum(case WHEN a.bet_time BETWEEN a.begin_time - (2 * 60 * 60 * 1000) and a.begin_time THEN nvl(s.settle_amount,0) ELSE 0 END) AS pre_two_settle_amount,
          sum(case WHEN a.bet_time BETWEEN a.begin_time - (1 * 60 * 60 * 1000) and a.begin_time THEN nvl(s.settle_amount,0) ELSE 0 END) AS pre_one_settle_amount,
          sum(CASE WHEN a.match_type=2 and a.play_id IN(2,4,19,39,154,155,172,181) THEN a.bet_amount * (106 / 100 - 1) WHEN a.match_type=2 and a.play_id NOT IN(2,4,19,39,154,155,172,181) THEN a.bet_amount * (108.5 / 100 - 1) ELSE 0 END) AS live_theory_amount,
          sum(CASE WHEN a.match_type=1 THEN a.bet_amount * (103.5 / 100 - 1) ELSE 0 END) as pre_theory_amount,
          sum(case WHEN a.bet_time BETWEEN a.begin_time - (2 * 60 * 60 * 1000) and a.begin_time THEN a.bet_amount * (103.5 / 100 - 1) ELSE 0 END) AS pre_two_theory_amount,
          sum(case WHEN a.bet_time BETWEEN a.begin_time - (1 * 60 * 60 * 1000) and a.begin_time THEN a.bet_amount * (103.5 / 100 - 1) ELSE 0 END) AS pre_one_theory_amount
   FROM dwm_report.order_match_detail a
   LEFT JOIN dwm_report.settle_full_detail s ON a.order_no=s.order_no
   left join base_temp.bss_order o on a.order_no=o.order_no
   WHERE o.series_type=1
     AND o.order_status=1
     AND a.bet_time BETWEEN unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),1))*1000 +(12 * 60 * 60 * 1000) AND unix_timestamp(date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),0))*1000 +(12 * 60 * 60 * 1000 - 1)
   GROUP BY a.match_id,
            a.match_info,
            a.sport_id,
            a.begin_time,
            a.tournament_id) p;
			
------------------------------------------------15  赛事注单玩法天表: 每天下午13点跑一遍昨天的数据.
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
   LEFT JOIN dwm_report.settle_full_detail s ON a.order_no=s.order_no
   left join base_temp.bss_order o on a.order_no=o.order_no
   WHERE o.series_type=1
     AND o.order_status=1
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
     AND o.order_status=1
   GROUP BY a.match_id) d ON p.match_id=d.match_id;
   
   -------------------------------------------------------------16 赛事注单用户天表: 每天下午13点跑一遍昨天的数据.
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
   LEFT JOIN dwm_report.settle_full_detail s ON d.order_no=s.order_no
   left join base_temp.bss_order o on d.order_no=o.order_no
   WHERE o.series_type=1
     AND o.order_status=1
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
     AND o.order_status=1
   GROUP BY a.match_id) d ON p.match_id=d.match_id;
   
   
---------------------------------------------17  用户报表 :每天下午13点跑一遍昨天的数据.
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