ALTER TABLE tybss_report.`r_user_order_day` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_user_order_month` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_user_order_week` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';



ALTER TABLE tybss_report.`r_user_sport_order_day` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_user_sport_order_month` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_user_sport_order_week` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';


ALTER TABLE tybss_report.`r_merchant_sport_order_day` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_sport_order_month` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_sport_order_week` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';



ALTER TABLE tybss_report.`r_merchant_order_day` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_order_month` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_order_week` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

#####################
ALTER TABLE tybss_report.`r_market_bet_info` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_match_bet_info` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_market_bet_info` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_match_bet_info` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_finance_day` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_finance_month` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';

ALTER TABLE tybss_report.`r_merchant_finance_bill_month` CHANGE `elasticsearch_id` `updated_time` BIGINT(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch';