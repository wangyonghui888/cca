CREATE OR REPLACE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `tybss_report`.`view_r_settle_full_detail` AS
SELECT
    date_format( from_unixtime(( `ts`.`settle_time` / 1000 )), '%Y-%m' ) AS `dt`,
    `ts`.`uid` AS `uid`,
    `ts`.`order_no` AS `order_no`,
    `ts`.`out_come` AS `out_come`,
    `ts`.`settle_amount` AS `settle_amount`,
    `ts`.`settle_type` AS `settle_type`,
    `ts`.`settle_time` AS `settle_time`,
    `ts`.`odd_finally` AS `odd_finally`,
    `ts`.`create_user` AS `create_user`,
    `ts`.`create_time` AS `create_time`,
    `ts`.`modify_user` AS `modify_user`,
    `ts`.`modify_time` AS `modify_time`,
    `ts`.`bet_amount` AS `bet_amount`,
    `ts`.`odds_value` AS `odds_value`,
    `ts`.`profit_amount` AS `profit_amount`,
    `tor`.`order_status` AS `order_status`,
    `tor`.`bill_status` AS `bill_status`,
    `tor`.`product_count` AS `product_count`,
    `tor`.`series_type` AS `series_type`,
    `tor`.`series_value` AS `series_value`,
    `tor`.`product_amount_total` AS `product_amount_total`,
    `tor`.`order_amount_total` AS `order_amount_total`,
    `tor`.`device_type` AS `device_type`,
    `tor`.`ip` AS `ip`,
    NULL AS `tenant_id`,
    `tor`.`currency_code` AS `currency_code`,
    `tor`.`device_imei` AS `device_imei`,
    `tor`.`max_win_amount` AS `max_win_amount`,
    `tu`.`fake_name` AS `username`,
    `tu`.`user_level` AS `user_level`,
    `tu`.`create_time` AS `user_create_time`,
    `tm`.`merchant_code` AS `merchant_code`,
    `tm`.`id` AS `merchant_id`,
    `tm`.`merchant_name` AS `merchant_name`,
    `tm`.`level` AS `level`,
    `tm`.`parent_id` AS `parent_id`,
    `tm`.`agent_level` AS `agent_level`,
    `tm`.`currency` AS `merchant_currency`
FROM
    ((((
        SELECT
            `tybss_merchant_common`.`t_settle`.`id` AS `id`,
            `tybss_merchant_common`.`t_settle`.`uid` AS `uid`,
            `tybss_merchant_common`.`t_settle`.`order_no` AS `order_no`,
            `tybss_merchant_common`.`t_settle`.`out_come` AS `out_come`,
            `tybss_merchant_common`.`t_settle`.`settle_amount` AS `settle_amount`,
            `tybss_merchant_common`.`t_settle`.`payout_status` AS `payout_status`,
            `tybss_merchant_common`.`t_settle`.`settle_type` AS `settle_type`,
            `tybss_merchant_common`.`t_settle`.`settle_time` AS `settle_time`,
            `tybss_merchant_common`.`t_settle`.`odd_finally` AS `odd_finally`,
            `tybss_merchant_common`.`t_settle`.`create_user` AS `create_user`,
            `tybss_merchant_common`.`t_settle`.`create_time` AS `create_time`,
            `tybss_merchant_common`.`t_settle`.`modify_user` AS `modify_user`,
            `tybss_merchant_common`.`t_settle`.`modify_time` AS `modify_time`,
            `tybss_merchant_common`.`t_settle`.`del_flag` AS `del_flag`,
            `tybss_merchant_common`.`t_settle`.`remark` AS `remark`,
            `tybss_merchant_common`.`t_settle`.`bet_amount` AS `bet_amount`,
            `tybss_merchant_common`.`t_settle`.`settle_score` AS `settle_score`,
            `tybss_merchant_common`.`t_settle`.`odds_value` AS `odds_value`,
            `tybss_merchant_common`.`t_settle`.`profit_amount` AS `profit_amount`,
            `tybss_merchant_common`.`t_settle`.`ip` AS `ip`,
            `tybss_merchant_common`.`t_settle`.`last_settle` AS `last_settle`,
            `tybss_merchant_common`.`t_settle`.`settle_times` AS `settle_times`
        FROM
            `tybss_merchant_common`.`t_settle`
        WHERE
            ((
                     `tybss_merchant_common`.`t_settle`.`last_settle` = 1
                 )
                AND ( `tybss_merchant_common`.`t_settle`.`bet_amount` > 0 ))) `ts`
        LEFT JOIN `tybss_merchant_common`.`t_order` `tor` ON ((
                `ts`.`order_no` = `tor`.`order_no`
            )))
        LEFT JOIN `tybss_merchant_common`.`t_user` `tu` ON ((
                `tu`.`uid` = `tor`.`uid`
            )))
        LEFT JOIN `tybss_merchant_common`.`t_merchant` `tm` ON ((
            `tm`.`merchant_code` = `tu`.`merchant_code`
        )))

