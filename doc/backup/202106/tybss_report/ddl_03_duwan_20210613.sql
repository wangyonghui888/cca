drop table if exists tybss_report.r_merchant_match_bet_info_group;
CREATE TABLE `tybss_report`.`r_merchant_match_bet_info_group` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `match_id` bigint(20) DEFAULT NULL COMMENT '赛事ID',
  `order_currency_code` varchar(50)  DEFAULT NULL COMMENT '1人民币 2美元 3港币 4越南盾 5新加坡币 6英镑 7欧元 8比特币	',
  `match_type` int(4)  DEFAULT 1 COMMENT '1 常规赛事 2电竞赛事',
  `match_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '赛事对阵',
  `sport_id` int(5) DEFAULT '0' COMMENT '运动种类编号',
  `sport_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '运动种类名称',
  `begin_time` bigint(20) DEFAULT NULL COMMENT '开赛时间',
  `match_status` varchar(3) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '比赛状态',
  `bet_amount` decimal(32,2) DEFAULT NULL COMMENT '注单总金额',
  `valid_bet_amount` decimal(32,2) DEFAULT NULL,
  `settle_amount` decimal(32,2) DEFAULT NULL COMMENT '派彩金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `tournament_id` bigint(20) DEFAULT NULL,
  `user_amount` int(2) DEFAULT NULL COMMENT '投注用户数',
  `order_amount` bigint(50) DEFAULT NULL COMMENT '投注笔数',
  `profit_rate` decimal(32,4) DEFAULT NULL,
  `play_amount` bigint(50) DEFAULT '0' COMMENT '玩法数量',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  `tournament_name` varchar(200) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '联赛名称',
  `merchant_code` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户编码',
  `merchant_name` varchar(100) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户名称',
  `tournament_level` int(5) DEFAULT NULL,
  `un_settle_order` bigint(50) DEFAULT NULL,
  `un_settle_amount` decimal(32,2) DEFAULT NULL,
  `agent_level` tinyint(2) DEFAULT NULL,
  `parlay_vaild_bet_amount` decimal(32,4) DEFAULT NULL COMMENT '串关有效投注金额',
  `parlay_valid_tickets` int(11) DEFAULT NULL COMMENT '串关有效投注订单数',
  `parlay_profit` decimal(32,4) DEFAULT NULL COMMENT '串关盈利金额',
  `parlay_profit_rate` decimal(32,4) DEFAULT NULL COMMENT '串关盈利率',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_update_time` (`updated_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='报表-商户赛事投注统计信息按币种分组' ;