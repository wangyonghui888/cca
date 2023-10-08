ALTER  TABLE  tybss_report.r_merchant_match_bet_info  ADD
      INDEX `index_time_code_id` (`begin_time`,`merchant_code`,`match_id`)  USING BTREE COMMENT '查询优化索引';
ALTER  TABLE  tybss_report.r_merchant_match_bet_info_group  ADD
      INDEX `index_time_code_id` (`begin_time`,`order_currency_code`,`merchant_code`,`match_id`)  USING BTREE COMMENT '查询优化索引';

