#业务tybss_report中执行
alter table tybss_report.r_merchant_finance_day MODIFY `order_amount_total` decimal(32,2) NOT NULL DEFAULT '0.0' COMMENT '投注金额';
alter table tybss_report.r_merchant_finance_day MODIFY  `settle_amount` decimal(32,2) DEFAULT '0.0' COMMENT '派彩金额';
alter table tybss_report.r_merchant_finance_day MODIFY  `profit_amount` decimal(32,2) DEFAULT NULL COMMENT '盈利金额（月账单专用）';
alter table tybss_report.r_merchant_finance_day MODIFY  `vip_amount` decimal(32,2) DEFAULT NULL COMMENT 'VIP费用（月账单专用）';
alter table tybss_report.r_merchant_finance_day MODIFY  `technique_amount` decimal(32,2) DEFAULT NULL COMMENT '技术费用（月账单专用）';

alter table tybss_report.r_merchant_finance_month MODIFY  `bill_amount` decimal(32,2) DEFAULT '0.0' COMMENT '账单金额：a*费率()+vip费+技术服务费';
alter table tybss_report.r_merchant_finance_month MODIFY  `order_payment_amount` decimal(32,2) NOT NULL DEFAULT '0.0' COMMENT '应缴费用：a*费率';
alter table tybss_report.r_merchant_finance_month MODIFY  `order_amount_total` decimal(32,2) NOT NULL DEFAULT '0.0' COMMENT '投注金额';
alter table tybss_report.r_merchant_finance_month MODIFY  `profit_amount` decimal(32,2) DEFAULT NULL COMMENT '盈利金额';
alter table tybss_report.r_merchant_finance_month MODIFY `adjust_amount` decimal(32,2) DEFAULT '0.0' COMMENT '调整额,含正负';
alter table tybss_report.r_merchant_finance_month MODIFY `vip_amount` decimal(32,2) DEFAULT NULL COMMENT 'VIP费用';
alter table tybss_report.r_merchant_finance_month MODIFY `technique_amount` decimal(32,2) DEFAULT NULL COMMENT '技术费用';

alter table tybss_report.r_merchant_finance_bill_month MODIFY `bill_order_amount` decimal(32,2) NOT NULL DEFAULT '0.0' COMMENT '投注金额';
alter table tybss_report.r_merchant_finance_bill_month MODIFY `bill_profit_amount` decimal(32,2) DEFAULT NULL COMMENT '盈利金额';

alter table tybss_report.r_merchant_finance_operate_record MODIFY `adjust_amount_before` decimal(32,2) DEFAULT '0.0' COMMENT '调整前-调整额,含正负';
alter table tybss_report.r_merchant_finance_operate_record MODIFY `adjust_amount` decimal(32,2) DEFAULT '0.0' COMMENT '调整额,含正负';

ALTER TABLE tybss_report.r_merchant_finance_day ADD INDEX idx_finance_day_id ( `finance_day_id` );
ALTER TABLE tybss_report.r_merchant_finance_month ADD INDEX idx_parent_id ( `parent_id` );

#保留2位小数点问题:数据清理重置
DELETE FROM r_merchant_finance_day;
DELETE FROM r_merchant_finance_month;
DELETE FROM r_merchant_finance_bill_month;
DELETE FROM r_merchant_finance_operate_record;

CALL p_r_merchant_finance_day('2020-05-01');
CALL p_r_merchant_finance_day('2020-05-02');
CALL p_r_merchant_finance_day('2020-05-03');
CALL p_r_merchant_finance_day('2020-05-04');
CALL p_r_merchant_finance_day('2020-05-05');
CALL p_r_merchant_finance_day('2020-05-06');
CALL p_r_merchant_finance_day('2020-05-07');
CALL p_r_merchant_finance_day('2020-05-08');
CALL p_r_merchant_finance_day('2020-05-09');
CALL p_r_merchant_finance_day('2020-05-10');
CALL p_r_merchant_finance_day('2020-05-11');
CALL p_r_merchant_finance_day('2020-05-12');
CALL p_r_merchant_finance_day('2020-05-13');
CALL p_r_merchant_finance_day('2020-05-14');
CALL p_r_merchant_finance_day('2020-05-15');
CALL p_r_merchant_finance_day('2020-05-16');
CALL p_r_merchant_finance_day('2020-05-17');
CALL p_r_merchant_finance_day('2020-05-18');
CALL p_r_merchant_finance_day('2020-05-19');
CALL p_r_merchant_finance_day('2020-05-20');
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
CALL p_r_merchant_finance_day('2020-06-08');
CALL p_r_merchant_finance_day('2020-06-09');
CALL p_r_merchant_finance_day('2020-06-10');
CALL p_r_merchant_finance_day('2020-06-11');
CALL p_r_merchant_finance_day('2020-06-12');

CALL p_r_merchant_finance_month('2020-06-01');
