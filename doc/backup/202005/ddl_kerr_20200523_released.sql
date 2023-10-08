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
  `execute_rate` double(10,2) DEFAULT '0.00' COMMENT '执行费率%',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `merchant_code_index` (`merchant_code`) USING BTREE COMMENT '商户编号索引',
  KEY `merchant_name_index` (`merchant_name`) USING BTREE COMMENT '商户名称索引',
  KEY `finance_date` (`finance_date`) USING BTREE COMMENT '账期索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='财务-对账单日报表';

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
  `adjust_cause` varchar(256) DEFAULT NULL COMMENT '调整原因',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `merchant_code_index` (`merchant_code`) USING BTREE COMMENT '商户编号索引',
  KEY `merchant_name_index` (`merchant_name`) USING BTREE COMMENT '商户名称索引',
  KEY `profit_amount_index` (`profit_amount`) USING BTREE COMMENT '盈利金额索引',
  KEY `finance_date_index` (`finance_date`) USING BTREE COMMENT '账期索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='财务-清算月报表';


drop table if exists r_merchant_finance_bill_month;
CREATE TABLE `r_merchant_finance_bill_month` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT 'ID  表ID  年-月-merchantCode-currency',
  `finance_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT 'finance_month表id',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '注单币种，CNY:人民币 USD:美元 EUR:欧元 SGD:新元',
  `bill_order_amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '投注金额',
  `bill_order_num` int(11) NOT NULL DEFAULT '0' COMMENT '投注笔数',
  `bill_profit_amount` bigint(20) DEFAULT NULL COMMENT '盈利金额',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `modify_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `elasticsearch_id` bigint(20) NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `finance_id_index` (`finance_id`) USING BTREE COMMENT '外键索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='财务-清算电子账单表';


DROP PROCEDURE if EXISTS p_r_merchant_finance_day;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_merchant_finance_day(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 9;
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
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(z.currency_code, '1'))       as id,
           concat(execute_date, '-', m.merchant_code)                                            as finance_day_id,
           execute_date                                                                          as finance_date,
           unix_timestamp(date_sub(execute_date, interval 1 day)) * 1000 + (12 * 60 * 60 * 1000) as finance_time,
           m.id                                                                                  as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(z.currency_code, '1')                                                        as currency,
           IFNULL(z.order_amount_total, 0)/100                                                   as order_amount_total,
           IFNULL(z.settle_amount, 0)/100                                                        as settle_amount,
           IFNULL(z.total_order_amount, 0)                                                       as order_num,
           IFNULL(z.order_valid_num, 0)                                                          as order_valid_num,
           IFNULL(z.cancel_order_amount, 0)                                                      as order_back_num,
           IFNULL(x.transfer_num, 0)                                                             as transfer_num,
           unix_timestamp(now())                                                                 as create_time,
           unix_timestamp(now())                                                                 as modify_time,
           UNIX_TIMESTAMP(current_timestamp(3))                                                  as elasticsearch_id,
           m.parent_id                                                                           as parent_id,
           (SELECT count(id) FROM tybss_new.t_merchant WHERE status = 1 and parent_id = m.id)    as merchant_num,
           IFNULL(z.profit_amount, 0)/100 														 as profit_amount,
           IFNULL(JSON_UNQUOTE(json_extract(m.fee_json, "$.computingStandard")), '1')            as computing_standard,
           IFNULL(ml.terrace_rate, 0)                                                            as terrace_rate,
           case json_extract(m.fee_json, "$.vipPaymentCycle")
               when 1 then json_extract(m.fee_json, "$.vipAmount")
               when 2 then (case
                                WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then json_extract(m.fee_json, "$.vipAmount")
                                else 0 end)
               when 3 then (case WHEN month(CURRENT_DATE) = 1 then json_extract(m.fee_json, "$.vipAmount") else 0 end)
               else 0 end                                                                        as vip_amount,
           case json_extract(m.fee_json, "$.techniquePaymentCycle")
                 when 1 then json_extract(m.fee_json, "$.techniqueAmount")
                 when 2 then (case
                                                    WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                            then json_extract(m.fee_json, "$.techniqueAmount")
                                                    else 0 end)
                 when 3 then (case
                                                    WHEN month(CURRENT_DATE) = 1 then json_extract(m.fee_json, "$.techniqueAmount")
                                                    else 0 end)
                 else 0 end                                                                      as technique_amount,
           IFNULL(json_extract(m.fee_json, "$.paymentCycle"),'1')                                as payment_cycle,
           IFNULL(json_extract(m.fee_json, "$.vipPaymentCycle"),'1')                             as vip_payment_cycle,
           IFNULL(json_extract(m.fee_json, "$.techniquePaymentCycle"),'1')                       as technique_payment_cycle,
           IFNULL(m.terrace_rate,  IFNULL(ml.terrace_rate, 0))                                   as execute_rate
    FROM tybss_new.t_merchant m
    LEFT JOIN  tybss_new.t_merchant_level ml on m.level=ml.level
    LEFT JOIN
         (
              SELECT u.merchant_code,
                    count(0) as transfer_num
                FROM tybss_new.t_account_change_history ta
				left join tybss_new.t_user u
				on u.uid = ta.uid
             where ta.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
               and ta.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
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
				 FROM   (SELECT   merchant_id,
							    currency_code,
                                count(0) as total_order_amount,
                                Count(CASE WHEN order_status in (2, 4, 5) THEN 1 END) AS cancel_order_amount,
                                Count(CASE WHEN order_status in (0, 1) THEN 1 END) AS order_valid_num,
                                SUM(CASE WHEN order_status in (0, 1) THEN order_amount_total END) AS order_amount_total
                        FROM tybss_new.t_order
                        WHERE create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                        and create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                        GROUP BY merchant_id, currency_code ) a
				LEFT JOIN
                        (SELECT SUM(o.order_amount_total) as settle_order_amount_total,
                               SUM(s.settle_amount)      as settle_amount,
                               count(0)                  as settle_valid_num,
                               o.merchant_id             as merchant_id,
                               o.currency_code           as currency_code,
                               unix_timestamp(now())     as create_time,
                               unix_timestamp(now())     as modify_time,
                               SUM(s.profit_amount)*-1   as profit_amount
                        FROM tybss_new.t_settle s
                        LEFT JOIN tybss_new.t_order o on o.order_no = s.order_no
                        where s.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                        and s.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                        and s.last_settle = 1
                        GROUP BY o.merchant_id, o.currency_code) b
				on a.merchant_id=b.merchant_id and a.currency_code = b.currency_code
    ) as z on z.merchant_id = m.id
    WHERE m.status = 1;

    /*sql结束*/
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_finance_day success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

DROP PROCEDURE if EXISTS p_r_merchant_finance_month;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_merchant_finance_month(in str_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 20;
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
        SET exce_msg = CONCAT(execute_date, "错误码：", result_code, " p_r_merchant_finance_day错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');

    REPLACE INTO tybss_report.r_merchant_finance_month
    SELECT
            concat(left(a.finance_date, 7),'-',a.merchant_code)                                         as id,
            left(a.finance_date, 7)                                                                 as finance_date,
            unix_timestamp(date_sub(date_sub(curdate(), interval day(curdate()) - 1 day),
            interval 1 month)) * 1000 +(12 * 60 * 60 * 1000)                                        as finance_time,
            a.merchant_id                                                                           as merchant_id,
            a.merchant_code                                                                         as merchant_code,
            a.parent_id                                                                             as parent_id,
            a.merchant_num                                                                          as merchant_num,
            a.merchant_name                                                                         as merchant_name,
            a.agent_level                                                                           as agent_level,
            a.currency                                                                              as currency,
            IFNULL( IF((case computing_standard
                when 1 then b.order_amount_total*a.execute_rate / 100
                when 2 then b.profit_amount*a.execute_rate / 100
                else 0 end)<0,
                0,
                (case computing_standard
                when 1 then b.order_amount_total*a.execute_rate / 100
                when 2 then b.profit_amount*a.execute_rate / 100
                else 0 end) ) +
            case IFNULL(a.vip_payment_cycle,1)
               when 1 then IFNULL(a.vip_amount,0)
               when 2 then (case WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then IFNULL(a.vip_amount,0) else 0 end)
               when 3 then (case WHEN month(CURRENT_DATE) = 1 then IFNULL(a.vip_amount,0) else 0 end)
               else 0 end +
            case IFNULL(a.technique_payment_cycle,1)
               when 1 then IFNULL(a.technique_amount,0)
               when 2 then (case WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then IFNULL(a.technique_amount,0) else 0 end)
               when 3 then (case WHEN month(CURRENT_DATE) = 1 then IFNULL(a.technique_amount,0) else 0 end)
               else 0 end , 0)
                                                                                                    as bill_amount,
            IF((case computing_standard
                when 1 then b.order_amount_total*a.execute_rate / 100
                when 2 then b.profit_amount*a.execute_rate / 100
                else 0 end)<0,
                0,
                (case computing_standard
                when 1 then b.order_amount_total*a.execute_rate / 100
                when 2 then b.profit_amount*a.execute_rate / 100
                else 0 end))
                                                                                                    as order_payment_amount,
            b.order_amount_total                                                                    as order_amount_total,
            b.profit_amount                                                                         as profit_amount,
            a.computing_standard                                                                    as computing_standard,
            a.terrace_rate                                                                          as terrace_rate,
            a.execute_rate                                                                          as execute_rate,
            0                                                                                       as adjust_amount,
            IFNULL(a.vip_amount,0)                                                                  as vip_amount,
            IFNULL(a.technique_amount,0)                                                            as technique_amount,
            'admin'                                                                                 as create_user,
            unix_timestamp(now())                                                                   as create_time,
            'admin'                                                                                 as modify_user,
            unix_timestamp(now())                                                                   as modify_time,
            IFNULL(a.payment_cycle,1)                                                               as payment_cycle,
            IFNULL(a.vip_payment_cycle,1)                                                           as vip_payment_cycle,
            IFNULL(a.technique_payment_cycle,1)                                                     as technique_payment_cycle,
            unix_timestamp(date_sub(date_sub(curdate(), interval day(curdate()) - 1 day),
            interval 1 month)) * 1000 +(12 * 60 * 60 * 1000)                                        as elasticsearch_id,
            b.order_num                                                                             as order_num,
            ''                                                                                      as adjust_cause
    FROM
    (select *
        FROM tybss_report.r_merchant_finance_day
        where finance_date = date_sub(date_sub(date_format(now(),'%y-%m-%d'),interval extract(day from now()) day),interval 0 month)
        and currency = '1'
    ) as a left JOIN
		(SELECT
            d.merchant_id                                                                           	                    as merchant_id,
            sum(d.order_amount_total * (SELECT rate FROM tybss_new.t_currency_rate WHERE currency_code = d.currency))      	as order_amount_total,
            sum(d.profit_amount * (SELECT rate FROM tybss_new.t_currency_rate WHERE currency_code = d.currency))           	as profit_amount,
            sum(order_num)                                                                          						as order_num
		FROM (
			select
                merchant_id                                                                          as merchant_id,
                currency                                                                             as currency,
                IFNULL(sum(order_amount_total),0)                                                    as order_amount_total,
                IFNULL(sum(profit_amount),0)                                                         as profit_amount,
                IFNULL(sum(order_valid_num),0)                                                       as order_num
			FROM tybss_report.r_merchant_finance_day
			where concat(left(finance_date, 7)) = date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH),'%Y-%m')
			GROUP BY merchant_id, currency
			) as d GROUP BY merchant_id
    ) as b
    on a.merchant_id = b.merchant_id;

    REPLACE into tybss_report.r_merchant_finance_bill_month
    SELECT
		   concat(concat(left(max(finance_date), 7), '-',merchant_code), '-',currency)         		as id,
           concat(left(max(finance_date), 7), '-',merchant_code)                                    as finance_id,
           currency                                                                       			as currency,
           SUM(order_amount_total)                                                                  as bill_order_amount,
           SUM(order_valid_num)                                                                           as bill_order_num,
           SUM(profit_amount)                                                                       as bill_profit_amount,
           unix_timestamp(now())                                                                    as create_time,
           unix_timestamp(now())                                                                    as modify_time,
           unix_timestamp(date_sub(date_sub(curdate(), interval day(curdate()) - 1 day), interval 1 month)) * 1000 +
           (12 * 60 * 60 * 1000)                                                                    as elasticsearch_id
    FROM tybss_report.r_merchant_finance_day
	where concat(left(finance_date, 7)) = date_format(DATE_SUB(execute_date, INTERVAL 1 MONTH),'%Y-%m')
	GROUP BY merchant_code, currency;


    /*sql结束*/
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_finance_month success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

drop event if EXISTS e_r_merchant_finance_day_yesterday;
drop event if EXISTS e_r_merchant_finance_before_yesterday;
CREATE EVENT e_r_merchant_finance_day_yesterday ON SCHEDULE EVERY 3600 SECOND DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY));
CREATE EVENT e_r_merchant_finance_before_yesterday ON SCHEDULE EVERY 12 hour DO CALL p_r_merchant_finance_day(DATE_ADD(CURRENT_DATE,INTERVAL -2 DAY));

drop event if EXISTS e_r_merchant_finance_month;
drop event if EXISTS e_r_merchant_finance_month_yesterday;
drop event if EXISTS e_r_merchant_finance_month_before_yesterday;
CREATE EVENT e_r_merchant_finance_month ON SCHEDULE EVERY 24 hour DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE,INTERVAL -1 DAY));
CREATE EVENT e_r_merchant_finance_month_yesterday ON SCHEDULE EVERY 24 hour DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE,INTERVAL -2 DAY));
CREATE EVENT e_r_merchant_finance_month_before_yesterday ON SCHEDULE EVERY 24 hour DO CALL p_r_merchant_finance_month(DATE_ADD(CURRENT_DATE,INTERVAL -3 DAY));
