DROP TABLE if EXISTS tybss_report.user_order_all;

CREATE TABLE tybss_report.`user_order_all` (
                                  `user_id` bigint(32) NOT NULL,
                                  `last_update` date DEFAULT NULL,
                                  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                  `merchant_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                  `order_amount` int(11) DEFAULT '0' COMMENT '投注笔数',
                                  `valid_bet_amount` decimal(16,2) DEFAULT '0.00' COMMENT '有效投注额',
                                  `bet_amount` decimal(16,2) DEFAULT '0.00' COMMENT '投注金额',
                                  `profit` decimal(16,2) DEFAULT '0.00' COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
                                  `profit_rate` int(4) DEFAULT NULL COMMENT '百分数：盈亏/投注',
                                  `last_login` bigint(32) DEFAULT NULL COMMENT '最后登录时间',
                                  `last_bet` bigint(32) DEFAULT NULL COMMENT '最后投注时间',
                                  `merchant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户名称',
                                  `register_time` bigint(32) DEFAULT NULL COMMENT '注册时间',
                                  `order_limit_20per` int(11) DEFAULT NULL COMMENT '注单限额20%以内注单数量',
                                  `order_limit_20to50` int(11) DEFAULT NULL COMMENT '注单限额20%-50%注单数量',
                                  `order_limit_50to80` int(11) DEFAULT NULL COMMENT '注单限额50%-80%注单数量',
                                  `order_limit_80per` int(11) DEFAULT NULL COMMENT '注单限额80%以上注单数量',
                                  `profit_order_num` int(11) DEFAULT '0' COMMENT '盈利订单笔数',
                                  `profit_order_rate` tinyint(2) DEFAULT NULL COMMENT '盈利订单笔数/有效订单笔数',
                                  `achieve_amount_num` int(11) DEFAULT '0' COMMENT '满额订单笔数',
                                  `achieve_amount_rate` tinyint(2) DEFAULT NULL COMMENT '满额订单笔数/有效订单笔数',
                                  `refuse_order_num` int(11) DEFAULT '0' COMMENT '拒绝订单笔数',
                                  `refuse_order_amount` bigint(32) DEFAULT '0' COMMENT '拒绝订单金额',
                                  `cancel_order_num` int(11) DEFAULT '0' COMMENT '取消订单笔数',
                                  `cancel_order_amount` bigint(32) DEFAULT '0' COMMENT '取消订单金额',
                                  `all_num` int(11) DEFAULT '0',
                                  `greterThan2_num` int(11) DEFAULT '0' COMMENT '大于欧赔为2的订单数(包括等于)',
                                  `lessThan2_num` int(11) DEFAULT '0' COMMENT '小于欧赔为2的订单数',
                                  `soccer_num` int(11) DEFAULT '0' COMMENT '足球订单数',
                                  `basketball_num` int(11) DEFAULT '0' COMMENT '篮球订单数',
                                  `others_num` int(11) DEFAULT '0' COMMENT '其他体种订单数',
                                  `series_num` int(11) DEFAULT '0' COMMENT '串关订单数',
                                  `soccer_handicap_num` int(11) DEFAULT '0' COMMENT '足球让分数',
                                  `soccer_overunder_num` int(11) DEFAULT '0' COMMENT '足球大小订单数',
                                  `basketball_handicap_num` int(11) DEFAULT '0' COMMENT '篮球让分订单数',
                                  `basketball_overunder_num` int(11) DEFAULT '0' COMMENT '篮球大小订单数',
                                  `soccer_handicap_main` int(11) DEFAULT '0' COMMENT '足球让分主盘订单数',
                                  `soccer_handicap_second` int(11) DEFAULT '0' COMMENT '足球让分复盘订单数',
                                  `soccer_overunder_main` int(11) DEFAULT '0' COMMENT '足球大小主盘订单数',
                                  `soccer_overunder_second` int(11) DEFAULT '0' COMMENT '足球大小副盘订单数',
                                  `balance` bigint(32) DEFAULT '0' COMMENT '余额',
                                  `level_id` int(11) DEFAULT NULL COMMENT '用户标签',
                                  `settle_amount` decimal(16,5) DEFAULT '0.00000' COMMENT '返还金额',
                                  `valid_order_num` int(11) DEFAULT '0' COMMENT '有效订单数',
                                  `first_bet_date` date DEFAULT NULL,
                                  `parent_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级编码',
                                  `parent_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC;


--
ALTER TABLE tybss_report.`user_order_all`
    ADD PRIMARY KEY (`user_id`) USING BTREE;
COMMIT;
