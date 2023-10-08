DROP VIEW IF EXISTS tybss_report.view_r_order_outright_match_detail;
CREATE  VIEW `tybss_report`.`view_r_order_outright_match_detail` AS
select uuid()                                                                        AS `id`,
       date_format(from_unixtime((`smi`.`match_begion_time` / 1000)), '%Y-%m')              AS `dt`,
       `smi`.`id`                                                                    AS `match_id`,
       `smi`.`sport_id`                                                              AS `sport_id`,
       `smi`.`tournament_id`                                                         AS `tournament_id`,
       `smi`.`third_outright_match_id`                                                        AS `third_match_id`,

       `smi`.`match_begion_time`                                                            AS `begin_time`,
       `smi`.`outright_manager_id`                                                       AS `match_manage_id`,

       `smi`.`outright_name_en`                                                             AS `outright_name_en`,

       `smi`.`outright_name_cn`                                                            AS `outright_name_cn`,

       `tod`.`id`                                                                    AS `bet_id`,
       `tod`.`bet_no`                                                                AS `bet_no`,
       `tod`.`order_no`                                                              AS `order_no`,
       `tod`.`play_options_id`                                                       AS `play_options_id`,
       `tod`.`sport_name`                                                            AS `sport_name`,
       `tod`.`play_id`                                                               AS `play_id`,
       `tod`.`play_name`                                                             AS `play_name`,
       `tod`.`match_name`                                                            AS `match_name`,
       `tod`.`match_type`                                                            AS `match_type`,
       `tod`.`bet_time`                                                              AS `bet_time`,
       `tod`.`market_type`                                                           AS `market_type`,
       `tod`.`market_value`                                                          AS `market_value`,
       `tod`.`match_info`                                                            AS `match_info`,
       `tod`.`bet_amount`                                                            AS `bet_amount`,
       `tod`.`market_type_finally`                                                   AS `market_type_finally`,
       `tod`.`odd_finally`                                                           AS `odd_finally`,
       `tod`.`accept_bet_odds`                                                       AS `accept_bet_odds`,
       `tod`.`max_win_amount`                                                        AS `max_win_amount`,
       `tod`.`bet_status`                                                            AS `bet_status`,
       `tod`.`score_benchmark`                                                       AS `score_benchmark`,
       `tod`.`play_options`                                                          AS `play_options`,
       `tod`.`match_process_id`                                                      AS `match_process_id`,
       `tod`.`remark`                                                                AS `remark`,
       `tod`.`bet_result`                                                            AS `bet_result`,
       `tod`.`play_option_name`                                                      AS `play_option_name`,
       `tod`.`bet_all_result`                                                        AS `bet_all_result`,
       `tod`.`settle_score`                                                          AS `settle_score`,
       `tod`.`pre_data_sourse`                                                       AS `pre_data_sourse`,
       `tod`.`live_data_sourse`                                                      AS `live_data_sourse`,
       `smo`.`odds_type`                                                             AS `odds_type`,
       `smo`.`addition1`                                                             AS `addition1`,
       `smo`.`addition2`                                                             AS `addition2`,
       `smo`.`addition3`                                                             AS `addition3`,
       `smo`.`addition4`                                                             AS `addition4`,
       `smo`.`addition5`                                                             AS `addition5`,
       `smo`.`addition6`                                                             AS `addition6`,
       `smo`.`target_side`                                                           AS `target_side`,
       `smo`.`pa_odds_value`                                                         AS `pa_odds_value`,
       `smo`.`odds_stall`                                                            AS `odds_stall`,
       `smo`.`odds_value`                                                            AS `odds_value`,
       `smo`.`odds_fields_template_id`                                               AS `odds_fields_template_id`,
       `smo`.`original_odds_value`                                                   AS `original_odds_value`,
       `smo`.`order_odds`                                                            AS `order_odds`,
       `smo`.`third_odds_field_source_id`                                            AS `third_odds_field_source_id`,
       `smo`.`odds_status`                                                           AS `odds_status`,
       `smo`.`original_market_value`                                                 AS `original_market_value`,
       `smo`.`update_time`                                                           AS `update_time`,
       `tu`.`uid`                                                                    AS `uid`,
       `tu`.`fake_name`                                                              AS `username`,
       `tu`.`user_level`                                                             AS `user_level`,
       `tu`.`ip_address`                                                             AS `ip_address`,
       `tu`.`currency_code`                                                          AS `currency_code`,
       `tu`.`merchant_code`                                                          AS `merchant_code`,
       `tm`.`id`                                                                     AS `merchant_id`,
       `tm`.`merchant_name`                                                          AS `merchant_name`,
       `tm`.`level`                                                                  AS `level`,
       `tm`.`parent_id`                                                              AS `parent_id`,
       `tm`.`create_time`                                                            AS `merchant_create_time`,
       `tm`.`agent_level`                                                            AS `agent_level`,
       `tm`.`rate`                                                                   AS `rate`,
       `tor`.`series_type`                                                           AS `series_type`,
       `tor`.`order_status`                                                          AS `order_status`,
       `tor`.`vip_level`                                                             AS `vip_level`,
       `tor`.`manager_code`                                                          AS `manager_code`,
       `tor`.`order_amount_total`                                                    AS `order_amount_total`,
       `smo`.`market_id`                                                             AS `market_id`,
       `tod`.`market_main`                                                           AS `market_main`,
       `tod`.`original_bet_amount`                                                   AS `original_bet_amount`,
       `tor`.`original_order_amount_total`                                           AS `original_order_amount_total`,
       `tor`.`original_product_amount_total`                                         AS `original_product_amount_total`,
       `tor`.`original_pre_bet_amount`                                               AS `original_pre_bet_amount`,
       `tor`.`original_max_win_amount`                                               AS `original_max_win_amount`,
       date_format(from_unixtime((`tor`.`create_time` / 1000)), '%Y-%m-%d %H:%m:%s') AS `sync_time`,
       date_format(from_unixtime((`tor`.`modify_time` / 1000)), '%Y-%m-%d %H:%m:%s') AS `modify_time`,
       `sl1`.`zs`                                                                    AS `tournament_name`
from (
      (
          (
              (
                  (
                      (
                          (
                              (
                                  (`tybss_merchant_common`.`t_order_detail` `tod`  join `tybss_merchant_common`.`s_outright_match_info` `smi` on
                                      ((`tod`.`match_id` = `smi`.`id`)))
                                      left join `tybss_merchant_common`.`t_order` `tor` on ((`tor`.`order_no` = `tod`.`order_no`)))
                                  left join `tybss_merchant_common`.`s_market_odds` `smo` on ((`tod`.`play_options_id` = `smo`.`id`)))
                              left join `tybss_merchant_common`.`t_user` `tu` on ((`tu`.`uid` = `tod`.`uid`)))
                          left join `tybss_merchant_common`.`t_merchant` `tm` on ((`tm`.`merchant_code` = `tu`.`merchant_code`)))
                      left join `tybss_merchant_common`.`s_betting_play` `sbt` on ((`sbt`.`id` = `tod`.`play_id`)))
                  left join `tybss_merchant_common`.`s_tournament` `st` on ((`st`.`id` = `tod`.`tournament_id`)))
              left join `tybss_merchant_common`.`s_language` `sl` on ((`sl`.`name_code` = `sbt`.`play_name_code`)))
         left join `tybss_merchant_common`.`s_language` `sl1` on ((`sl1`.`name_code` = `st`.`name_code`)));