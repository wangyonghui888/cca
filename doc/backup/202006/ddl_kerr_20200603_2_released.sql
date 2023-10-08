## 业务表 - tybss_report 库执行

drop table if exists r_merchant_finance_month;

CREATE TABLE `r_merchant_finance_month` (
                                            `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '表ID  账期-merchantCode',
                                            `finance_date` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '账期 年-月',
                                            `finance_time` bigint(20) NOT NULL COMMENT '账单时间戳',
                                            `merchant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户Id',
                                            `merchant_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户Code',
                                            `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '上级商户id',
                                            `merchant_num` int(11) DEFAULT '0' COMMENT '二级商户数量',
                                            `merchant_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户名称',
                                            `agent_level` tinyint(2) DEFAULT NULL COMMENT '商户类型/代理级别(0,直营;1:渠道;2:二级代理)',
                                            `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '注单币种，CNY:人民币 USD:美元 EUR:欧元 SGD:新元',
                                            `bill_amount` bigint(20) DEFAULT '0' COMMENT '账单金额：a*费率()+vip费+技术服务费',
                                            `order_payment_amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '应缴费用：a*费率',
                                            `order_amount_total` bigint(20) NOT NULL DEFAULT '0' COMMENT '投注金额',
                                            `profit_amount` bigint(20) DEFAULT NULL COMMENT '盈利金额',
                                            `computing_standard` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '分成方式(1,投注;2盈利)',
                                            `terrace_rate` double(10,2) DEFAULT '0.00' COMMENT '标准费率%',
                                            `execute_rate` double(10,2) DEFAULT '0.00' COMMENT '执行费率%',
                                            `adjust_amount` bigint(20) DEFAULT '0' COMMENT '调整额,含正负',
                                            `vip_amount` bigint(32) DEFAULT NULL COMMENT 'VIP费用',
                                            `technique_amount` bigint(32) DEFAULT NULL COMMENT '技术费用',
                                            `create_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建用户',
                                            `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
                                            `modify_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '修改人',
                                            `modify_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
                                            `payment_cycle` int(1) DEFAULT '0' COMMENT '缴纳周期(1:月,2:季,3年)',
                                            `vip_payment_cycle` tinyint(2) DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)',
                                            `technique_payment_cycle` tinyint(2) DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)',
                                            `elasticsearch_id` bigint(20) DEFAULT NULL COMMENT '根据此字段增量同步到elasticsearch',
                                            `order_num` int(11) NOT NULL DEFAULT '0' COMMENT '投注笔数',
                                            `adjust_cause` varchar(256) DEFAULT NULL COMMENT '调整原因'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='财务-清算月报表' ROW_FORMAT=DYNAMIC;


ALTER TABLE `r_merchant_finance_month`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `merchant_code_index` (`merchant_code`) USING BTREE COMMENT '商户编号索引',
    ADD KEY `merchant_name_index` (`merchant_name`) USING BTREE COMMENT '商户名称索引',
    ADD KEY `profit_amount_index` (`profit_amount`) USING BTREE COMMENT '盈利金额索引',
    ADD KEY `finance_date_index` (`finance_date`) USING BTREE COMMENT '账期索引';
COMMIT;


drop table if exists r_merchant_finance_day;

CREATE TABLE `r_merchant_finance_day` (
                                          `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '表ID  账期-merchantCode-currency',
                                          `finance_day_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '账期-merchantCode',
                                          `finance_date` varchar(32) NOT NULL COMMENT '账期 年-月-日',
                                          `finance_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '账期时间戳',
                                          `merchant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户Id',
                                          `merchant_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户Code',
                                          `merchant_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户名称',
                                          `agent_level` tinyint(2) DEFAULT NULL COMMENT '商户类型/代理级别(0,直营;1:渠道;2:二级代理)',
                                          `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '注单币种，CNY:人民币 USD:美元 EUR:欧元 SGD:新元',
                                          `order_amount_total` bigint(20) NOT NULL DEFAULT '0' COMMENT '投注金额',
                                          `settle_amount` bigint(60) DEFAULT '0' COMMENT '派彩金额，是x100过后的金额数',
                                          `order_num` bigint(20) DEFAULT '0' COMMENT '投注笔数',
                                          `order_valid_num` int(3) DEFAULT '0' COMMENT '有效注单数',
                                          `order_back_num` int(3) DEFAULT '0' COMMENT '退回注单数',
                                          `transfer_num` int(11) DEFAULT '0' COMMENT '帐变记录数',
                                          `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
                                          `modify_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
                                          `elasticsearch_id` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
                                          `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '上级商户id（月账单专用）',
                                          `merchant_num` int(11) DEFAULT '0' COMMENT '二级商户数量（月账单专用）',
                                          `profit_amount` bigint(20) DEFAULT NULL COMMENT '盈利金额（月账单专用）',
                                          `computing_standard` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '分成方式(1,投注;2盈利)（月账单专用）',
                                          `terrace_rate` double(10,2) DEFAULT '0.00' COMMENT '标准费率%（月账单专用）',
                                          `vip_amount` bigint(32) DEFAULT NULL COMMENT 'VIP费用（月账单专用）',
                                          `technique_amount` bigint(32) DEFAULT NULL COMMENT '技术费用（月账单专用）',
                                          `payment_cycle` int(1) DEFAULT '0' COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
                                          `vip_payment_cycle` tinyint(2) DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
                                          `technique_payment_cycle` tinyint(2) DEFAULT NULL COMMENT '缴纳周期(1:月,2:季,3年)（月账单专用）',
                                          `execute_rate` double(10,2) DEFAULT '0.00' COMMENT '执行费率%'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='财务-对账单日报表' ROW_FORMAT=DYNAMIC;


ALTER TABLE `r_merchant_finance_day`
    ADD PRIMARY KEY (`id`) USING BTREE,
    ADD KEY `merchant_code_index` (`merchant_code`) USING BTREE COMMENT '商户编号索引',
    ADD KEY `merchant_name_index` (`merchant_name`) USING BTREE COMMENT '商户名称索引',
    ADD KEY `finance_date` (`finance_date`) USING BTREE COMMENT '账期索引';
COMMIT;


drop table if exists res_all_task_event_log;
CREATE TABLE `res_all_task_event_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自动编号',
  `task_type` int(2) NOT NULL DEFAULT '0' COMMENT '任务类型：1：market_bet_info，2：match_bet_info，3：merchant_order_day，4：merchant_order_month，5：merchant_order_week，6：merchant_sport_order_day ，7：merchant_sport_order_month，8：merchant_sport_order_week，9：order_full_detail，10：order_match_detail，11：settle_full_detail，12：user_full_info，13：user_order_day，14：user_order_month 15：user_order_week，16：user_sport_order_day，17：user_sport_order_month，18：user_sport_order_week，19：rcs_order_statistic_date,20：日志本身删除5天之前日志',
  `start_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '任务执行开始时间',
  `end_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '任务执行结束时间',
  `result` int(2) NOT NULL DEFAULT '0' COMMENT '成功：1，失败：2',
  `exce_msg` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '异常信息，result为2时才有',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=75377 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='所有任务时间执行结果表';

##费率等信息 由json获取转换到字段获取
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

    REPLACE INTO tybss_report.r_merchant_finance_day
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
           IFNULL(z.total_order_amount, 0)                                                       as order_num,
           IFNULL(z.order_valid_num, 0)                                                          as order_valid_num,
           IFNULL(z.cancel_order_amount, 0)                                                      as order_back_num,
           IFNULL(x.transfer_num, 0)                                                             as transfer_num,
           unix_timestamp(now())                                                                 as create_time,
           unix_timestamp(now())                                                                 as modify_time,
           UNIX_TIMESTAMP(current_timestamp(3))                                                  as elasticsearch_id,
           m.parent_id                                                                           as parent_id,
           (SELECT count(id) FROM tybss_new.t_merchant WHERE status = 1 and parent_id = m.id)    as merchant_num,
           IFNULL(z.profit_amount, 0) / 100                                                      as profit_amount,
           IFNULL(m.computing_standard, '1')                                                     as computing_standard,
           IFNULL(ml.terrace_rate, 0)                                                            as terrace_rate,
           case m.vip_payment_cycle
               when 1 then m.vip_amount
               when 2 then (case
                                WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then m.vip_amount
                                else 0 end)
               when 3 then (case WHEN month(CURRENT_DATE) = 1 then m.vip_amount else 0 end)
               else 0 end                                                                        as vip_amount,
           case m.technique_payment_cycle
               when 1 then m.technique_amount
               when 2 then (case
                                WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                    then m.technique_amount
                                else 0 end)
               when 3 then (case
                                WHEN month(CURRENT_DATE) = 1 then m.technique_amount
                                else 0 end)
               else 0 end                                                                        as technique_amount,
           IFNULL(m.payment_cycle, '1')                                                          as payment_cycle,
           IFNULL(m.vip_payment_cycle, '1')                                                      as vip_payment_cycle,
           IFNULL(m.technique_payment_cycle, '1')                                                as technique_payment_cycle,
           IFNULL(m.terrace_rate, IFNULL(ml.terrace_rate, 0))                                    as execute_rate
    FROM tybss_new.t_merchant m
             LEFT JOIN tybss_new.t_merchant_level ml on m.level = ml.level
             LEFT JOIN
         (
             SELECT u.merchant_code,
                    count(0) as transfer_num
             FROM tybss_new.t_account_change_history ta
                      left join tybss_new.t_user u
                                on u.uid = ta.uid
             where ta.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
               and ta.create_time <=
                   unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
               and u.merchant_code is not null
             GROUP BY u.merchant_code
         ) as x on x.merchant_code = m.merchant_code
             LEFT JOIN (
        SELECT a.merchant_id,
               a.currency_code,
               a.total_order_amount,
               a.cancel_order_amount,
               a.order_valid_num,
               a.order_amount_total,
               b.settle_order_amount_total,
               b.settle_amount,
               b.settle_valid_num,
               b.create_time,
               b.modify_time,
               b.profit_amount
        FROM (SELECT merchant_id,
                     currency_code,
                     count(0)                                                          as total_order_amount,
                     Count(CASE WHEN order_status in (2, 4, 5) THEN 1 END)             AS cancel_order_amount,
                     Count(CASE WHEN order_status in (0, 1) THEN 1 END)                AS order_valid_num,
                     SUM(CASE WHEN order_status in (0, 1) THEN order_amount_total END) AS order_amount_total
              FROM tybss_new.t_order
              WHERE create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                and create_time <=
                    unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
              GROUP BY merchant_id, currency_code) a
                 LEFT JOIN
             (SELECT SUM(o.order_amount_total) as settle_order_amount_total,
                     SUM(s.settle_amount)      as settle_amount,
                     count(0)                  as settle_valid_num,
                     o.merchant_id             as merchant_id,
                     o.currency_code           as currency_code,
                     unix_timestamp(now())     as create_time,
                     unix_timestamp(now())     as modify_time,
                     SUM(s.profit_amount) * -1 as profit_amount
              FROM tybss_new.t_settle s
                       LEFT JOIN tybss_new.t_order o on o.order_no = s.order_no
              where s.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                and s.create_time <=
                    unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                and s.last_settle = 1
              GROUP BY o.merchant_id, o.currency_code) b
             on a.merchant_id = b.merchant_id and a.currency_code = b.currency_code
    ) as z on z.merchant_id = m.id
    WHERE m.status = 1;

    /*sql结束*/
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_finance_day success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

## 补偿执行event
CALL p_r_merchant_finance_day('2020-05-21');
CALL p_r_merchant_finance_day('2020-05-22');
CALL p_r_merchant_finance_day('2020-05-23');
CALL p_r_merchant_finance_day('2020-05-24');
CALL p_r_merchant_finance_day('2020-05-25');
CALL p_r_merchant_finance_day('2020-05-26');
CALL p_r_merchant_finance_day('2020-05-27');
CALL p_r_merchant_finance_day('2020-05-28');
CALL p_r_merchant_finance_day('2020-05-29');
CALL p_r_merchant_finance_day('2020-05-30');
CALL p_r_merchant_finance_day('2020-05-31');
CALL p_r_merchant_finance_day('2020-06-01');
CALL p_r_merchant_finance_day('2020-06-02');
CALL p_r_merchant_finance_day('2020-06-03');
CALL p_r_merchant_finance_day('2020-06-04');
CALL p_r_merchant_finance_day('2020-06-05');
CALL p_r_merchant_finance_day('2020-06-06');
CALL p_r_merchant_finance_day('2020-06-07');

CALL p_r_merchant_finance_month('2020-06-01');