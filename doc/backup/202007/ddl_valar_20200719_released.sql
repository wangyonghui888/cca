
drop table if exists r_merchant_finance_month_utc8;
CREATE TABLE `r_merchant_finance_month_utc8`
(
    `id`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  NOT NULL COMMENT '表ID  账期-merchantCode',
    `finance_date`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  NOT NULL COMMENT '账期 年-月',
    `finance_time`            bigint(20)                                                    NOT NULL COMMENT '账单时间戳',
    `merchant_id`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  NOT NULL COMMENT '商户Id',
    `merchant_code`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  NOT NULL COMMENT '商户Code',
    `parent_id`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs           DEFAULT NULL COMMENT '上级商户id',
    `merchant_num`            int(11)                                                                DEFAULT '0' COMMENT '二级商户数量',
    `merchant_name`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户名称',
    `agent_level`             tinyint(2)                                                             DEFAULT NULL COMMENT '商户类型/代理级别(0,直营;1:渠道;2:二级代理)',
    `currency`                varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  NOT NULL DEFAULT '' COMMENT '注单币种，CNY:人民币 USD:美元 EUR:欧元 SGD:新元',
    `bill_amount`             decimal(32, 2)                                                         DEFAULT '0.00' COMMENT '账单金额：a*费率()+vip费+技术服务费',
    `order_payment_amount`    decimal(32, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '应缴费用：a*费率',
    `order_amount_total`      decimal(32, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '投注金额',
    `profit_amount`           decimal(32, 2)                                                         DEFAULT NULL COMMENT '盈利金额',
    `computing_standard`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '分成方式(1,投注;2盈利)',
    `terrace_rate`            double(10, 2)                                                          DEFAULT '0.00' COMMENT '标准费率%',
    `execute_rate`            double(10, 2)                                                          DEFAULT '0.00' COMMENT '执行费率%',
    `adjust_amount`           decimal(32, 2)                                                         DEFAULT '0.00' COMMENT '调整额,含正负',
    `vip_amount`              decimal(32, 2)                                                         DEFAULT NULL COMMENT 'VIP费用',
    `technique_amount`        decimal(32, 2)                                                         DEFAULT NULL COMMENT '技术费用',
    `create_user`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs           DEFAULT NULL COMMENT '创建用户',
    `create_time`             bigint(20)                                                             DEFAULT '0' COMMENT '创建时间',
    `modify_user`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs           DEFAULT NULL COMMENT '修改人',
    `modify_time`             bigint(20)                                                             DEFAULT NULL COMMENT '修改时间',
    `payment_cycle`           int(1)                                                                 DEFAULT '0' COMMENT '缴纳周期(1:月,2:季,3年)',
    `vip_payment_cycle`       tinyint(2)                                                             DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)',
    `technique_payment_cycle` tinyint(2)                                                             DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)',
    `updated_time`            bigint(20)                                                    NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
    `order_num`               int(11)                                                       NOT NULL DEFAULT '0' COMMENT '投注笔数',
    `adjust_cause`            varchar(256)                                                           DEFAULT NULL COMMENT '调整原因'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='财务-清算月报表'
  ROW_FORMAT = DYNAMIC;

--
-- Dumping data for table `r_merchant_finance_month`
--
--
-- Indexes for table `r_merchant_finance_month`
--
ALTER TABLE `r_merchant_finance_month_utc8`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `merchant_code_index` (`merchant_code`) USING BTREE COMMENT '商户编号索引',
    ADD KEY `merchant_name_index` (`merchant_name`) USING BTREE COMMENT '商户名称索引',
    ADD KEY `profit_amount_index` (`profit_amount`) USING BTREE COMMENT '盈利金额索引',
    ADD KEY `finance_date_index` (`finance_date`) USING BTREE COMMENT '账期索引',
    ADD KEY `idx_parent_id` (`parent_id`);
COMMIT;

drop table if exists r_merchant_finance_bill_month_utc8;

CREATE TABLE `r_merchant_finance_bill_month_utc8`
(
    `id`                 varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT 'ID  表ID  年-月-merchantCode-currency',
    `finance_id`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT 'finance_month表id',
    `currency`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '注单币种，CNY:人民币 USD:美元 EUR:欧元 SGD:新元',
    `bill_order_amount`  decimal(32, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '投注金额',
    `bill_order_num`     int(11)                                                      NOT NULL DEFAULT '0' COMMENT '投注笔数',
    `bill_profit_amount` decimal(32, 2)                                                        DEFAULT NULL COMMENT '盈利金额',
    `create_time`        bigint(20)                                                            DEFAULT '0' COMMENT '创建时间',
    `modify_time`        bigint(20)                                                            DEFAULT NULL COMMENT '修改时间',
    `updated_time`       bigint(20)                                                   NOT NULL COMMENT '根据此字段增量同步到elasticsearch'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='财务-清算电子账单表'
  ROW_FORMAT = DYNAMIC;

--
-- Dumping data for table `r_merchant_finance_bill_month`
--


--
-- Indexes for dumped tables
--

--
-- Indexes for table `r_merchant_finance_bill_month`
--
ALTER TABLE `r_merchant_finance_bill_month_utc8`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `finance_id_index` (`finance_id`) USING BTREE COMMENT '外键索引';
COMMIT;

