#tybss_new表执行---------------------------------------------------------------------------------------------------------
#订单表添加索引
ALTER  TABLE  tybss_new.t_order  ADD  INDEX `index_create_time` (`create_time`)  USING BTREE COMMENT '时间索引';
ALTER  TABLE  tybss_new.t_settle  ADD  INDEX `index_settle_time` (`settle_time`)  USING BTREE COMMENT '时间索引';

#删除老数据
DELETE FROM tybss_new.t_merchant WHERE id like CONCAT('%','a','%') or id like CONCAT('%','b','%') or id like CONCAT('%','c','%');

#以下sql在tybss_report执行------------------------------------------------------------------------------------------------

# 新增字段  因为新增字段过多 并且老字段多人维护 并且新需求需要重新生成数据 故采用改名备份当前表 新增新表
-- ALTER TABLE r_merchant_finance_day ADD  `platform_profit` decimal(32,2) DEFAULT '0.00' COMMENT '平台盈利';
-- ALTER TABLE r_merchant_finance_day ADD  `order_user_num` int(11) DEFAULT '0' COMMENT '投注用户数';
-- ALTER TABLE r_merchant_finance_day ADD  `platform_profit_rate` decimal(32,2) DEFAULT '0.00' COMMENT '平台盈利率 %';
--
-- ALTER TABLE r_merchant_finance_day ADD  `settle_order_amount_total` decimal(32,2) NOT NULL DEFAULT '0.00' COMMENT '结算-投注金额';
-- ALTER TABLE r_merchant_finance_day ADD  `settle_settle_amount` decimal(32,2) DEFAULT '0.00' COMMENT '结算-派彩金额';
-- ALTER TABLE r_merchant_finance_day ADD  `settle_order_num` bigint(20) DEFAULT '0' COMMENT '结算-投注笔数';
-- ALTER TABLE r_merchant_finance_day ADD  `settle_order_valid_num` int(11) DEFAULT '0' COMMENT '结算-有效注单数';
-- ALTER TABLE r_merchant_finance_day ADD  `settle_order_back_num` int(11) DEFAULT '0' COMMENT '结算-退回注单数';
-- ALTER TABLE r_merchant_finance_day ADD  `settle_transfer_num` int(11) DEFAULT '0' COMMENT '结算-帐变记录数';
-- ALTER TABLE r_merchant_finance_day ADD  `settle_platform_profit` decimal(32,2) DEFAULT '0.00' COMMENT '结算-平台盈利';
-- ALTER TABLE r_merchant_finance_day ADD  `settle_order_user_num` int(11) DEFAULT '0' COMMENT '结算-投注用户数';
-- ALTER TABLE r_merchant_finance_day ADD  `settle_platform_profit_rate` decimal(32,2) DEFAULT '0.00' COMMENT '结算-平台盈利率 %';
--
-- ALTER TABLE r_merchant_finance_day MODIFY `order_valid_num` int(11) DEFAULT '0' COMMENT '有效注单数';
-- ALTER TABLE r_merchant_finance_day MODIFY `order_back_num` int(11) DEFAULT '0' COMMENT '退回注单数';

########备份 财务-对账单日报表
alter table tybss_report.r_merchant_finance_day rename AS r_merchant_finance_day_backup_20200713;

