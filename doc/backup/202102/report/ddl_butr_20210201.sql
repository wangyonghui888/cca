
CREATE TABLE `r_match_market_settle_day_utc8` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `match_id` bigint(20) DEFAULT '0' COMMENT '赛事编号',
  `play_id` int(8) DEFAULT '0' COMMENT '玩法ID',
  `market_id` bigint(50) DEFAULT '0' COMMENT '盘口id',
  `match_info` varchar(255) DEFAULT NULL,
  `match_status` bigint(50) DEFAULT '0' COMMENT '赛事状态',
  `play_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '玩法名称',
  `market_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '盘口值',
  `market_type` varchar(100) DEFAULT NULL,
  `match_type` bigint(50) DEFAULT NULL,
  `settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '已结算注单总金额',
  `settle_amount` decimal(32,0) DEFAULT NULL COMMENT '结算金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL COMMENT '盈利率',
  `settle_user_count` bigint(50) DEFAULT NULL COMMENT '已结算投注用户数',
  `settle_order_count` bigint(50) DEFAULT NULL COMMENT '已结算投注笔数',
  `beging_time` bigint(50) DEFAULT NULL COMMENT '开赛时间',
  `tournament_level` bigint(50) DEFAULT NULL COMMENT '联赛等级',
  `sport_id` bigint(50) DEFAULT NULL COMMENT '赛种',
  `pre_trader_id` varchar(255) DEFAULT NULL COMMENT '赛前操盘手ID',
  `live_trader_id` varchar(255) DEFAULT NULL COMMENT '滚球操盘手ID',
   `pre_risk_manager_code` varchar(10) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '赛前操盘平台 如： SR'',',
   `live_risk_manager_code` varchar(10) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '滚球操盘平台 如： SR、MTS',
  `tournament_id` varchar(255) DEFAULT NULL COMMENT '联赛ID',
  `time` bigint(50) DEFAULT NULL COMMENT '时间天',
  `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_match_market_time` (`time`) USING BTREE,
   KEY `index_match_market_updatedTime` (`updated_time`) USING BTREE,
   KEY `index_match_id` (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;




CREATE TABLE `r_match_settle_bet_info` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `match_id` bigint(20) DEFAULT '0' COMMENT '赛事编号',
  `match_info` varchar(255) DEFAULT NULL,
  `match_status` bigint(50) DEFAULT NULL COMMENT '赛事状态',
  `settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '已结算注单总金额',
  `settle_amount` decimal(32,0) DEFAULT NULL COMMENT '结算金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL COMMENT '盈利率',
  `settle_user_count` bigint(50) DEFAULT NULL COMMENT '已结算投注用户数',
  `settle_order_count` bigint(50) DEFAULT NULL COMMENT '已结算投注笔数',
  `beging_time` bigint(50) DEFAULT NULL COMMENT '开赛时间',
  `tournament_level` bigint(50) DEFAULT NULL COMMENT '联赛等级',
   `pre_settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '早盘注单总金额',
   `pre_settle_amount` decimal(32,0) DEFAULT NULL COMMENT '早盘结算总金额',
   `pre_settle_profit` decimal(32,0) DEFAULT NULL COMMENT '早盘盈利总金额',
  `pre_order_count` bigint(50) DEFAULT NULL COMMENT '早盘总投注笔数',
   `live_settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '滚球注单总金额',
   `live_settle_amount` decimal(32,0) DEFAULT NULL COMMENT '滚球结算总金额',
   `live_settle_profit` decimal(32,0) DEFAULT NULL COMMENT '滚球盈利总金额',
  `live_order_count` bigint(50) DEFAULT NULL COMMENT '滚球总投注笔数',
  `sport_id` bigint(50) DEFAULT NULL COMMENT '赛种',
  `pre_trader_id` varchar(255) DEFAULT NULL COMMENT '赛前操盘手ID',
  `live_trader_id` varchar(255) DEFAULT NULL COMMENT '滚球操盘手ID',
   `pre_risk_manager_code` varchar(10) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '赛前操盘平台 如： SR'',',
   `live_risk_manager_code` varchar(10) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '滚球操盘平台 如： SR、MTS',
  `tournament_id` varchar(255) DEFAULT NULL COMMENT '联赛ID',
  `time` bigint(50) DEFAULT NULL COMMENT '时间天',
   `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_match_settle_time` (`time`) USING BTREE,
   KEY `index_match_settle_updatedTime` (`updated_time`) USING BTREE,
   KEY `index_match_settle_match_id` (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;




CREATE TABLE `r_tournament_settle_bet_info` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
   `tournament_id` varchar(255) DEFAULT NULL COMMENT '联赛ID',
  `settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '已结算注单总金额',
  `settle_amount` decimal(32,0) DEFAULT NULL COMMENT '结算金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL COMMENT '盈利率',
  `settle_user_count` bigint(50) DEFAULT NULL COMMENT '已结算投注用户数',
  `settle_order_count` bigint(50) DEFAULT NULL COMMENT '已结算投注笔数',
   `pre_settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '早盘注单总金额',
   `pre_settle_amount` decimal(32,0) DEFAULT NULL COMMENT '早盘结算总金额',
   `pre_settle_profit` decimal(32,0) DEFAULT NULL COMMENT '早盘盈利总金额',
  `pre_order_count` bigint(50) DEFAULT NULL COMMENT '早盘总投注笔数',
   `live_settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '滚球注单总金额',
   `live_settle_amount` decimal(32,0) DEFAULT NULL COMMENT '滚球结算总金额',
   `live_settle_profit` decimal(32,0) DEFAULT NULL COMMENT '滚球盈利总金额',
  `live_order_count` bigint(50) DEFAULT NULL COMMENT '滚球总投注笔数',
  `sport_id` bigint(50) DEFAULT NULL COMMENT '赛种',
  `time` bigint(50) DEFAULT NULL COMMENT '时间天',
   `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_tournament_settle_time` (`time`) USING BTREE,
   KEY `index_tournament_settle_updatedTime` (`updated_time`) USING BTREE,
   KEY `index_tournament_settle_tournament_id` (`tournament_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;


CREATE TABLE `r_match_settle_user_count` (
  `match_id` bigint(20) DEFAULT '0' COMMENT '赛事编号',
  `settle_user_count` bigint(50) DEFAULT NULL COMMENT '已结算投注用户数',
  PRIMARY KEY (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;


CREATE TABLE `r_match_play_settle_day_utc8` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `match_id` bigint(20) DEFAULT '0' COMMENT '赛事编号',
	`play_id` bigint(20) DEFAULT '0' COMMENT '玩法ID',
	 `play_name` varchar(255) DEFAULT NULL COMMENT '玩法名称',
  `match_info` varchar(255) DEFAULT NULL,
  `match_status` bigint(50) DEFAULT NULL COMMENT '赛事状态',
  `settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '已结算注单总金额',
  `settle_amount` decimal(32,0) DEFAULT NULL COMMENT '结算金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL COMMENT '盈利率',
  `settle_order_count` bigint(50) DEFAULT NULL COMMENT '已结算投注笔数',
  `beging_time` bigint(50) DEFAULT NULL COMMENT '开赛时间',
  `tournament_level` bigint(50) DEFAULT NULL COMMENT '联赛等级',
   `pre_settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '早盘注单总金额',
   `pre_settle_amount` decimal(32,0) DEFAULT NULL COMMENT '早盘结算总金额',
   `pre_settle_profit` decimal(32,0) DEFAULT NULL COMMENT '早盘盈利总金额',
  `pre_order_count` bigint(50) DEFAULT NULL COMMENT '早盘总投注笔数',
   `live_settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '滚球注单总金额',
   `live_settle_amount` decimal(32,0) DEFAULT NULL COMMENT '滚球结算总金额',
   `live_settle_profit` decimal(32,0) DEFAULT NULL COMMENT '滚球盈利总金额',
  `live_order_count` bigint(50) DEFAULT NULL COMMENT '滚球总投注笔数',
  `sport_id` bigint(50) DEFAULT NULL COMMENT '赛种',
  `pre_trader_id` varchar(255) DEFAULT NULL COMMENT '赛前操盘手ID',
  `live_trader_id` varchar(255) DEFAULT NULL COMMENT '滚球操盘手ID',
  `tournament_id` varchar(255) DEFAULT NULL COMMENT '联赛ID',
  `time` bigint(50) DEFAULT NULL COMMENT '时间天',
   `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_match_settle_time` (`time`) USING BTREE,
   KEY `index_match_settle_updatedTime` (`updated_time`) USING BTREE,
   KEY `index_match_settle_match_id` (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;



CREATE TABLE `r_match_bet_interval_day_utc8` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `match_id` bigint(20) DEFAULT '0' COMMENT '赛事编号',
	 `user_count` bigint(20) DEFAULT '0' COMMENT '玩法ID',
  `bet_interval` bigint(20) DEFAULT '0' COMMENT '投注区间',
  `settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '已结算注单总金额',
  `settle_amount` decimal(32,0) DEFAULT NULL COMMENT '结算金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL COMMENT '盈利率',
  `settle_order_count` bigint(50) DEFAULT NULL COMMENT '已结算投注笔数',
  `beging_time` bigint(50) DEFAULT NULL COMMENT '开赛时间',
  `tournament_id` varchar(255) DEFAULT NULL COMMENT '联赛ID',
    `sport_id` bigint(50) DEFAULT NULL COMMENT '赛种',
  `time` bigint(50) DEFAULT NULL COMMENT '时间天',
   `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_match_bet_interval_time` (`time`) USING BTREE,
   KEY `index_match_bet_interval` (`bet_interval`) USING BTREE,
	    KEY `index_match_bet_interval_updatedTime` (`updated_time`) USING BTREE,
   KEY `index_match_bet_interval_match_id` (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;




CREATE TABLE `r_match_user_settle_day_utc8` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `match_id` bigint(20) DEFAULT '0' COMMENT '赛事编号',
	 `uid` bigint(20) DEFAULT '0' COMMENT '用户ID',
	  `username` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `user_level` bigint(3) DEFAULT '0' COMMENT '用户标签ID',
  `settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '已结算注单总金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL COMMENT '盈利率',
  `settle_order_count` bigint(50) DEFAULT NULL COMMENT '已结算投注笔数',
  `beging_time` bigint(50) DEFAULT NULL COMMENT '开赛时间',
	`sport_id` bigint(20) DEFAULT NULL COMMENT '赛种ID',
  `tournament_id` varchar(255) DEFAULT NULL COMMENT '联赛ID',
	`time` bigint(50) DEFAULT NULL COMMENT '时间天',
   `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_match_user_settle_time` (`time`) USING BTREE,
   KEY `index_match_user_settle` (`uid`) USING BTREE,
	    KEY `index_match_user_settle_updatedTime` (`updated_time`) USING BTREE,
   KEY `index_match_user_settle_match_id` (`match_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;

CREATE TABLE `r_tournament_user_settle_day_utc8` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
    `tournament_id` varchar(255) DEFAULT NULL COMMENT '联赛ID',
	 `uid` bigint(20) DEFAULT '0' COMMENT '用户ID',
	  `username` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `user_level` bigint(3) DEFAULT '0' COMMENT '用户标签ID',
  `settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '已结算注单总金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL COMMENT '盈利率',
  `settle_order_count` bigint(50) DEFAULT NULL COMMENT '已结算投注笔数',
	`sport_id` bigint(20) DEFAULT NULL COMMENT '赛种ID',
	`time` bigint(50) DEFAULT NULL COMMENT '时间天',
   `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_tournament_user_settle_time` (`time`) USING BTREE,
   KEY `index_tournament_user_settle` (`uid`) USING BTREE,
	    KEY `index_tournament_user_settle_updatedTime` (`updated_time`) USING BTREE,
   KEY `index_tournament_user_settle_tournament_id` (`tournament_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;



CREATE TABLE `r_sport_duty_trader_day` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
    `trader_id` bigint(20) NOT NULL  COMMENT 'trader_id',
	`shift` int(2) DEFAULT NULL COMMENT '班次 1早班:2:中班3:晚班4:晚班(1)5:晚班(2)',
  `sport_id` bigint(20) DEFAULT NULL COMMENT '体育种类',
  `user_code` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '操盘手code',
  `user_id` int(10) DEFAULT NULL COMMENT '操盘手id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 `time` bigint(50) DEFAULT NULL COMMENT '时间天',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_sport_duty_trader_time` (`time`) USING BTREE,
	KEY `index_sport_duty_trader_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;



CREATE TABLE `r_rist_duty_trader_day` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
    `trader_id` bigint(20) NOT NULL  COMMENT 'trader_id',
	`shift` int(2) DEFAULT NULL COMMENT '班次 1早班:2:中班3:晚班4:晚班(1)5:晚班(2)',
  `sport_id` bigint(20) DEFAULT NULL COMMENT '体育种类',
  `user_code` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '操盘手code',
  `settle_bet_amount` decimal(32,0) DEFAULT NULL COMMENT '已结算注单总金额',
  `profit` decimal(32,2) DEFAULT NULL COMMENT '盈利金额',
  `profit_rate` decimal(32,4) DEFAULT NULL COMMENT '盈利率',
  `settle_order_count` bigint(50) DEFAULT NULL COMMENT '已结算投注笔数',
	`all_count` bigint(20) DEFAULT NULL COMMENT '总场次',
	`mts_count` bigint(20) DEFAULT NULL COMMENT 'MTS场次',
	`pa_count` bigint(20)  DEFAULT NULL COMMENT '场次',
	`bet_avg`   decimal(32,4) DEFAULT NULL COMMENT '场均货量',
	`profit_ave` decimal(32,4) DEFAULT NULL COMMENT '场均盈利',
	`time` bigint(50) DEFAULT NULL COMMENT '时间天',
   `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_rist_duty_trader_time` (`time`) USING BTREE,
   KEY `index_rist_duty_trader` (`trader_id`) USING BTREE,
   KEY `index_rist_duty_trader_shift` (`shift`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;

CREATE TABLE `r_device_type_day` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
    `device_type` bigint(20) NOT NULL  COMMENT 'device_type',
	`order_count` bigint(20) DEFAULT NULL COMMENT '投注数',
	`time` bigint(50) DEFAULT NULL COMMENT '时间天',
   `year` bigint(50) DEFAULT NULL COMMENT '年份',
  `mouth` bigint(50) DEFAULT NULL COMMENT '时间月',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_device_type_day_time` (`time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
