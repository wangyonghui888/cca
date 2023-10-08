
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
                WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), ' 12:00:00')
                       AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), ' 12:00:00')
                       AND r.merchant_name IS NOT NULL
                GROUP  BY r.merchant_name,
                          s.sport_id) m
               LEFT JOIN (SELECT Count(1)   user_all_count,
                                 merchant_id,
                                 Count(CASE
                                         WHEN From_unixtime(Cast(Substring(user_create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), ' 12:00:00')
                                              AND From_unixtime(Cast(Substring(user_create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), ' 12:00:00') THEN 1
                                         ELSE NULL
                                       END) AS user_register_count,
                                 Count(CASE
                                         WHEN From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), ' 12:00:00')
                                              AND From_unixtime(Cast(Substring(first_bet_date, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), ' 12:00:00') THEN 1
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
                                               WHEN From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), ' 12:00:00')
                                                    AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), ' 12:00:00') THEN s.settle_amount
                                               ELSE 0
                                             END ) AS BIGINT))         AS return_amount,
                                 s.merchant_code
                          FROM   dwm_report.t_settle_full_detail s
                                 LEFT JOIN base_temp.bss_order_detail o
                                        ON o.order_no = s.order_no
                                           AND s.series_type = 1
                          WHERE  From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), ' 12:00:00')
                                 AND From_unixtime(Cast(Substring(s.create_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), ' 12:00:00')
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
                                    WHERE  From_unixtime(Cast(Substring(s.begin_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') > Concat(Date_format(Trunc(Add_months(CURRENT_TIMESTAMP, -1), 'MM'), 'yyyy-MM-dd'), ' 12:00:00')
                                           AND From_unixtime(Cast(Substring(s.begin_time, 0, 10) AS BIGINT), 'yyyy-MM-dd HH:ss:mm') <= Concat(Date_format(Date_sub(CURRENT_DATE, Dayofmonth(CURRENT_DATE) - 1), 'yyyy-MM-dd'), ' 12:00:00')
                                    GROUP  BY s.merchant_code,
                                              s.sport_id) n
                                ON m.merchant_code = n.merchant_code
                                   AND m.sport_id = n.sport_id) b
              ON a.merchant_code = b.merchant_code
                 AND a.sport_id = b.sport_id