########新增表  账务日-财务-对账单日报表
drop table if exists r_merchant_finance_day;
CREATE TABLE `r_merchant_finance_day` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '表ID  账期-merchantCode-currency',
  `finance_day_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '账期-merchantCode',
  `finance_date` varchar(32) COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '账期 年-月-日',
  `finance_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '账期时间戳',
  `merchant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户Id',
  `merchant_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户Code',
  `merchant_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户名称',
  `agent_level` tinyint(2) DEFAULT NULL COMMENT '商户类型/代理级别(0,直营;1:渠道;2:二级代理)',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '注单币种，CNY:人民币 USD:美元 EUR:欧元 SGD:新元',
  `order_amount_total` decimal(32,2) NOT NULL DEFAULT '0.00' COMMENT '投注金额',
  `settle_amount` decimal(32,2) DEFAULT '0.00' COMMENT '派彩金额',
  `order_num` bigint(20) DEFAULT '0' COMMENT '投注笔数',
  `order_valid_num` int(11) DEFAULT '0' COMMENT '有效注单数',
  `order_back_num` int(11) DEFAULT '0' COMMENT '退回注单数',
  `transfer_num` int(11) DEFAULT '0' COMMENT '帐变记录数',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `modify_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '上级商户id（月账单专用）',
  `merchant_num` int(11) DEFAULT '0' COMMENT '二级商户数量（月账单专用）',
  `profit_amount` decimal(32,2) DEFAULT NULL COMMENT '盈利金额（月账单专用）',
  `computing_standard` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '分成方式(1,投注;2盈利)（月账单专用）',
  `terrace_rate` double(10,2) DEFAULT '0.00' COMMENT '标准费率%（月账单专用）',
  `vip_amount` decimal(32,2) DEFAULT NULL COMMENT 'VIP费用（月账单专用）',
  `technique_amount` decimal(32,2) DEFAULT NULL COMMENT '技术费用（月账单专用）',
  `payment_cycle` int(1) DEFAULT '0' COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
  `vip_payment_cycle` tinyint(2) DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
  `technique_payment_cycle` tinyint(2) DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
  `execute_rate` double(10,2) DEFAULT '0.00' COMMENT '执行费率%',
  `platform_profit` decimal(32,2) DEFAULT '0.00' COMMENT '平台盈利',
  `order_user_num` int(11) DEFAULT '0' COMMENT '投注用户数',
  `platform_profit_rate` decimal(32,2) DEFAULT '0.00' COMMENT '平台盈利率 %',
  `settle_order_amount_total` decimal(32,2) NOT NULL DEFAULT '0.00' COMMENT '结算-投注金额',
  `settle_settle_amount` decimal(32,2) DEFAULT '0.00' COMMENT '结算-派彩金额',
  `settle_order_num` bigint(20) DEFAULT '0' COMMENT '结算-投注笔数',
  `settle_order_valid_num` int(11) DEFAULT '0' COMMENT '结算-有效注单数',
  `settle_order_back_num` int(11) DEFAULT '0' COMMENT '结算-退回注单数',
  `settle_transfer_num` int(11) DEFAULT '0' COMMENT '结算-帐变记录数',
  `settle_platform_profit` decimal(32,2) DEFAULT '0.00' COMMENT '结算-平台盈利',
  `settle_order_user_num` int(11) DEFAULT '0' COMMENT '结算-投注用户数',
  `settle_platform_profit_rate` decimal(32,2) DEFAULT '0.00' COMMENT '结算-平台盈利率 %',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `merchant_code_index` (`merchant_code`) USING BTREE COMMENT '商户编号索引',
  KEY `merchant_name_index` (`merchant_name`) USING BTREE COMMENT '商户名称索引',
  KEY `finance_date` (`finance_date`) USING BTREE COMMENT '账期索引',
  KEY `idx_finance_day_id` (`finance_day_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='财务-对账单日报表';

########新增表    自然日-财务-对账单日报表
drop table if exists r_merchant_finance_day_utc8;
CREATE TABLE `r_merchant_finance_day_utc8` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '表ID  账期-merchantCode-currency',
  `finance_day_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '账期-merchantCode',
  `finance_date` varchar(32) COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '账期 年-月-日',
  `finance_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '账期时间戳',
  `merchant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户Id',
  `merchant_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户Code',
  `merchant_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户名称',
  `agent_level` tinyint(2) DEFAULT NULL COMMENT '商户类型/代理级别(0,直营;1:渠道;2:二级代理)',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '注单币种，CNY:人民币 USD:美元 EUR:欧元 SGD:新元',
  `order_amount_total` decimal(32,2) NOT NULL DEFAULT '0.00' COMMENT '投注金额',
  `settle_amount` decimal(32,2) DEFAULT '0.00' COMMENT '派彩金额',
  `order_num` bigint(20) DEFAULT '0' COMMENT '投注笔数',
  `order_valid_num` int(11) DEFAULT '0' COMMENT '有效注单数',
  `order_back_num` int(11) DEFAULT '0' COMMENT '退回注单数',
  `transfer_num` int(11) DEFAULT '0' COMMENT '帐变记录数',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `modify_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `updated_time` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '上级商户id（月账单专用）',
  `merchant_num` int(11) DEFAULT '0' COMMENT '二级商户数量（月账单专用）',
  `profit_amount` decimal(32,2) DEFAULT NULL COMMENT '盈利金额（月账单专用）',
  `computing_standard` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '分成方式(1,投注;2盈利)（月账单专用）',
  `terrace_rate` double(10,2) DEFAULT '0.00' COMMENT '标准费率%（月账单专用）',
  `vip_amount` decimal(32,2) DEFAULT NULL COMMENT 'VIP费用（月账单专用）',
  `technique_amount` decimal(32,2) DEFAULT NULL COMMENT '技术费用（月账单专用）',
  `payment_cycle` int(1) DEFAULT '0' COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
  `vip_payment_cycle` tinyint(2) DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
  `technique_payment_cycle` tinyint(2) DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
  `execute_rate` double(10,2) DEFAULT '0.00' COMMENT '执行费率%',
  `platform_profit` decimal(32,2) DEFAULT '0.00' COMMENT '平台盈利',
  `order_user_num` int(11) DEFAULT '0' COMMENT '投注用户数',
  `platform_profit_rate` decimal(32,2) DEFAULT '0.00' COMMENT '平台盈利率 %',
  `settle_order_amount_total` decimal(32,2) NOT NULL DEFAULT '0.00' COMMENT '结算-投注金额',
  `settle_settle_amount` decimal(32,2) DEFAULT '0.00' COMMENT '结算-派彩金额',
  `settle_order_num` bigint(20) DEFAULT '0' COMMENT '结算-投注笔数',
  `settle_order_valid_num` int(11) DEFAULT '0' COMMENT '结算-有效注单数',
  `settle_order_back_num` int(11) DEFAULT '0' COMMENT '结算-退回注单数',
  `settle_transfer_num` int(11) DEFAULT '0' COMMENT '结算-帐变记录数',
  `settle_platform_profit` decimal(32,2) DEFAULT '0.00' COMMENT '结算-平台盈利',
  `settle_order_user_num` int(11) DEFAULT '0' COMMENT '结算-投注用户数',
  `settle_platform_profit_rate` decimal(32,2) DEFAULT '0.00' COMMENT '结算-平台盈利率 %',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `merchant_code_index` (`merchant_code`) USING BTREE COMMENT '商户编号索引',
  KEY `merchant_name_index` (`merchant_name`) USING BTREE COMMENT '商户名称索引',
  KEY `finance_date` (`finance_date`) USING BTREE COMMENT '账期索引',
  KEY `idx_finance_day_id` (`finance_day_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='财务-对账单日报表-自然日';

######## report库总 新增 货币汇率表
drop table if exists t_currency_rate;
CREATE TABLE `t_currency_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自动编号',
  `country_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '国家名称【中文】',
  `country_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '国家名称【英文】',
  `rate` double(15,4) NOT NULL COMMENT '汇率值,固定4位小数(以人民币为基准)',
  `currency_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '币种编码',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '创建用户',
  `modify_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '修改人',
  `modify_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_currency_code` (`currency_code`) USING BTREE COMMENT '币种编码索引'
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='货币汇率表(以美金为基准)';

INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (1, '人民币', '人民币', 1.0000, '1', 0, '0', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (2, '美元', '美元', 6.9874, '2', 0, '0', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (3, '港币', '港币', 0.9104, '3', 0, '0', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (4, '越南盾', '越南盾', 0.0003, '4', 0, '0', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (5, '新加坡币', '新加坡币', 4.9892, '5', 0, '0', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (6, '英镑', '英镑', 8.7900, '6', 0, '0', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (7, '欧元', '欧元', 7.7034, '7', 0, '0', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (8, '比特币', '比特币', 30000.0000, '8', 0, '0', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (9, '人民币', '人民币', 1.0000, 'RMB', 0, '非本人别删', '0', 0);
INSERT INTO `tybss_report`.`t_currency_rate`(`id`, `country_zh`, `country_cn`, `rate`, `currency_code`, `create_time`, `create_user`, `modify_user`, `modify_time`) VALUES (10, '人民币', '人民币', 1.0000, 'CNY', 0, '非本人别删', '0', 0);


######## 重新生成日报表存储过程
DROP PROCEDURE if EXISTS p_r_merchant_finance_day;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_merchant_finance_day(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 11;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, "错误码：", result_code, " p_r_merchant_finance_day错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

##r_merchant_finance_day 账务日表新增  此处不使用replace
    DELETE FROM tybss_report.r_merchant_finance_day WHERE finance_date = execute_date;
    INSERT INTO tybss_report.r_merchant_finance_day
    (id, finance_day_id, finance_date, finance_time, merchant_id, merchant_code, merchant_name, agent_level, currency,
    order_amount_total, settle_amount, order_num, order_valid_num, order_back_num, transfer_num, create_time, modify_time,
    updated_time, parent_id, merchant_num, profit_amount, computing_standard, terrace_rate, vip_amount, technique_amount,
    payment_cycle, vip_payment_cycle, technique_payment_cycle, execute_rate, platform_profit, order_user_num, platform_profit_rate,
    settle_order_amount_total, settle_settle_amount, settle_order_num, settle_order_valid_num, settle_order_back_num,
    settle_transfer_num, settle_platform_profit, settle_order_user_num, settle_platform_profit_rate)
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(z.currency_code, '1'))         as id,
           concat(execute_date, '-', m.merchant_code)                                            as finance_day_id,
           execute_date                                                                          as finance_date,
           unix_timestamp(date_sub(execute_date, interval 1 day)) * 1000 + (12 * 60 * 60 * 1000) as finance_time,
           m.id                                                                                  as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(z.currency_code, '1')                                                          as currency,
           IFNULL(z.order_amount_total, 0) / 100                                                 as order_amount_total,
           IFNULL(z.settle_amount, 0) / 100                                                      as settle_amount,
           IFNULL(z.order_num, 0)                                                                as order_num,
           IFNULL(z.order_valid_num, 0)                                                          as order_valid_num,
           IFNULL(z.order_back_num, 0)                                                           as order_back_num,
           IFNULL(x.transfer_num, 0)                                                             as transfer_num,
           unix_timestamp(now())                                                                 as create_time,
           unix_timestamp(now())                                                                 as modify_time,
           unix_timestamp(now())                                                                 as updated_time,
           m.parent_id                                                                           as parent_id,
           (SELECT count(id) FROM tybss_new.t_merchant WHERE status = 1 and parent_id = m.id)    as merchant_num,
           IFNULL(z.profit_amount, 0) / 100                                                      as profit_amount,
           m.computing_standard                                                               as computing_standard,
           m.terrace_rate                                                                     as terrace_rate,
           m.vip_amount                                                                       as vip_amount,
           m.technique_amount                                                                 as technique_amount,
           m.payment_cycle                                                                    as payment_cycle,
           m.vip_payment_cycle                                                                as vip_payment_cycle,
           m.technique_payment_cycle                                                          as technique_payment_cycle,
           m.execute_rate                                                                     as execute_rate,

           IFNULL(z.platform_profit, 0) / 100                                                   as platform_profit,
           IFNULL(z.order_user_num, 0)                                                          as order_user_num,
           z.platform_profit_rate                                                               as platform_profit_rate,
           IFNULL(z.settle_order_amount_total, 0)  / 100                                        as settle_order_amount_total,
           IFNULL(z.settle_settle_amount, 0) / 100                                              as settle_settle_amount,
           IFNULL(z.settle_order_num, 0)                                                        as settle_order_num,
           IFNULL(z.settle_order_valid_num, 0)                                                  as settle_order_valid_num,
           IFNULL(z.settle_order_back_num, 0)                                                   as settle_order_back_num,
           IFNULL(x.transfer_num, 0)                                                            as settle_transfer_num,
           IFNULL(z.settle_platform_profit, 0) / 100                                            as settle_platform_profit,
           IFNULL(z.settle_order_user_num, 0)                                                   as settle_order_user_num,
           z.settle_platform_profit_rate                                                        as settle_platform_profit_rate
    FROM (  SELECT
						tm.id,
						tm.agent_level,
                        tm.merchant_code,
                        tm.merchant_name,
                        tm.parent_id,
						IFNULL(tma.terrace_rate,0) as terrace_rate,
						IFNULL(tma.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
						IFNULL(tma.payment_cycle,'1') as payment_cycle,
						IFNULL(tma.vip_payment_cycle,'1') as vip_payment_cycle,
						IFNULL(tma.technique_payment_cycle,'1') as technique_payment_cycle,
						IFNULL(tma.computing_standard, '1') as computing_standard,
						0 as vip_amount,
						0 as technique_amount
						FROM tybss_new.t_merchant tm
						LEFT JOIN  tybss_new.t_merchant tma on tm.parent_id = tma.id
						LEFT JOIN tybss_new.t_merchant_level ml on tm.level = ml.level
						WHERE tm.agent_level = 2
						and tm.status = 1
					UNION
					SELECT
						tm.id,
						tm.agent_level,
                        tm.merchant_code,
                        tm.merchant_name,
                        tm.parent_id,
						IFNULL(tm.terrace_rate,0) as terrace_rate,
						IFNULL(tm.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
						IFNULL(tm.payment_cycle,'1') as payment_cycle,
						IFNULL(tm.vip_payment_cycle,'1') as vip_payment_cycle,
						IFNULL(tm.technique_payment_cycle,'1') as technique_payment_cycle,
						IFNULL(tm.computing_standard, '1') as computing_standard,
						case tm.vip_payment_cycle
                           when 1 then tm.vip_amount
                           when 2 then (case
                                            WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then tm.vip_amount
                                            else 0 end)
                           when 3 then (case WHEN month(CURRENT_DATE) = 1 then tm.vip_amount else 0 end)
                           else 0 end                                                                        as vip_amount,
                        case tm.technique_payment_cycle
                           when 1 then tm.technique_amount
                           when 2 then (case
                                            WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                then tm.technique_amount
                                            else 0 end)
                           when 3 then (case
                                            WHEN month(CURRENT_DATE) = 1 then tm.technique_amount
                                            else 0 end)
                           else 0 end                                                                        as technique_amount
						FROM tybss_new.t_merchant tm
						LEFT JOIN tybss_new.t_merchant_level ml on tm.level = ml.level
						WHERE tm.agent_level != 2
						and tm.status = 1
				) as m
             LEFT JOIN(SELECT u.merchant_code,
                              count(0) as transfer_num
                       FROM tybss_new.t_account_change_history ta
                                left join tybss_new.t_user u
                                          on u.uid = ta.uid
                       where ta.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                         and ta.create_time <=
                             unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                         and u.merchant_code is not null
                       GROUP BY u.merchant_code) as x on x.merchant_code = m.merchant_code
             LEFT JOIN (SELECT a.merchant_id,
                               a.currency_code,
                               a.order_amount_total,
                               a.settle_amount,
                               IFNULL(a.order_num, 0) as order_num,
                               a.order_valid_num,
                               a.order_back_num,
                               a.order_user_num,
                               a.platform_profit,
                               ROUND(IFNULL(a.platform_profit/a.order_amount_total, 0)*100,2)  as platform_profit_rate,
                               b.settle_order_amount_total,
                               b.settle_settle_amount,
                               IFNULL(b.settle_order_num, 0) as settle_order_num,
                               b.settle_order_valid_num,
                               b.settle_order_back_num,
                               b.settle_order_user_num,
                               b.settle_platform_profit,
                               ROUND(IFNULL(b.settle_platform_profit/b.settle_order_amount_total, 0)*100,2)  as settle_platform_profit_rate,
                               b.profit_amount
                         FROM (SELECT o.merchant_id,
                                     o.currency_code,
                                     SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END)      AS order_amount_total,
                                     SUM(s.settle_amount)                                                       as settle_amount,
                                     count(0)                                                                   as order_num,
                                     Count(CASE WHEN o.order_status in (0, 1) THEN 1 END)                       AS order_valid_num,
                                     Count(CASE WHEN o.order_status in (2, 4, 5) THEN 1 END)                    AS order_back_num,
                                     Count(distinct o.uid)                                                      AS order_user_num,
                                     SUM(s.profit_amount) * -1                                                  as platform_profit
                              FROM tybss_new.t_order o
                              LEFT JOIN tybss_new.t_settle s ON o.order_no = s.order_no AND s.last_settle = 1
                              WHERE o.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                                and o.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                              GROUP BY o.merchant_id, o.currency_code) a
                         LEFT JOIN(SELECT o.merchant_id,
                                     o.currency_code,
                                     SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END)      AS settle_order_amount_total,
                                     SUM(s.settle_amount)                                                       as settle_settle_amount,
                                     count(0)                                                                   as settle_order_num,
                                     Count(CASE WHEN o.order_status in (0, 1) THEN 1 END)                       AS settle_order_valid_num,
                                     Count(CASE WHEN o.order_status in (2, 4, 5) THEN 1 END)                    AS settle_order_back_num,
                                     Count(distinct o.uid)                                                      AS settle_order_user_num,
                                     SUM(s.profit_amount) * -1                                                  as settle_platform_profit,
                                     SUM(s.profit_amount) * -1                                                  as profit_amount
                                   FROM tybss_new.t_settle s
                                   LEFT JOIN tybss_new.t_order o on o.order_no = s.order_no
                                   where s.settle_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                                     and s.settle_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                                     and s.last_settle = 1
                                     and o.order_status = 1
                                   GROUP BY o.merchant_id, o.currency_code) b
                         on a.merchant_id = b.merchant_id and a.currency_code = b.currency_code) as z
             on z.merchant_id = m.id
    WHERE z.order_valid_num > 0 or z.settle_order_valid_num > 0;


##r_merchant_finance_day 自然日表新增 与账务日差别就是时间计算不一样
    DELETE FROM tybss_report.r_merchant_finance_day_utc8 WHERE finance_date = execute_date;
    INSERT INTO tybss_report.r_merchant_finance_day_utc8
    (id, finance_day_id, finance_date, finance_time, merchant_id, merchant_code, merchant_name, agent_level, currency,
    order_amount_total, settle_amount, order_num, order_valid_num, order_back_num, transfer_num, create_time, modify_time,
    updated_time, parent_id, merchant_num, profit_amount, computing_standard, terrace_rate, vip_amount, technique_amount,
    payment_cycle, vip_payment_cycle, technique_payment_cycle, execute_rate, platform_profit, order_user_num, platform_profit_rate,
    settle_order_amount_total, settle_settle_amount, settle_order_num, settle_order_valid_num, settle_order_back_num,
    settle_transfer_num, settle_platform_profit, settle_order_user_num, settle_platform_profit_rate)
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(z.currency_code, '1'))         as id,
           concat(execute_date, '-', m.merchant_code)                                            as finance_day_id,
           execute_date                                                                          as finance_date,
           unix_timestamp(date_sub(execute_date, interval 1 day)) * 1000 + (12 * 60 * 60 * 1000) as finance_time,
           m.id                                                                                  as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(z.currency_code, '1')                                                          as currency,
           IFNULL(z.order_amount_total, 0) / 100                                                 as order_amount_total,
           IFNULL(z.settle_amount, 0) / 100                                                      as settle_amount,
           IFNULL(z.order_num, 0)                                                                as order_num,
           IFNULL(z.order_valid_num, 0)                                                          as order_valid_num,
           IFNULL(z.order_back_num, 0)                                                           as order_back_num,
           IFNULL(x.transfer_num, 0)                                                             as transfer_num,
           unix_timestamp(now())                                                                 as create_time,
           unix_timestamp(now())                                                                 as modify_time,
           unix_timestamp(now())                                                                 as updated_time,
           m.parent_id                                                                           as parent_id,
           (SELECT count(id) FROM tybss_new.t_merchant WHERE status = 1 and parent_id = m.id)    as merchant_num,
           IFNULL(z.profit_amount, 0) / 100                                                      as profit_amount,
           m.computing_standard                                                               as computing_standard,
           m.terrace_rate                                                                     as terrace_rate,
           m.vip_amount                                                                       as vip_amount,
           m.technique_amount                                                                 as technique_amount,
           m.payment_cycle                                                                    as payment_cycle,
           m.vip_payment_cycle                                                                as vip_payment_cycle,
           m.technique_payment_cycle                                                          as technique_payment_cycle,
           m.execute_rate                                                                     as execute_rate,

           IFNULL(z.platform_profit, 0) / 100                                                   as platform_profit,
           IFNULL(z.order_user_num, 0)                                                          as order_user_num,
           z.platform_profit_rate                                                               as platform_profit_rate,
           IFNULL(z.settle_order_amount_total, 0)  / 100                                        as settle_order_amount_total,
           IFNULL(z.settle_settle_amount, 0) / 100                                              as settle_settle_amount,
           IFNULL(z.settle_order_num, 0)                                                        as settle_order_num,
           IFNULL(z.settle_order_valid_num, 0)                                                  as settle_order_valid_num,
           IFNULL(z.settle_order_back_num, 0)                                                   as settle_order_back_num,
           IFNULL(x.transfer_num, 0)                                                            as settle_transfer_num,
           IFNULL(z.settle_platform_profit, 0) / 100                                            as settle_platform_profit,
           IFNULL(z.settle_order_user_num, 0)                                                   as settle_order_user_num,
           z.settle_platform_profit_rate                                                        as settle_platform_profit_rate
    FROM (  SELECT
						tm.id,
						tm.agent_level,
                        tm.merchant_code,
                        tm.merchant_name,
                        tm.parent_id,
						IFNULL(tma.terrace_rate,0) as terrace_rate,
						IFNULL(tma.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
						IFNULL(tma.payment_cycle,'1') as payment_cycle,
						IFNULL(tma.vip_payment_cycle,'1') as vip_payment_cycle,
						IFNULL(tma.technique_payment_cycle,'1') as technique_payment_cycle,
						IFNULL(tma.computing_standard, '1') as computing_standard,
						0 as vip_amount,
						0 as technique_amount
						FROM tybss_new.t_merchant tm
						LEFT JOIN  tybss_new.t_merchant tma on tm.parent_id = tma.id
						LEFT JOIN tybss_new.t_merchant_level ml on tm.level = ml.level
						WHERE tm.agent_level = 2
						and tm.status = 1
					UNION
					SELECT
						tm.id,
						tm.agent_level,
                        tm.merchant_code,
                        tm.merchant_name,
                        tm.parent_id,
						IFNULL(tm.terrace_rate,0) as terrace_rate,
						IFNULL(tm.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
						IFNULL(tm.payment_cycle,'1') as payment_cycle,
						IFNULL(tm.vip_payment_cycle,'1') as vip_payment_cycle,
						IFNULL(tm.technique_payment_cycle,'1') as technique_payment_cycle,
						IFNULL(tm.computing_standard, '1') as computing_standard,
						case tm.vip_payment_cycle
                           when 1 then tm.vip_amount
                           when 2 then (case
                                            WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then tm.vip_amount
                                            else 0 end)
                           when 3 then (case WHEN month(CURRENT_DATE) = 1 then tm.vip_amount else 0 end)
                           else 0 end                                                                        as vip_amount,
                        case tm.technique_payment_cycle
                           when 1 then tm.technique_amount
                           when 2 then (case
                                            WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                then tm.technique_amount
                                            else 0 end)
                           when 3 then (case
                                            WHEN month(CURRENT_DATE) = 1 then tm.technique_amount
                                            else 0 end)
                           else 0 end                                                                        as technique_amount
						FROM tybss_new.t_merchant tm
						LEFT JOIN tybss_new.t_merchant_level ml on tm.level = ml.level
						WHERE tm.agent_level != 2
						and tm.status = 1
				) as m
             LEFT JOIN(SELECT u.merchant_code,
                              count(0) as transfer_num
                       FROM tybss_new.t_account_change_history ta
                                left join tybss_new.t_user u
                                          on u.uid = ta.uid
                       where ta.create_time >= unix_timestamp(execute_date) * 1000
                         and ta.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1
                         and u.merchant_code is not null
                       GROUP BY u.merchant_code) as x on x.merchant_code = m.merchant_code
             LEFT JOIN (SELECT a.merchant_id,
                               a.currency_code,
                               a.order_amount_total,
                               a.settle_amount,
                               IFNULL(a.order_num, 0) as order_num,
                               a.order_valid_num,
                               a.order_back_num,
                               a.order_user_num,
                               a.platform_profit,
                               ROUND(IFNULL(a.platform_profit/a.order_amount_total, 0)*100,2)  as platform_profit_rate,
                               b.settle_order_amount_total,
                               b.settle_settle_amount,
                               IFNULL(b.settle_order_num, 0) as settle_order_num,
                               b.settle_order_valid_num,
                               b.settle_order_back_num,
                               b.settle_order_user_num,
                               b.settle_platform_profit,
                               ROUND(IFNULL(b.settle_platform_profit/b.settle_order_amount_total, 0)*100,2)  as settle_platform_profit_rate,
                               b.profit_amount
                         FROM (SELECT o.merchant_id,
                                     o.currency_code,
                                     SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END)      AS order_amount_total,
                                     SUM(s.settle_amount)                                                       as settle_amount,
                                     count(0)                                                                   as order_num,
                                     Count(CASE WHEN o.order_status in (0, 1) THEN 1 END)                       AS order_valid_num,
                                     Count(CASE WHEN o.order_status in (2, 4, 5) THEN 1 END)                    AS order_back_num,
                                     Count(distinct o.uid)                                                      AS order_user_num,
                                     SUM(s.profit_amount) * -1                                                  as platform_profit
                              FROM tybss_new.t_order o
                              LEFT JOIN tybss_new.t_settle s ON o.order_no = s.order_no AND s.last_settle = 1
                              WHERE o.create_time >= unix_timestamp(execute_date) * 1000
                                and o.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1
                                and o.order_status in (0, 1)
                              GROUP BY o.merchant_id, o.currency_code) a
                         LEFT JOIN(SELECT o.merchant_id,
                                     o.currency_code,
                                     SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END)      AS settle_order_amount_total,
                                     SUM(s.settle_amount)                                                       as settle_settle_amount,
                                     count(0)                                                                   as settle_order_num,
                                     Count(CASE WHEN o.order_status in (0, 1) THEN 1 END)                       AS settle_order_valid_num,
                                     Count(CASE WHEN o.order_status in (2, 4, 5) THEN 1 END)                    AS settle_order_back_num,
                                     Count(distinct o.uid)                                                      AS settle_order_user_num,
                                     SUM(s.profit_amount) * -1                                                  as settle_platform_profit,
                                     SUM(s.profit_amount) * -1                                                  as profit_amount
                                   FROM tybss_new.t_settle s
                                   LEFT JOIN tybss_new.t_order o on o.order_no = s.order_no
                                   where s.settle_time >= unix_timestamp(execute_date) * 1000
                                     and s.settle_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1
                                     and s.last_settle = 1
                                     and o.order_status = 1
                                   GROUP BY o.merchant_id, o.currency_code) b
                         on a.merchant_id = b.merchant_id and a.currency_code = b.currency_code) as z
             on z.merchant_id = m.id
    WHERE z.order_valid_num > 0 or z.settle_order_valid_num > 0;


    /*sql结束*/
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_finance_day success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

/**
CALL p_r_merchant_finance_day('2020-05-01');
账务日：2020-05-01 12：00：00   到 2020-05-02 11：59：59
自然日：2020-05-01 00：00：00   到 2020-05-01 23：59：59
 */

######## 重新生成月报表存储过程
DROP PROCEDURE if EXISTS p_r_merchant_finance_month;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_merchant_finance_month(in str_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 12;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, "错误码：", result_code, " p_r_merchant_finance_month 错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');

    #多币种和去0的关系  不适用REPLACE
    DELETE FROM tybss_report.r_merchant_finance_month WHERE finance_date =  date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH), '%Y-%m');
    INSERT INTO tybss_report.r_merchant_finance_month
    SELECT concat(left(a.finance_date, 7), '-', a.merchant_code)                     as id,
           left(a.finance_date, 7)                                                   as finance_date,
           unix_timestamp(date_sub(date_sub(curdate(), interval day(curdate()) - 1 day),
                                   interval 1 month)) * 1000 + (12 * 60 * 60 * 1000) as finance_time,
           a.merchant_id                                                             as merchant_id,
           a.merchant_code                                                           as merchant_code,
           a.parent_id                                                               as parent_id,
           b.merchant_num                                                            as merchant_num,
           a.merchant_name                                                           as merchant_name,
           a.agent_level                                                             as agent_level,
           a.currency                                                                as currency,
           IFNULL(IF((case computing_standard
                          when 1 then b.order_amount_total * a.execute_rate / 100
                          when 2 then b.profit_amount * a.execute_rate / 100
                          else 0 end) < 0,
                     0,
                     (case computing_standard
                          when 1 then b.order_amount_total * a.execute_rate / 100
                          when 2 then b.profit_amount * a.execute_rate / 100
                          else 0 end)) +
                  case IFNULL(a.vip_payment_cycle, 1)
                      when 1 then IFNULL(a.vip_amount, 0)
                      when 2 then (case
                                       WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then IFNULL(a.vip_amount, 0)
                                       else 0 end)
                      when 3 then (case WHEN month(CURRENT_DATE) = 1 then IFNULL(a.vip_amount, 0) else 0 end)
                      else 0 end +
                  case IFNULL(a.technique_payment_cycle, 1)
                      when 1 then IFNULL(a.technique_amount, 0)
                      when 2 then (case
                                       WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then IFNULL(a.technique_amount, 0)
                                       else 0 end)
                      when 3 then (case WHEN month(CURRENT_DATE) = 1 then IFNULL(a.technique_amount, 0) else 0 end)
                      else 0 end, 0)
                                                                                     as bill_amount,
           IF((case computing_standard
                   when 1 then b.order_amount_total * a.execute_rate / 100
                   when 2 then b.profit_amount * a.execute_rate / 100
                   else 0 end) < 0,
              0,
              (case computing_standard
                   when 1 then b.order_amount_total * a.execute_rate / 100
                   when 2 then b.profit_amount * a.execute_rate / 100
                   else 0 end))
                                                                                     as order_payment_amount,
           b.order_amount_total                                                      as order_amount_total,
           b.profit_amount                                                           as profit_amount,
           a.computing_standard                                                      as computing_standard,
           a.terrace_rate                                                            as terrace_rate,
           a.execute_rate                                                            as execute_rate,
           0                                                                         as adjust_amount,
           IFNULL(a.vip_amount, 0)                                                   as vip_amount,
           IFNULL(a.technique_amount, 0)                                             as technique_amount,
           'admin'                                                                   as create_user,
           unix_timestamp(now())                                                     as create_time,
           'admin'                                                                   as modify_user,
           unix_timestamp(now())                                                     as modify_time,
           IFNULL(a.payment_cycle, 1)                                                as payment_cycle,
           IFNULL(a.vip_payment_cycle, 1)                                            as vip_payment_cycle,
           IFNULL(a.technique_payment_cycle, 1)                                      as technique_payment_cycle,
           unix_timestamp(date_sub(date_sub(curdate(), interval day(curdate()) - 1 day),
                                   interval 1 month)) * 1000 + (12 * 60 * 60 * 1000) as elasticsearch_id,
           b.order_num                                                               as order_num,
           ''                                                                        as adjust_cause
    FROM (SELECT * FROM tybss_report.r_merchant_finance_day
            WHERE
                id like CONCAT(date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH), '%Y-%m'),'%')
                and id in (
                  select MAX(id)
                  FROM tybss_report.r_merchant_finance_day
                  where id like CONCAT(date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH), '%Y-%m'),'%')
                  and currency = '1'
			    GROUP BY merchant_code)
         ) as a
             left JOIN
         (SELECT d.merchant_id                                                                                        as merchant_id,
                 d.merchant_num                                                                                       as merchant_num,
                 sum(d.order_amount_total * (SELECT rate
                                             FROM tybss_new.t_currency_rate
                                             WHERE currency_code = d.currency))                                       as order_amount_total,
                 sum(d.profit_amount * (SELECT rate
                                        FROM tybss_new.t_currency_rate
                                        WHERE currency_code = d.currency))                                            as profit_amount,
                 sum(order_num)                                                                                       as order_num
          FROM (
                   select mf.merchant_id                        as merchant_id,
                          (SELECT count(1) FROM (SELECT DISTINCT merchant_id FROM r_merchant_finance_day WHERE parent_id = mf.merchant_id) mfd)    as merchant_num,
                          mf.currency                           as currency,
                          IFNULL(sum(mf.order_amount_total), 0) as order_amount_total,
                          IFNULL(sum(mf.profit_amount), 0)      as profit_amount,
                          IFNULL(sum(mf.order_valid_num), 0)    as order_num
                   FROM tybss_report.r_merchant_finance_day mf
                   where mf.finance_date like CONCAT(date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH), '%Y-%m'),'%')
                   GROUP BY mf.merchant_id, mf.currency
               ) as d
          GROUP BY merchant_id
         ) as b
         on a.merchant_id = b.merchant_id;

    DELETE FROM tybss_report.r_merchant_finance_bill_month WHERE concat(left(id, 7)) =  date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH), '%Y-%m');
    INSERT into tybss_report.r_merchant_finance_bill_month
    SELECT concat(concat(left(max(finance_date), 7), '-', merchant_code), '-', currency) as id,
           concat(left(max(finance_date), 7), '-', merchant_code)                        as finance_id,
           currency                                                                      as currency,
           SUM(order_amount_total)                                                       as bill_order_amount,
           SUM(order_valid_num)                                                          as bill_order_num,
           SUM(profit_amount)                                                            as bill_profit_amount,
           unix_timestamp(now())                                                         as create_time,
           unix_timestamp(now())                                                         as modify_time,
           unix_timestamp(date_sub(date_sub(curdate(), interval day(curdate()) - 1 day), interval 1 month)) * 1000 +
           (12 * 60 * 60 * 1000)                                                         as elasticsearch_id
    FROM tybss_report.r_merchant_finance_day
    where finance_date like CONCAT(date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH), '%Y-%m'),'%')
    GROUP BY merchant_code, currency;

    /*sql结束*/
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_finance_month success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;


/**
CALL p_r_merchant_finance_month('2020-06-01');
CALL p_r_merchant_finance_month('2020-06-20');
都计算：2020-05月份的月数据
*/


########循环调用 日期类存储过程   start_date<=目标<=end_date
DROP PROCEDURE IF EXISTS p_r_merchant_loop_exc;

DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_merchant_loop_exc(in start_date varchar(100),end_date varchar(100),procedure_name varchar(100),type_num varchar(100))
BEGIN

DECLARE startDate date DEFAULT NOW();
DECLARE endDate date DEFAULT NOW();
set startDate = DATE_FORMAT(start_date,'%Y-%m-%d');
set endDate = DATE_FORMAT(end_date,'%Y-%m-%d');


#日统计
IF type_num = '1' THEN
    WHILE startDate<=endDate
    DO

    IF 'p_r_merchant_finance_day' = procedure_name THEN
    CALL p_r_merchant_finance_day(startDate);

    ELSEIF 'p_r_merchant_order_day' = procedure_name THEN
    CALL p_r_merchant_order_day(startDate);

    ELSEIF 'p_r_merchant_sport_order_day' = procedure_name THEN
    CALL p_r_merchant_sport_order_day(startDate);

    ELSEIF 'p_r_user_order_day' = procedure_name THEN
    CALL p_r_user_order_day(startDate);

    ELSEIF 'p_r_user_sport_order_day' = procedure_name THEN
    CALL p_r_user_sport_order_day(startDate);

    ELSEIF 'p_r_market_bet_info' = procedure_name THEN
    CALL p_r_market_bet_info(startDate);

    ELSEIF 'p_r_match_bet_info' = procedure_name THEN
    CALL p_r_match_bet_info(startDate);

    ELSEIF 'p_r_merchant_market_bet_info' = procedure_name THEN
    CALL p_r_merchant_market_bet_info(startDate);

    ELSEIF 'p_r_merchant_match_bet_info' = procedure_name THEN
    CALL p_r_merchant_match_bet_info(startDate);

    END IF;

    set startDate = DATE_ADD(startDate,INTERVAL 1 DAY);
    END WHILE;
END IF;


#周统计
IF type_num = '2' THEN
    WHILE DATE_FORMAT(date_add(startDate, INTERVAL -1 day), '%Y%U')<=DATE_FORMAT(date_add(endDate, INTERVAL -1 day), '%Y%U')
    DO

    IF 'p_r_merchant_order_week' = procedure_name THEN
    CALL p_r_merchant_order_week(startDate);

    ELSEIF 'p_r_merchant_sport_order_week' = procedure_name THEN
    CALL p_r_merchant_sport_order_week(startDate);

    ELSEIF 'p_r_user_order_week' = procedure_name THEN
    CALL p_r_user_order_week(startDate);

    ELSEIF 'p_r_user_sport_order_week' = procedure_name THEN
    CALL p_r_user_sport_order_week(startDate);

    END IF;

    set startDate = DATE_ADD(startDate,INTERVAL 1 WEEK);
    END WHILE;


END IF;


#月统计
IF type_num = '3' THEN
    WHILE DATE_FORMAT(startDate, '%Y-%m')<=DATE_FORMAT(endDate, '%Y-%m')
    DO

    IF 'p_r_merchant_finance_month' = procedure_name THEN
    CALL p_r_merchant_finance_month(startDate);

    ELSEIF 'p_r_merchant_order_month' = procedure_name THEN
    CALL p_r_merchant_order_month(startDate);

    ELSEIF 'p_r_merchant_sport_order_month' = procedure_name THEN
    CALL p_r_merchant_sport_order_month(startDate);

    ELSEIF 'p_r_user_order_month' = procedure_name THEN
    CALL p_r_user_order_month(startDate);

    ELSEIF 'p_r_user_sport_order_month' = procedure_name THEN
    CALL p_r_user_sport_order_month(startDate);

    END IF;

    set startDate = DATE_ADD(startDate,INTERVAL 1 MONTH);
    END WHILE;
END IF;

END//
DELIMITER ;



/**
执行对应的存储过程。如果需要可以加入对应的 IF语句
start_date: 开始时间（包含）
end_date: 截止时间（包含）
procedure_name: 存储过程名称
type_num:  1:日期类型   2:周类型  3：月类型

一下参考：
CALL  p_r_merchant_loop_exc('2020-05-01','2020-07-08','p_r_merchant_order_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-07-08','p_r_merchant_order_week','2');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-07-07','p_r_merchant_order_month','3');

CALL  p_r_merchant_loop_exc('2020-05-01','2020-07-07','p_r_merchant_finance_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-07-07','p_r_merchant_finance_month','3');

上线 end_date 最好使用日期函数：
DATE_FORMAT(now(),'%Y-%m-%d')
DATE_FORMAT(DATE_ADD(now(),INTERVAL -1 DAY),'%Y-%m-%d')
 */

######## 重新生成数据
CALL  p_r_merchant_loop_exc('2020-05-01',DATE_FORMAT(DATE_ADD(now(),INTERVAL -1 DAY),'%Y-%m-%d'),'p_r_merchant_finance_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01',DATE_FORMAT(DATE_ADD(now(),INTERVAL -1 DAY),'%Y-%m-%d'),'p_r_merchant_finance_month','3');
