ALTER TABLE tybss_report.`r_merchant_order_day` DROP `live_users`;

ALTER TABLE tybss_report.`r_merchant_order_day` DROP `live_user_rate`;

ALTER TABLE tybss_report.`r_merchant_order_day` DROP `live_profit`;

ALTER TABLE tybss_report.`r_merchant_order_day` DROP `live_return`;

ALTER TABLE tybss_report.`r_merchant_order_day` DROP `live_return_rate`;

ALTER TABLE tybss_report.`r_merchant_order_day` DROP `live_bet_amount`;

ALTER TABLE tybss_report.`r_merchant_order_day` DROP `live_order_num`;
#################
ALTER TABLE tybss_report.`r_merchant_order_week` DROP `live_users`;

ALTER TABLE tybss_report.`r_merchant_order_week` DROP `live_user_rate`;

ALTER TABLE tybss_report.`r_merchant_order_week` DROP `live_profit`;

ALTER TABLE tybss_report.`r_merchant_order_week` DROP `live_return`;

ALTER TABLE tybss_report.`r_merchant_order_week` DROP `live_return_rate`;

ALTER TABLE tybss_report.`r_merchant_order_week` DROP `live_bet_amount`;

ALTER TABLE tybss_report.`r_merchant_order_week` DROP `live_order_num`;
#################
ALTER TABLE tybss_report.`r_merchant_order_month` DROP `live_users`;

ALTER TABLE tybss_report.`r_merchant_order_month` DROP `live_user_rate`;

ALTER TABLE tybss_report.`r_merchant_order_month` DROP `live_profit`;

ALTER TABLE tybss_report.`r_merchant_order_month` DROP `live_return`;

ALTER TABLE tybss_report.`r_merchant_order_month` DROP `live_return_rate`;

ALTER TABLE tybss_report.`r_merchant_order_month` DROP `live_bet_amount`;

ALTER TABLE tybss_report.`r_merchant_order_month` DROP `live_order_num`;

drop table tybss_report.r_merchant_order_year;

###################################################
ALTER TABLE tybss_report.`r_merchant_sport_order_day` DROP `live_users`;

ALTER TABLE tybss_report.`r_merchant_sport_order_day` DROP `live_user_rate`;

ALTER TABLE tybss_report.`r_merchant_sport_order_day` DROP `live_profit`;

ALTER TABLE tybss_report.`r_merchant_sport_order_day` DROP `live_return`;

ALTER TABLE tybss_report.`r_merchant_sport_order_day` DROP `live_return_rate`;

ALTER TABLE tybss_report.`r_merchant_sport_order_day` DROP `live_bet_amount`;

ALTER TABLE tybss_report.`r_merchant_sport_order_day` DROP `live_order_num`;
#################
ALTER TABLE tybss_report.`r_merchant_sport_order_week` DROP `live_users`;

ALTER TABLE tybss_report.`r_merchant_sport_order_week` DROP `live_user_rate`;

ALTER TABLE tybss_report.`r_merchant_sport_order_week` DROP `live_profit`;

ALTER TABLE tybss_report.`r_merchant_sport_order_week` DROP `live_return`;

ALTER TABLE tybss_report.`r_merchant_sport_order_week` DROP `live_return_rate`;

ALTER TABLE tybss_report.`r_merchant_sport_order_week` DROP `live_bet_amount`;

ALTER TABLE tybss_report.`r_merchant_sport_order_week` DROP `live_order_num`;

ALTER TABLE tybss_report.`r_merchant_sport_order_month` DROP `live_users`;

ALTER TABLE tybss_report.`r_merchant_sport_order_month` DROP `live_user_rate`;

ALTER TABLE tybss_report.`r_merchant_sport_order_month` DROP `live_profit`;

ALTER TABLE tybss_report.r_merchant_sport_order_month DROP `live_return`;

ALTER TABLE tybss_report.`r_merchant_sport_order_month` DROP `live_return_rate`;

ALTER TABLE tybss_report.`r_merchant_sport_order_month` DROP `live_bet_amount`;

ALTER TABLE tybss_report.`r_merchant_sport_order_month` DROP `live_order_num`;

