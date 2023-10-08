DROP VIEW IF EXISTS tybss_report.view_r_user_full_info;
CREATE VIEW tybss_report.`view_r_user_full_info` AS
select `tu`.`uid`                                                                   AS `uid`,
       `tu`.`fake_name`                                                              AS `username`,
       `tu`.`user_level`                                                            AS `user_level`,
       `tu`.`vip_level`                                                             AS `vip_level`,
       `tu`.`ip_address`                                                            AS `ip_address`,
       `tu`.`currency_code`                                                         AS `currency_code`,
       `tu`.`create_time`                                                           AS `user_create_time`,
       `tm`.`merchant_code`                                                         AS `merchant_code`,
       `tm`.`id`                                                                    AS `merchant_id`,
       `tm`.`merchant_name`                                                         AS `merchant_name`,
       `tm`.`level`                                                                 AS `level`,
       `tm`.`parent_id`                                                             AS `parent_id`,
       `tm`.`create_time`                                                           AS `merchant_create_time`,
       `tm`.`agent_level`                                                           AS `agent_level`,
       `tm`.`currency`                                                              AS `merchant_currency`,
       `tm`.`rate`                                                                  AS `rate`,
       NULL                                                                         AS `first_bet_date`,
       DATE_FORMAT(FROM_UNIXTIME((`tu`.`create_time` / 1000)), '%Y-%m-%d %H:%m:%s') AS sync_time
from (`tybss_merchant_common`.`t_user` `tu`
         left join `tybss_merchant_common`.`t_merchant` `tm`
                   on ((`tu`.`merchant_code` = `tm`.`merchant_code`)));