

### 对账单临时表(tybss-report)
CREATE TABLE `r_merchant_finance_day_temp` (
                                               `merchant_id` varchar(32)  NOT NULL COMMENT '商户Id',
                                               `merchant_code` varchar(64)  NOT NULL COMMENT '商户Code',
                                               `merchant_name` varchar(128)  NOT NULL COMMENT '商户名称',
                                               `agent_level` tinyint(2) DEFAULT NULL COMMENT '商户类型/代理级别(0,直营;1:渠道;2:二级代理)',
                                               `currency` varchar(10)  NOT NULL DEFAULT '' COMMENT '注单币种，CNY:人民币 USD:美元 EUR:欧元 SGD:新元',
                                               `finance_date` varchar(32)  DEFAULT NULL COMMENT '账期 年-月-日',
                                               UNIQUE KEY `un_merchant_id_code_currency` (`merchant_id`,`merchant_code`,`currency`,`finance_date`) USING BTREE COMMENT '唯一索引',
                                               KEY `merchant_code_index` (`merchant_code`) USING BTREE COMMENT '商户编号索引',
                                               KEY `finance_date_index` (`finance_date`) USING BTREE COMMENT '账单日期索引'
) COMMENT='对账单临时表';


