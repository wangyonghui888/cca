
alter table oubao.t_order
    add column tag varchar(32) null COMMENT '订单状态改变来源';

alter table oubao.t_order
    add column settle_amount decimal(16,2) null COMMENT '派彩金额';


CREATE TABLE oubao.`t_order_message` (
                                   `id` bigint(20) NOT NULL COMMENT '自动编号',
                                   `merchant_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户代码',
                                   `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建用户',
                                   `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',

                                   `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '订单单号',
                                   `order_status` int(10) DEFAULT '0' COMMENT '订单状态(0:待处理,1:已处理,2:取消交易)',
                                   `series_type` int(2) DEFAULT NULL COMMENT '串关类型(1：单关(默认) 、2：双式投注,例如1/2  、3：三式投注,例如1/2/3   、4：N串1,例如4串1   、5：N串F,例如5串26 )',
                                   `series_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '串关值(单关(默认) 、双式投注,例如1/2  、三式投注,例如1/2/3   、N串1,例如4串1   、N串F,例如5串26 )',
                                   `amount_total` decimal(32,2) DEFAULT NULL COMMENT '注单总价',
                                   `profit_amount` decimal(32,2) DEFAULT NULL COMMENT '实际付款金额',
                                   `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
                                   `settle_time` bigint(20) DEFAULT NULL COMMENT '结算时间',

                                   `outcome` int(10) DEFAULT '0' COMMENT '赛果',

                                   `settle_amount` decimal(16,2) DEFAULT NULL COMMENT '派彩金额',
                                   `tag` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '订单状态改变来源',
                                   `data_create_time` bigint(20) DEFAULT NULL COMMENT '数据创建时间'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='订单表' ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `t_order`
--

-- Indexes for dumped tables
--

--
-- Indexes for table `t_order`
--
ALTER TABLE oubao.`t_order_message`
    ADD PRIMARY KEY (`id`) USING BTREE;


--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `t_order`
--
ALTER TABLE oubao.`t_order_message`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自动编号', AUTO_INCREMENT=11112277;
COMMIT;
#-------------------------------------------------

CREATE TABLE merchant.`t_order_message` (
                                   `id` bigint(20) NOT NULL COMMENT '自动编号',
                                   `merchant_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户代码',
                                   `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建用户',
                                   `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',

                                   `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '订单单号',
                                   `order_status` int(10) DEFAULT '0' COMMENT '订单状态(0:待处理,1:已处理,2:取消交易)',
                                   `series_type` int(2) DEFAULT NULL COMMENT '串关类型(1：单关(默认) 、2：双式投注,例如1/2  、3：三式投注,例如1/2/3   、4：N串1,例如4串1   、5：N串F,例如5串26 )',
                                   `series_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '串关值(单关(默认) 、双式投注,例如1/2  、三式投注,例如1/2/3   、N串1,例如4串1   、N串F,例如5串26 )',
                                   `amount_total` decimal(32,2) DEFAULT NULL COMMENT '注单总价',
                                   `profit_amount` decimal(32,2) DEFAULT NULL COMMENT '实际付款金额',
                                   `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
                                   `settle_time` bigint(20) DEFAULT NULL COMMENT '结算时间',

                                   `outcome` int(10) DEFAULT '0' COMMENT '赛果',

                                   `settle_amount` decimal(16,2) DEFAULT NULL COMMENT '派彩金额',
                                   `tag` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '订单状态改变来源',
                                   `data_create_time` bigint(20) DEFAULT NULL COMMENT '数据创建时间'

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='订单表' ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `t_order`
--

-- Indexes for dumped tables
--

--
-- Indexes for table `t_order`
--
ALTER TABLE merchant.`t_order_message`
    ADD PRIMARY KEY (`id`) USING BTREE;


--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `t_order`
--
ALTER TABLE merchant.`t_order_message`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自动编号', AUTO_INCREMENT=11112277;
COMMIT;


DROP TABLE IF EXISTS merchant.`t_order_settle`;

CREATE TABLE merchant.`t_order_settle` (
                                  `id` bigint(20) NOT NULL COMMENT '自动编号' primary key ,
                                  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '订单单号',
                                  `user_id` bigint(20)  NULL COMMENT '用户ID',
                                  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户名',
                                  `order_status` int(10) NOT NULL DEFAULT '0' COMMENT '订单状态(0:待处理,1:已处理,2:取消交易)',
                                  `series_type` int(2) DEFAULT NULL COMMENT '串关类型(1：单关(默认) 、2：双式投注,例如1/2  、3：三式投注,例如1/2/3   、4：N串1,例如4串1   、5：N串F,例如5串26 )',
                                  `series_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '串关值(单关(默认) 、双式投注,例如1/2  、三式投注,例如1/2/3   、N串1,例如4串1   、N串F,例如5串26 )',
                                  `amount_total` decimal(20,2) DEFAULT NULL COMMENT '实际付款金额',
                                  `settle_amount` decimal(20,2) DEFAULT NULL COMMENT '结算金额',
                                  `profit_amount` decimal(32,2)  NULL,
                                  `outcome` int(2) DEFAULT NULL COMMENT '结果',
                                  `create_time` bigint(20) NOT NULL COMMENT '投注时间',
                                  `settle_time` bigint(20) DEFAULT NULL COMMENT '结算时间',
                                  `insert_time` bigint(20) DEFAULT NULL COMMENT '插入时间',
                                  `merchant_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户代码',
                                  `tag` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '订单状态改变来源'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='订单表' ROW_FORMAT=DYNAMIC;
ALTER TABLE merchant.`t_order_settle`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自动编号', AUTO_INCREMENT=11112277;


alter table tybss_new.t_merchant add column balance_url VARCHAR(200) null COMMENT '查询余额URL';
