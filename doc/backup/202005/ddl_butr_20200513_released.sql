ALTER TABLE tybss_report.r_merchant_order_day  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_merchant_order_day  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_merchant_order_month  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_merchant_order_month  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_merchant_order_week  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_merchant_order_week  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_merchant_order_year  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_merchant_order_year  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_merchant_sport_order_day  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_merchant_sport_order_day  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_merchant_sport_order_month  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_merchant_sport_order_month  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_merchant_sport_order_week  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_merchant_sport_order_week  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_merchant_sport_order_year  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_merchant_sport_order_year  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';


ALTER TABLE tybss_report.r_user_order_day  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_user_order_day  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_user_order_month  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_user_order_month  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_user_order_week  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_user_order_week  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_user_order_year  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_user_order_year  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_user_sport_order_day  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_user_sport_order_day  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_user_sport_order_month  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_user_sport_order_month  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_user_sport_order_week  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_user_sport_order_week  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';
ALTER TABLE tybss_report.r_user_sport_order_year  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE tybss_report.r_user_sport_order_year  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';

ALTER TABLE merchant.user_order_all  ADD parent_code varchar(32)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs    COMMENT '商户父级编码';
ALTER TABLE merchant.user_order_all  ADD parent_name varchar(100)  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs   COMMENT '商户父级名称';


CREATE TABLE tybss_report.`r_merchant_match_bet_info` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `match_id` bigint(20) DEFAULT NULL COMMENT '赛事ID',
  `match_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '赛事对阵',
  `sport_id` int(5) DEFAULT '0' COMMENT '运动种类编号',
  `sport_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '运动种类名称',
  `begin_time` bigint(20) DEFAULT NULL COMMENT '开赛时间',
  `match_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '比赛状态',
  `bet_amount` decimal(32,2) DEFAULT NULL COMMENT '注单总金额',
  `valid_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '有效投注额',
  `settle_amount` decimal(32,2) DEFAULT NULL COMMENT '派彩金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `tournament_id` bigint(20) DEFAULT NULL,
  `user_amount` int(2) DEFAULT NULL COMMENT '投注用户数',
  `order_amount` bigint(50) DEFAULT NULL COMMENT '投注笔数',
  `profit_rate` decimal(32,4) DEFAULT NULL,
  `play_amount` bigint(50) DEFAULT '0' COMMENT '玩法数量',
  `elasticsearch_id` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  `tournament_name` varchar(200) DEFAULT NULL COMMENT '联赛名称',
  merchant_code varchar(32) DEFAULT NULL COMMENT '商户编码',
  merchant_name varchar(100) DEFAULT NULL COMMENT '商户名称'
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='报表-商户赛事投注统计信息';


CREATE TABLE `r_merchant_market_bet_info` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `match_id` bigint(20) DEFAULT '0' COMMENT '赛事编号',
  `play_id` int(8) DEFAULT '0' COMMENT '玩法ID',
  `market_id` bigint(50) DEFAULT '0' COMMENT '盘口id',
  `match_info` varchar(255) DEFAULT NULL,
  `play_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '玩法名称',
  `market_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '盘口值',
  `market_type` varchar(100) DEFAULT NULL,
  `bet_amount` decimal(32,0) DEFAULT NULL,
  `order_amount_total` decimal(32,0) DEFAULT NULL,
  `valid_bet_amount` decimal(32,2) DEFAULT NULL COMMENT '注单有效总金额',
  `settle_amount` decimal(32,0) DEFAULT NULL,
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL,
  `user_amount` bigint(50) DEFAULT NULL,
  `order_amount` bigint(50) DEFAULT NULL COMMENT '投注笔数',
  `elasticsearch_id` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  merchant_code varchar(32) DEFAULT NULL COMMENT '商户编码',
  merchant_name varchar(100) DEFAULT NULL COMMENT '商户名称'
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='报表-商户盘口投注统计信息';