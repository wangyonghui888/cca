
alter table merchant.merchant_log
    add column domain_type bigint DEFAULT NULL COMMENT '域名类型';