drop table tybss_report.r_merchant_sport_order_year;


drop table if exists tybss_report.`r_user_order_day_utc8`;
CREATE TABLE tybss_report.`r_user_order_day_utc8` (
                                         `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
                                         `user_id` bigint(32) DEFAULT NULL,
                                         `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                         `merchant_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                         `merchant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                         `time` int(32) DEFAULT NULL COMMENT '格式20200110',
                                         `profit` decimal(16,2) DEFAULT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
                                         `return_amount` decimal(16,2) DEFAULT NULL COMMENT '返还金额',
                                         `profit_rate` decimal(16,4) DEFAULT NULL,
                                         `bet_amount` decimal(16,2) DEFAULT NULL COMMENT '投注金额',
                                         `valid_bet_amount` decimal(16,2) DEFAULT NULL COMMENT '有效投注金额',
                                         `bet_num` int(6) DEFAULT NULL COMMENT '投注笔数',
                                         `valid_tickets` int(6) DEFAULT NULL COMMENT '有效投注笔数',
                                         `settle_order_num` int(6) DEFAULT NULL COMMENT '已结算注单数',
                                         `settle_order_amount` decimal(16,2) DEFAULT NULL COMMENT '已结算注单总金额',
                                         `settle_profit` decimal(16,2) DEFAULT NULL COMMENT '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
                                         `settle_return` decimal(16,0) DEFAULT NULL,
                                         `settle_profit_rate` decimal(16,4) DEFAULT NULL,
                                         `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
                                         `year` int(11) DEFAULT NULL,
                                         `parent_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级编码',
                                         `parent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC;
ALTER TABLE tybss_report.`r_user_order_day_utc8`
    ADD PRIMARY KEY (`id`) USING BTREE;
COMMIT;

drop table if exists tybss_report.`r_user_order_month_utc8`;
CREATE TABLE tybss_report.`r_user_order_month_utc8` (
                                         `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
                                         `user_id` bigint(32) DEFAULT NULL,
                                         `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                         `merchant_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                         `merchant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                         `time` int(32) DEFAULT NULL COMMENT '格式20200110',
                                         `profit` decimal(16,2) DEFAULT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
                                         `return_amount` decimal(16,2) DEFAULT NULL COMMENT '返还金额',
                                         `profit_rate` decimal(16,4) DEFAULT NULL,
                                         `bet_amount` decimal(16,2) DEFAULT NULL COMMENT '投注金额',
                                         `valid_bet_amount` decimal(16,2) DEFAULT NULL COMMENT '有效投注金额',
                                         `bet_num` int(6) DEFAULT NULL COMMENT '投注笔数',
                                         `valid_tickets` int(6) DEFAULT NULL COMMENT '有效投注笔数',
                                         `settle_order_num` int(6) DEFAULT NULL COMMENT '已结算注单数',
                                         `settle_order_amount` decimal(16,2) DEFAULT NULL COMMENT '已结算注单总金额',
                                         `settle_profit` decimal(16,2) DEFAULT NULL COMMENT '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
                                         `settle_return` decimal(16,0) DEFAULT NULL,
                                         `settle_profit_rate` decimal(16,4) DEFAULT NULL,
                                         `active_days` int(4) DEFAULT NULL,
                                         `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
                                         `parent_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级编码',
                                         `parent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC;
ALTER TABLE tybss_report.`r_user_order_month_utc8`
    ADD PRIMARY KEY (`id`) USING BTREE;
COMMIT;


drop table if exists tybss_report.`r_merchant_order_day_utc8`;
CREATE TABLE tybss_report.`r_merchant_order_day_utc8` (
                                        `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
                                        `merchant_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户编号',
                                        `time` bigint(32) DEFAULT NULL COMMENT '格式:20210110',
                                        `merchant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户名称',
                                        `merchant_level` tinyint(2) DEFAULT NULL COMMENT '商户等级',
                                        `add_user` bigint(10) DEFAULT '0' COMMENT '新增用户数',
                                        `register_total_user_sum` int(16) DEFAULT NULL COMMENT '当前商户总用户数',
                                        `bet_user_rate` decimal(16,4) DEFAULT NULL,
                                        `profit` decimal(32,2) DEFAULT NULL COMMENT '总盈利金额，即指商户注单的毛盈利，暂不计算返水等',
                                        `profit_rate` decimal(16,4) DEFAULT NULL,
                                        `return_amount` decimal(32,2) DEFAULT NULL COMMENT '派彩金额',
                                        `return_rate` decimal(16,4) DEFAULT NULL,
                                        `bet_amount` decimal(32,2) DEFAULT NULL COMMENT '投注额',
                                        `valid_bet_amount` decimal(32,2) DEFAULT NULL COMMENT '有效投注额',
                                        `filed_bet_amount` decimal(32,2) DEFAULT NULL COMMENT '投注失败投注额',
                                        `total_tickets` int(10) DEFAULT NULL COMMENT '订单数',
                                        `valid_tickets` int(10) DEFAULT NULL COMMENT '有效订单数',
                                        `filed_tickets` int(10) DEFAULT NULL COMMENT '投注失败订单数',
                                        `bet_users` int(10) DEFAULT NULL COMMENT '投注用户数',
                                        `valid_bet_users` int(10) DEFAULT NULL COMMENT '有效投注用户数',
                                        `first_bet_user_sum` int(8) DEFAULT NULL COMMENT '首投用户数',
                                        `settle_user_rate` decimal(16,4) DEFAULT NULL,
                                        `settle_profit` decimal(32,2) DEFAULT NULL COMMENT '当天结算注单-当天结算注单派彩金额',
                                        `settle_return` decimal(32,2) DEFAULT NULL COMMENT '当天结算注单派彩金额',
                                        `settle_return_rate` decimal(16,4) DEFAULT NULL,
                                        `settle_bet_amount` decimal(32,2) DEFAULT NULL COMMENT '结算注单总额',
                                        `settle_order_num` int(16) DEFAULT NULL COMMENT '结算注单数',
                                        `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
                                        `year` int(11) DEFAULT NULL,
                                        `active_user` bigint(8) DEFAULT '0' COMMENT '活跃用户数',
                                        `parent_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级编码',
                                        `parent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称',
                                        `settle_users` int(16) DEFAULT NULL COMMENT '总结算玩家数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC;
ALTER TABLE tybss_report.`r_merchant_order_day_utc8`
    ADD PRIMARY KEY (`id`) USING BTREE;
COMMIT;

drop table if exists tybss_report.`r_merchant_order_month_utc8`;
CREATE TABLE tybss_report.`r_merchant_order_month_utc8` (
                                        `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
                                        `merchant_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户编号',
                                        `time` bigint(32) DEFAULT NULL COMMENT '格式:20210110',
                                        `merchant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户名称',
                                        `merchant_level` tinyint(2) DEFAULT NULL COMMENT '商户等级',
                                        `add_user` bigint(10) DEFAULT '0' COMMENT '新增用户数',
                                        `register_total_user_sum` int(16) DEFAULT NULL COMMENT '当前商户总用户数',
                                        `bet_user_rate` decimal(16,4) DEFAULT NULL,
                                        `profit` decimal(32,2) DEFAULT NULL COMMENT '总盈利金额，即指商户注单的毛盈利，暂不计算返水等',
                                        `profit_rate` decimal(16,4) DEFAULT NULL,
                                        `return_amount` decimal(32,2) DEFAULT NULL COMMENT '派彩金额',
                                        `return_rate` decimal(16,4) DEFAULT NULL,
                                        `bet_amount` decimal(32,2) DEFAULT NULL COMMENT '投注额',
                                        `valid_bet_amount` decimal(32,2) DEFAULT NULL COMMENT '有效投注额',
                                        `filed_bet_amount` decimal(32,2) DEFAULT NULL COMMENT '投注失败投注额',
                                        `total_tickets` int(10) DEFAULT NULL COMMENT '订单数',
                                        `valid_tickets` int(10) DEFAULT NULL COMMENT '有效订单数',
                                        `filed_tickets` int(10) DEFAULT NULL COMMENT '投注失败订单数',
                                        `bet_users` int(10) DEFAULT NULL COMMENT '投注用户数',
                                        `valid_bet_users` int(10) DEFAULT NULL COMMENT '有效投注用户数',
                                        `first_bet_user_sum` int(8) DEFAULT NULL COMMENT '首投用户数',
                                        `settle_user_rate` decimal(16,4) DEFAULT NULL,
                                        `settle_profit` decimal(32,2) DEFAULT NULL COMMENT '当天结算注单-当天结算注单派彩金额',
                                        `settle_return` decimal(32,2) DEFAULT NULL COMMENT '当天结算注单派彩金额',
                                        `settle_return_rate` decimal(16,4) DEFAULT NULL,
                                        `settle_bet_amount` decimal(32,2) DEFAULT NULL COMMENT '结算注单总额',
                                        `settle_order_num` int(16) DEFAULT NULL COMMENT '结算注单数',
                                        `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
                                        `parent_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级编码',
                                        `parent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称',
                                        `settle_users` int(16) DEFAULT NULL COMMENT '总结算玩家数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC;
ALTER TABLE tybss_report.`r_merchant_order_month_utc8`
    ADD PRIMARY KEY (`id`) USING BTREE;
COMMIT;


ALTER TABLE tybss_report.`r_merchant_order_day` ADD `valid_bet_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '有效投注额' AFTER `bet_amount`;
ALTER TABLE tybss_report.`r_merchant_order_day` ADD `filed_bet_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '失败投注额' AFTER `valid_bet_amount`;

ALTER TABLE tybss_report.`r_merchant_order_day` ADD `valid_tickets` INT(12) NULL DEFAULT NULL COMMENT '有效注单数' AFTER `order_sum`;
ALTER TABLE tybss_report.`r_merchant_order_day` ADD `filed_tickets` INT(12) NULL DEFAULT NULL COMMENT '失败注单数' AFTER `valid_tickets`;

ALTER TABLE tybss_report.`r_merchant_order_day` ADD `valid_bet_users` INT(8) NULL DEFAULT NULL COMMENT '有效投注用户数' AFTER `bet_user_sum`;

ALTER TABLE tybss_report.`r_merchant_order_month` ADD `valid_bet_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '有效投注额' AFTER `bet_amount`;
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `filed_bet_amount` DECIMAL(32,2) NULL DEFAULT NULL COMMENT '失败投注额' AFTER `valid_bet_amount`;

ALTER TABLE tybss_report.`r_merchant_order_month` ADD `valid_tickets` INT(12) NULL DEFAULT NULL COMMENT '有效注单数' AFTER `order_sum`;
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `filed_tickets` INT(12) NULL DEFAULT NULL COMMENT '失败注单数' AFTER `valid_tickets`;

ALTER TABLE tybss_report.`r_merchant_order_month` ADD `valid_bet_users` INT(8) NULL DEFAULT NULL COMMENT '有效投注用户数' AFTER `bet_user_sum`;


ALTER TABLE tybss_report.`r_user_order_day`
    ADD `return_amount` DECIMAL(16, 2) NULL COMMENT '返还金额' AFTER `profit`;

ALTER TABLE tybss_report.`r_user_sport_order_day`
    ADD `return_amount` DECIMAL(16, 2) NULL COMMENT '返还金额' AFTER `profit`;



ALTER TABLE tybss_report.`r_user_order_day` ADD `valid_bet_amount` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '有效投注额' AFTER `bet_amount`;

ALTER TABLE tybss_report.`r_user_order_day` ADD `valid_tickets` INT(6) NULL DEFAULT NULL COMMENT '有效注单数' AFTER `bet_num`;

ALTER TABLE tybss_report.`r_user_order_month` ADD `valid_bet_amount` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '有效投注额' AFTER `bet_amount`;

ALTER TABLE tybss_report.`r_user_order_month` ADD `valid_tickets` INT(6) NULL DEFAULT NULL COMMENT '有效注单数' AFTER `bet_num`;
ALTER TABLE tybss_report.`r_user_order_month` ADD `return_amount` DECIMAL(16, 2) NULL COMMENT '返还金额' AFTER `profit`;
