
alter table merchant.merchant_log
    add column domain_self_result varchar(1000)  DEFAULT NULL COMMENT '域名自检结果';

alter table merchant.merchant_log
    add column domain_third_result varchar(1000)  DEFAULT NULL COMMENT '域名第三方检测结果';