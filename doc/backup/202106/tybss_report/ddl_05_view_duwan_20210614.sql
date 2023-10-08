DROP VIEW IF EXISTS tybss_report.view_r_settle_full_detail_group;
CREATE VIEW tybss_report.`view_r_settle_full_detail_group` AS
select date_format(from_unixtime((`ts`.`settle_time` / 1000)), '%Y-%m') AS `dt`,
       `ts`.`uid`                                                       AS `uid`,
       `ts`.`order_no`                                                  AS `order_no`,
       `ts`.`out_come`                                                  AS `out_come`,
       `ts`.`original_settle_amount`                                             AS `settle_amount`,
       `ts`.`settle_type`                                               AS `settle_type`,
       `ts`.`settle_time`                                               AS `settle_time`,
       `ts`.`odd_finally`                                               AS `odd_finally`,
       `ts`.`create_user`                                               AS `create_user`,
       `ts`.`create_time`                                               AS `create_time`,
       `ts`.`modify_user`                                               AS `modify_user`,
       `ts`.`modify_time`                                               AS `modify_time`,
       `ts`.`original_bet_amount`                                                AS `bet_amount`,
       `ts`.`odds_value`                                                AS `odds_value`,
       `ts`.`original_profit_amount`                                             AS `profit_amount`,
       `tor`.`order_status`                                             AS `order_status`,
       `tor`.`bill_status`                                              AS `bill_status`,
       `tor`.`product_count`                                            AS `product_count`,
       `tor`.`series_type`                                              AS `series_type`,
       `tor`.`series_value`                                             AS `series_value`,
       `tor`.`original_product_amount_total`                                     AS `product_amount_total`,
       `tor`.`original_order_amount_total`                                       AS `order_amount_total`,
       `tor`.`device_type`                                              AS `device_type`,
       `tor`.`ip`                                                       AS `ip`,
       NULL                                                             AS `tenant_id`,
       `tor`.`currency_code`                                            AS `currency_code`,
       `tor`.`device_imei`                                              AS `device_imei`,
       `tor`.`original_max_win_amount`                                           AS `max_win_amount`,
       `tu`.`fake_name`                                                 AS `username`,
       `tu`.`user_level`                                                AS `user_level`,
       `tu`.`create_time`                                               AS `user_create_time`,
       `tm`.`merchant_code`                                             AS `merchant_code`,
       `tm`.`id`                                                        AS `merchant_id`,
       `tm`.`merchant_name`                                             AS `merchant_name`,
       `tm`.`level`                                                     AS `level`,
       `tm`.`parent_id`                                                 AS `parent_id`,
       `tm`.`agent_level`                                               AS `agent_level`,
       `tm`.`currency`                                                  AS `merchant_currency`
from ((((select * from `tybss_new`.`t_settle` where last_settle = 1 and bet_amount > 0) `ts`
    left join `tybss_new`.`t_order` `tor`
    on ((`ts`.`order_no` = `tor`.`order_no`)))
    left join `tybss_new`.`t_user` `tu`
    on ((`tu`.`uid` = `tor`.`uid`)))
         left join `tybss_new`.`t_merchant` `tm`
                   on ((`tm`.`merchant_code` = `tu`.`merchant_code`)))